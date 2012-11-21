/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package booster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ui.IUserInterface;
import ui.TextUI;
import util.DataSetReader;
import util.DirectoryBrowser;
import util.Pair;
import classifier.IBuilder;
import classifier.IClassifier;
import classifier.bayesian.BayesianBuilder;
import classifier.dataset.DataSet;
import classifier.dataset.discretization.Discretization;
import classifier.dataset.discretization.HistogramEqualization;
import classifier.dataset.discretization.IDataSetPreProcess;
import classifier.dataset.discretization.TenFoldSplitting;
import classifier.decisiontree.DecisionTreeBuilder;
import java.util.HashMap;

/**
 *
 * @author Nicklas
 */
public class AdaBooster implements IBooster {

    public static Class[] availableClassifiers = new Class[]{BayesianBuilder.class, DecisionTreeBuilder.class};
    public static Class[] availablePreProcesses = new Class[]{TenFoldSplitting.class, HistogramEqualization.class};
    private final IUserInterface ui;
    private DataSet data;
    private DataSet trainingData;
    private DataSet testingData;
    private ClassifierEnsemble ensemble;    
    private Discretization discretization;

    public AdaBooster(IUserInterface ui) {
        HashMap<Integer, String> attr = new HashMap<>();
        attr.put(0, "Shape");
        attr.put(1, "Size");
        attr.put(2, "Color");
//        AttributeNode.attributeName = attr;
        this.ui = ui;
    }
    public void startTest() throws Exception {
        String dataFile = ui.requestString("Pick a data set:\n" + DirectoryBrowser.resources());
        double split = 80.0;
        
        List<IDataSetPreProcess> preprocesses = new LinkedList<>();
//        preprocesses.add(new HistogramEqualization());
        preprocesses.add(new TenFoldSplitting());
        IDataSetPreProcess[] processes = new IDataSetPreProcess[preprocesses.size()];
        processes = preprocesses.toArray(processes);
        this.discretization = new Discretization(processes);
        
        String classifiers = ui.requestString("Enter the number of NBC and DTC:depth, eg: 5 5:5");
        String[] classSplit = classifiers.split("\\s");
        int NBC = Integer.valueOf(classSplit[0]);
        classSplit = classSplit[1].split(":");
        int DTC = Integer.valueOf(classSplit[0]);
        int DTCDepth = Integer.valueOf(classSplit[1]);
        DecisionTreeBuilder.depth = DTCDepth;
        
        List<Pair<IBuilder, Integer>> builders = new LinkedList<>();
        builders.add(new Pair<>((IBuilder)new BayesianBuilder(), NBC));
        builders.add(new Pair<>((IBuilder)new DecisionTreeBuilder(), DTC));
        
        DataSetReader dsr = new DataSetReader();
        ArrayList<double[]> rawData = dsr.read(dataFile);
        Collections.shuffle(rawData);
        double[][] rawMatrix = new double[rawData.size()][rawData.get(0).length];
        rawMatrix = rawData.toArray(rawMatrix);
        data = discretization.process(rawMatrix);
        
        //Split into trainingData and testData
        int trainingSize = (int) (data.length() * split / 100);
        trainingData = data.subset(0, trainingSize);
        testingData = data.subset(trainingSize, data.length());
        this.ensemble = new ClassifierEnsemble(trainingData, testingData);
        buildClassifiers(builders, trainingData);
    }
    public void start() throws Exception {
        String dataFile = ui.requestString("Pick a data set:\n" + DirectoryBrowser.resources());
        
        double trainingTestSplit = ui.requestDouble("Percantage used for training? [ 0 , 100 ]");
        
        int discIndex;
        List<IDataSetPreProcess> preprocesses = new LinkedList<>();
        while ((discIndex = ui.requestChoice("Select discretization strategy, end with -1", availablePreProcesses)) != -1) {
            preprocesses.add(((IDataSetPreProcess)availablePreProcesses[discIndex-1].newInstance()));
        }
        IDataSetPreProcess[] processes = new IDataSetPreProcess[preprocesses.size()];
        processes = preprocesses.toArray(processes);
        this.discretization = new Discretization(processes);
        
        int classifierIndex;
        List<Pair<IBuilder, Integer>> builders = new LinkedList<>();
        while ((classifierIndex = ui.requestChoice("Select classifier, end with -1", availableClassifiers)) != -1) {
            IBuilder ib = (IBuilder)availableClassifiers[classifierIndex-1].newInstance();
            int nof = ui.requestInt("How many would you like?");
            if (ib.getClass().equals(DecisionTreeBuilder.class) && DecisionTreeBuilder.depth == -1){
                DecisionTreeBuilder.depth = ui.requestInt("Specify the maximum depth of the three: ");
            }
            builders.add(new Pair<>(ib, nof));
        }

        //Read dataset to data
        DataSetReader dsr = new DataSetReader();
        ArrayList<double[]> rawData = dsr.read(dataFile);
        Collections.shuffle(rawData);
        double[][] rawMatrix = new double[rawData.size()][rawData.get(0).length];
        rawMatrix = rawData.toArray(rawMatrix);
        data = discretization.process(rawMatrix);
        
        //Split into trainingData and testData
        int trainingSize = (int) (data.length() * trainingTestSplit / 100);
        trainingData = data.subset(0, trainingSize);
        testingData = data.subset(trainingSize, data.length());
        this.ensemble = new ClassifierEnsemble(trainingData, testingData);
        buildClassifiers(builders, trainingData);
    }
    public void buildClassifiers(List<Pair<IBuilder, Integer>> builders, DataSet baseDataSet) {
        DataSet dataSet = baseDataSet.subset(0, baseDataSet.length());
        for (Pair<IBuilder, Integer> ib : builders) {
            for (int i = 0; i < ib.second; i++) {
                Pair<IClassifier, DataSet> r = ib.first.build(dataSet);
                ensemble.addClassifier(r.first);
                dataSet = r.second;
            }
        }
        classifyTestSet();
    }

    public void classifyTestSet() {
        ensemble.test();
    }

    public static void main(String[] args) throws Exception {
        AdaBooster b = new AdaBooster(new TextUI());
        b.start();
    }
}
