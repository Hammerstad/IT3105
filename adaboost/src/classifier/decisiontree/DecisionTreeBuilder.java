/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.decisiontree;

import classifier.dataset.DataSet;
import classifier.IBuilder;
import classifier.IClassifier;
import classifier.dataset.matcher.DataSetMatcher;
import classifier.dataset.matcher.attribute.AttributeEqualsMatcher;
import classifier.dataset.matcher.matcher.CategoryMatcher;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nicklas
 */
public class DecisionTreeBuilder extends IBuilder {

    double eps = 0.000000000001;

    @Override
    protected IClassifier generateHypothesis(DataSet ds) {
        int numberOfAttributes = ds.get(0).getAttributes().length;
        List<Integer> selectableAttributes = new LinkedList<>();
        for (int i = 0; i < numberOfAttributes; i++) {
            selectableAttributes.add(i);
        }
        return null;
    }
    private Node buildTree(DataSet ds, List<Integer> selectableAttributes, int depth) {
        int chooseAttribute = chooseSplittingAttribute(ds, selectableAttributes);
        if (chooseAttribute == -1) {
            throw new RuntimeException("Found no splitting attribute");
        }else if (depth == 0){
            int[] classes = ds.getClasses();
            int[] found = new int[classes.length];
            int foundInd = 0;
            for (int cls : classes) {
                found[foundInd++] = DataSetMatcher.filter(ds, new CategoryMatcher(cls)).length();
            }
            int maxInd = 0;
            boolean unique = true;
            for (int i = 1; i < classes.length; i++){
                if (found[maxInd] < found[i]){
                    maxInd = i;
                    unique = true;
                }else if (found[maxInd] == found[i]) {
                    unique = false;
                }
            }
            if (unique) {
                return new LeafNode(classes[maxInd]);
            }else {
                return new LeafNode(-1);
            }
        }
        if (isMapped(ds)){
            return new LeafNode(ds.get(0).getCategory());
        }else {
            Node n = new AttributeNode(chooseAttribute);
            double[] attributeValues = ds.getAttributeValues(chooseAttribute);
            DataSet[] dss = new DataSet[attributeValues.length];
            int dssInd = 0;
            for (double attrValue : attributeValues) {
                dss[dssInd] = DataSetMatcher.filter(ds, new AttributeEqualsMatcher(chooseAttribute, attrValue));
                if (isMapped(dss[dssInd])) {
                    n.addChild(new LeafNode(dss[dssInd].getClasses()[0]), attrValue);
                }else {
                    n.addChild(buildTree(dss[dssInd], new LinkedList<>(selectableAttributes), depth-1), attrValue);
                }
                dssInd++;
            }
        }
        return null;
    }
    private boolean isMapped(DataSet ds) {
        return ds.getClasses().length==1;
    }
    private int chooseSplittingAttribute(DataSet ds, List<Integer> selectableAttributes) {
        if (ds.length() == 0 || selectableAttributes.isEmpty()){
            return -1;
        }
        double[] entropy = new double[selectableAttributes.size()];
        for (int attributeIndex = 0; attributeIndex < selectableAttributes.size(); attributeIndex++) {
            int attributeIndexD = selectableAttributes.get(attributeIndex);
            double[] distinctValuesForAttribute = ds.getAttributeValues(attributeIndexD);
            for (int a = 0, aL = distinctValuesForAttribute.length; a < aL; a++) {
                double attributeValue = distinctValuesForAttribute[a];
                double PVk = DataSetMatcher.filter(ds, new AttributeEqualsMatcher(attributeIndexD, attributeValue)).length() / (ds.length() * 1.0);
                entropy[attributeIndex] += PVk * log2(PVk);
            }
        }
        double max = Double.MIN_VALUE;
        int maxIndT = -1;
        for (int maxInd = 0; maxInd < entropy.length; maxInd++) {
            if (entropy[maxInd] > max) {
                max = entropy[maxInd];
                maxIndT = maxInd;
            }
        }
        return selectableAttributes.remove(maxIndT);
    }

    private static double log2(double d) {
        return Math.log(d) / Math.log(2);
    }
}
