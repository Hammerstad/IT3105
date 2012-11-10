package classifier.decisiontree.AI2;


import java.io.*;
import java.util.*;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Nicklas
 */
public class Main {

    double eps = 0.00000001;

    public static enum AttributeDecision {

        TRIVIAL, RANDOM, INFORMATION
    };
    public static boolean gotGraphviz = true;
    public static AttributeDecision method = AttributeDecision.RANDOM;

    public static void main(String[] args) throws Exception {
        Main mr = new Main();

        int[][] training = mr.read(new File("training.txt"));
        int[][] test = mr.read(new File("test.txt"));
        int NOF_attributes = training[0].length - 1;
        List<Integer> attrList = new ArrayList<Integer>();
        for (int i = 0; i < NOF_attributes; i++) {
            attrList.add(i);
        }
        Node root = mr.buildTree(training, attrList, method);

        printGraph(root);
        
        double d = mr.classification(root, test);
        System.out.println("Classification: "+d);
        
        if (gotGraphviz) {
            Graphviz gv = new Graphviz();
            gv.createGraph(root, d);
            gv.show();
        }

    }

    private Node buildTree(int[][] matrix, List<Integer> attributeList, AttributeDecision decision) {
        int chooseAttr = choose(matrix, decision, attributeList);
        if (chooseAttr == -1) {
            return null;
        }
        //System.out.println("Choose: " + chooseAttr);
        int isMapped = isMapped(matrix, chooseAttr);

        if (isMapped > 0) {
            //System.out.println("mapping: " + isMapped);
            return new LeafNode(isMapped);
        } else {
            int[][][] s = splitMatrix(matrix, chooseAttr);
            int mappingOnes = isMapped(s[0], chooseAttr);
            int mappingTwos = isMapped(s[1], chooseAttr);

            Node n = new AttributeNode(chooseAttr);
            if (mappingOnes > 0) {
                n.addChild(new LeafNode(mappingOnes), 1);
            } else {
                n.addChild(buildTree(s[0], cloneList(attributeList), decision), 1);
            }
            if (mappingTwos > 0) {
                n.addChild(new LeafNode(mappingTwos), 2);
            } else {
                n.addChild(buildTree(s[1], cloneList(attributeList), decision), 2);
            }
            return n;
        }
    }

    private int isMapped(int[][] matrix, int attr) {
        if (matrix.length == 0) {
            return -1;
        }
        int first = matrix[0][attr], cls = matrix[0][matrix[0].length - 1];
        //System.out.println("First: " + first + " size: " + matrix.length);
        for (int[] row : matrix) {
            if (row[attr] != first || row[matrix[0].length - 1] != cls) {
                //System.out.println("DealBreaker: " + row[attr]);
                return -1;
            }
        }
        return cls;
    }

    private int choose(int[][] matrix, AttributeDecision d, List<Integer> attrList) {
        if (attrList.isEmpty() || matrix.length == 0) {
            return -1;
        } else if (d == AttributeDecision.TRIVIAL) {
            return attrList.remove(0);
        } else if (d == AttributeDecision.RANDOM) {
            return attrList.remove((int) Math.floor(Math.random() * attrList.size()));
        } else if (d == AttributeDecision.INFORMATION) {
            double[] entropy = new double[attrList.size()];
            for (int index = 0; index < attrList.size(); index++) {
                for (int value = 1; value < 3; value++) {
                    double p = 0;
                    double n = 0;
                    for (int row = 0; row < matrix.length; row++) {
                        if (matrix[row][attrList.get(index)] == value && matrix[row][matrix[row].length - 1] == 1) {
                            p++;
                        } else if (matrix[row][attrList.get(index)] == value && matrix[row][matrix[row].length - 1] == 2) {
                            n++;
                        }
                    }
                    p += eps;
                    n += eps;
                    entropy[index] = entropy[index] + (p + n) / matrix.length * (-p / (p + n) * log2(p / (p + n)) - n / (p + n) * log2(n / (p + n)));
                }
            }
            double m = Double.MAX_VALUE;
            int index = 0;
            for (int i = 0; i < entropy.length; i++) {
                if (entropy[i] < m) {
                    index = i;
                    m = entropy[i];
                }
            }
            return attrList.remove(index);
        }
        return -1;
    }

    private double classification(Node root, int[][] matrix) {
        double c = 0, nof = 0;

        for (int[] r : matrix) {
            if (classificationSingle((AttributeNode) root, r)) {
                c++;
            }
            nof++;
        }
        return c / nof;
    }

    private boolean classificationSingle(AttributeNode root, int[] row) {
        Stack<Node> stack = new Stack<Node>();
        stack.push(root);
        Node current;
        while (!stack.isEmpty()) {
            current = stack.pop();
            if (current instanceof AttributeNode) {
                AttributeNode an = (AttributeNode) current;
                int attrNo = an.attr;
                int attrVal = row[attrNo];
                //Find child with this choice

                for (Edge e : an.children) {
                    if (e.choice == attrVal) {
                        stack.push(e.child);
                    }
                }
            } else if (current instanceof LeafNode) {
                LeafNode ln = (LeafNode) current;
                return (ln.cls == row[row.length - 1]);
            }
        }
        return false;
    }

    private int[][] read(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = new LinkedList<String>();
        String line;

        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();

        int NOF_attributes = lines.get(0).split("\\s").length;
        int NOF_examples = lines.size();

        int[][] matrix = new int[NOF_examples][NOF_attributes];

        StringTokenizer st;
        int attributeCounter = 0, exampleCounter = 0;
        for (String l : lines) {
            st = new StringTokenizer(l, "	");
            attributeCounter = 0;
            while (st.hasMoreTokens()) {
                matrix[exampleCounter][attributeCounter++] = st.nextToken().equals("1") ? 1 : 2;
            }
            exampleCounter++;
        }

        return matrix;
    }

    private int[][][] splitMatrix(int[][] matrix, int attr) {
        //System.out.println("Spliting on attr: " + attr);
        int NOF_ones = 0, NOF_twos = 0;
        for (int[] row : matrix) {
            if (row[attr] == 1) {
                NOF_ones++;
            } else {
                NOF_twos++;
            }
        }
        //System.out.println("Found:: ones: " + NOF_ones + " twos: " + NOF_twos);
        int[][] ones = new int[NOF_ones][matrix[0].length];
        int[][] twos = new int[NOF_twos][matrix[0].length];
        int onesC = 0, twosC = 0;

        for (int[] row : matrix) {
            if (row[attr] == 1) {
                ones[onesC++] = row;
            } else {
                twos[twosC++] = row;
            }
        }
        return new int[][][]{ones, twos};
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

    private List<Integer> cloneList(List<Integer> list) {
        List<Integer> a = new LinkedList<Integer>();
        for (int i : list) {
            a.add(i);
        }
        return a;
    }

    private static double log2(double d) {
        return Math.log(d) / Math.log(2);
    }
}
