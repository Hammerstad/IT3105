/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.decisiontree;

import classifier.dataset.DataSet;
import classifier.IBuilder;
import classifier.IClassifier;
import classifier.dataset.Instance;
import classifier.dataset.matcher.DataSetMatcher;
import classifier.dataset.matcher.attribute.AttributeEqualsMatcher;
import classifier.dataset.matcher.matcher.CategoryMatcher;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nicklas
 */
public class DecisionTreeBuilder extends IBuilder {

    public static int depth = -1;
    double eps = 0.000000000001;

    @Override
    protected IClassifier generateHypothesis(DataSet ds) {
        int numberOfAttributes = ds.get(0).getAttributes().length;
        List<Integer> selectableAttributes = new LinkedList<>();
        for (int i = 0; i < numberOfAttributes; i++) {
            selectableAttributes.add(i);
        }
        Node n = buildTree(ds, selectableAttributes, depth);
//        printGraph(n);
        return new DecisionTreeClassifier(n);
    }

    private Node buildTree(DataSet ds, List<Integer> selectableAttributes, int depth) {
        int chooseAttribute = chooseSplittingAttribute(ds, selectableAttributes);
        if (chooseAttribute == -1 || depth == 0) {
            int[] classes = ds.getClasses();
            int[] found = new int[classes.length];
            int foundInd = 0;
            for (int cls : classes) {
                found[foundInd++] = DataSetMatcher.filter(ds, new CategoryMatcher(cls)).length();
            }
            int maxInd = 0;
            boolean unique = true;
            for (int i = 1; i < classes.length; i++) {
                if (found[maxInd] < found[i]) {
                    maxInd = i;
                    unique = true;
                } else if (found[maxInd] == found[i]) {
                    unique = false;
                }
            }
            if (unique) {
                return new LeafNode(classes[maxInd]);
            } else {
                return new LeafNode(-1);
            }
        }
        if (isMapped(ds)) {
            return new LeafNode(ds.get(0).getCategory());
        } else {
            Node n = new AttributeNode(chooseAttribute);
            double[] attributeValues = ds.getAttributeValues(chooseAttribute);
            DataSet[] dss = new DataSet[attributeValues.length];
            int dssInd = 0;
            for (double attrValue : attributeValues) {
                dss[dssInd] = DataSetMatcher.filter(ds, new AttributeEqualsMatcher(chooseAttribute, attrValue));
                if (isMapped(dss[dssInd])) {
                    n.addChild(new LeafNode(dss[dssInd].getClasses()[0]), attrValue);
                } else {
                    n.addChild(buildTree(dss[dssInd], new LinkedList<>(selectableAttributes), depth - 1), attrValue);
                }
                dssInd++;
            }
            return n;
        }
    }

    private boolean isMapped(DataSet ds) {
        return ds.getClasses().length == 1;
    }

    private int chooseSplittingAttribute(DataSet ds, List<Integer> selectableAttributes) {
        if (ds.length() == 0 || selectableAttributes.isEmpty()) {
            return -1;
        }
        double[] entropy = new double[selectableAttributes.size()];
        double allWeights = 0;
        for (Instance i : ds.getInstances()){
            allWeights+=i.getWeight();
        }
//        System.out.println("AllWeights: "+allWeights);
        for (int attributeIndex = 0; attributeIndex < selectableAttributes.size(); attributeIndex++) {
            int attributeIndexD = selectableAttributes.get(attributeIndex);
            double[] distinctValuesForAttribute = ds.getAttributeValues(attributeIndexD);
            for (int a = 0, aL = distinctValuesForAttribute.length; a < aL; a++) {
                double attributeValue = distinctValuesForAttribute[a];
                DataSet DPVk = DataSetMatcher.filter(ds, new AttributeEqualsMatcher(attributeIndexD, attributeValue));
                double branchWeight = 0;
                for (Instance bi : DPVk.getInstances()){
                    branchWeight+=bi.getWeight();
                }
                branchWeight+=Double.MIN_VALUE; //Just to avoid log(0)
//                double PVk = DataSetMatcher.filter(ds, new AttributeEqualsMatcher(attributeIndexD, attributeValue)).length() / (ds.length() * 1.0);
                double PVk = branchWeight/allWeights;
                
                entropy[attributeIndex] += PVk * log2(PVk);
            }
        }
        for (int i = 0; i < entropy.length; i++) {
            entropy[i] *= -1;
        }
        double min = Double.MAX_VALUE;
        int minIndT = -1;
        for (int minInd = 0; minInd < entropy.length; minInd++) {
//            System.out.println("Testing: "+entropy[maxInd]+" "+max);
            if (entropy[minInd] < min) {
                min = entropy[minInd];
                minIndT = minInd;
            }
        }
        System.out.println("Entropy: "+Arrays.toString(entropy));
        return selectableAttributes.remove(minIndT);
    }

    private static double log2(double d) {
        return Math.log(d) / Math.log(2);
    }

    public static void printGraph(Node root) {
        printGraph(root, 0);
    }

    private static void printGraph(Node root, int indent) {
        if (root == null) {
            return;
        }
        for (int i = 0; i < indent; i++) {
            System.out.print(" ");
        }
        System.out.println(root.toString());
        for (Node c : root.getChildren()) {
            printGraph(c, indent + 4);
        }
    }
}
