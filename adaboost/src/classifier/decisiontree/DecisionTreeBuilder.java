/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier.decisiontree;

import classifier.IBuilder;
import classifier.IClassifier;
import classifier.dataset.DataSet;
import classifier.dataset.Instance;
import classifier.dataset.matcher.DataSetMatcher;
import classifier.dataset.matcher.attribute.AttributeEqualsMatcher;
import classifier.dataset.matcher.matcher.CategoryMatcher;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import util.MathHelper;

/**
 *
 * @author Nicklas
 */
public class DecisionTreeBuilder extends IBuilder {
    public static int depth = -1;

    @Override
    protected IClassifier generateHypothesis(DataSet ds) {
        int numberOfAttributes = ds.get(0).getAttributes().length;
        List<Integer> selectableAttributes = new LinkedList<>();
        for (int i = 0; i < numberOfAttributes; i++) {
            selectableAttributes.add(i);
        }
//        System.out.println("Weigths: "+MathHelper.sum(ds.getInstanceWeights()));
        Node n = buildTree(ds, selectableAttributes, depth);
//        printGraph(n);
        return new DecisionTreeClassifier(n);
    }

    private Node buildTree(DataSet ds, List<Integer> selectableAttributes, int depth) {
        int attribute = findMinEntropyIndex(ds, selectableAttributes);
        if (attribute == -1 || depth == 0) {
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
        } else if (hasUniqueClass(ds)) {
            return new LeafNode(ds.get(0).getCategory());
        } else {
            Node n = new AttributeNode(attribute);
            DataSet[] subsets = createSubSets(ds, attribute);
            for (DataSet child : subsets) {
                if (hasUniqueClass(child)) {
                    n.addChild(new LeafNode(child.getClasses()[0]), child.get(0).getAttributes()[attribute]);
                } else {
                    n.addChild(buildTree(child, new LinkedList<>(selectableAttributes), depth - 1), child.get(0).getAttributes()[attribute]);
                }
            }
            return n;
        }
    }

    private static DataSet[] createSubSets(DataSet ds, int attribute) {
        double[] attributeValues = ds.getAttributeValues(attribute);
        DataSet[] subs = new DataSet[attributeValues.length];
        for (int i = 0; i < subs.length; i++) {
            subs[i] = DataSetMatcher.filter(ds, new AttributeEqualsMatcher(attribute, attributeValues[i]));
        }
        return subs;
    }

    private boolean hasUniqueClass(DataSet ds) {
        return ds.getClasses().length == 1;
    }

    private static int findMinEntropyIndex(DataSet ds, List<Integer> selectableIndices) {
        if (ds.length() == 0 || selectableIndices.isEmpty()) {
            return -1;
        }
        double min = Double.MAX_VALUE;
        int selectedIndex = -1;
        for (Integer i : selectableIndices) {
            double e = entropy(ds, i);
            if (e < min) {
                min = e;
                selectedIndex = i;
            }
        }
        selectableIndices.remove((Integer) selectedIndex);
        System.out.println("Choosing "+selectedIndex);
        return selectedIndex;
    }

    private static double entropy(DataSet ds, int splitAttribute) {
        int[] classes = ds.getClasses();
        double[] entropies = new double[ds.getAttributeValues(splitAttribute).length];
        int entropesInd = 0;
        double dataSetWeight = MathHelper.sum(ds.getInstanceWeights());
        for (double attributeValues : ds.getAttributeValues(splitAttribute)) {
            DataSet localds = DataSetMatcher.filter(ds, new AttributeEqualsMatcher(splitAttribute, attributeValues));
            double localEntropy = 0;
            double localWeight = MathHelper.sum(localds.getInstanceWeights());
            for (int cls : classes) {
                DataSet localSet = DataSetMatcher.filter(localds, new CategoryMatcher(cls));
                double frac = localSet.length() / (localds.length() * 1.0);
                localEntropy += -frac * log2(frac);
            }
//            entropies[entropesInd++] = ((localds.length() * 1.0) / ds.length()) * localEntropy;
            entropies[entropesInd++] = (localWeight/dataSetWeight)*localEntropy;
        }
//        System.out.println(Arrays.toString(entropies));
//        System.out.println("Entropy: " + MathHelper.sum(entropies)+" when splitting on "+splitAttribute);
        return MathHelper.sum(entropies);
    }

    public enum Shape {

        CIRCLE(1), STAR(2), DIAMOND(3);
        int i;

        private Shape(int i) {
            this.i = i;
        }

        public int getValue() {
            return this.i;
        }
    }

    public enum Size {

        BIG(1), SMALL(2);
        int i;

        private Size(int i) {
            this.i = i;
        }

        public int getValue() {
            return this.i;
        }
    }

    public enum Color {

        GREEN(1), BLUE(2), RED(3);
        int i;

        private Color(int i) {
            this.i = i;
        }

        public int getValue() {
            return this.i;
        }
    }

    private static double log2(double d) {
        if (d == 0) {
            return 0;
        }
        return Math.log(d) / Math.log(2);
    }

    public static void main(String[] args) {

        DataSet ds = new DataSet(new Instance[]{
                    new Instance(new double[]{Shape.CIRCLE.i, Size.BIG.i, Color.GREEN.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.CIRCLE.i, Size.BIG.i, Color.GREEN.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.CIRCLE.i, Size.BIG.i, Color.BLUE.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.CIRCLE.i, Size.SMALL.i, Color.GREEN.i, 2}, 1.0 / 16),
                    new Instance(new double[]{Shape.CIRCLE.i, Size.SMALL.i, Color.RED.i, 2}, 1.0 / 16),
                    new Instance(new double[]{Shape.STAR.i, Size.BIG.i, Color.RED.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.STAR.i, Size.BIG.i, Color.RED.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.STAR.i, Size.BIG.i, Color.BLUE.i, 2}, 1.0 / 16),
                    new Instance(new double[]{Shape.STAR.i, Size.BIG.i, Color.GREEN.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.STAR.i, Size.SMALL.i, Color.RED.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.STAR.i, Size.SMALL.i, Color.BLUE.i, 2}, 1.0 / 16),
                    new Instance(new double[]{Shape.DIAMOND.i, Size.BIG.i, Color.GREEN.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.DIAMOND.i, Size.BIG.i, Color.BLUE.i, 1}, 1.0 / 16),
                    new Instance(new double[]{Shape.DIAMOND.i, Size.BIG.i, Color.GREEN.i, 2}, 1.0 / 16),
                    new Instance(new double[]{Shape.DIAMOND.i, Size.SMALL.i, Color.BLUE.i, 2}, 1.0 / 16),
                    new Instance(new double[]{Shape.DIAMOND.i, Size.SMALL.i, Color.RED.i, 1}, 1.0 / 16)
                });
        
        DecisionTreeBuilder.depth = 5;
        DecisionTreeBuilder b = new DecisionTreeBuilder();
        b.build(ds);
    }
}
