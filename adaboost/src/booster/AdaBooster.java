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
import classifier.dataset.discretization.IDataSetPreProcess;
import classifier.dataset.discretization.TenFoldSplitting;

/**
 *
 * @author Nicklas
 */
public class AdaBooster implements IBooster {

    public static Class[] availableClassifiers = new Class[]{BayesianBuilder.class};
    public static Class[] availablePreProcesses = new Class[]{TenFoldSplitting.class};
    private final IUserInterface ui;
    private DataSet data;
    private DataSet trainingData;
    private DataSet testingData;
    private ClassifierEnsemble ensemble;
    private Discretization discretization;

    public AdaBooster(IUserInterface ui) {
        this.ui = ui;
        this.ensemble = new ClassifierEnsemble();
    }

    public void start() throws Exception {
        String dataFile = ui.requestString("Pick a data set:\n" + DirectoryBrowser.resources());
        double trainingTestSplit = ui.requestDouble("Percantage used for training? [ 0 , 100 ]");
        
        
        int discIndex;
        List<IDataSetPreProcess> preprocesses = new LinkedList<>();
        while ((discIndex = ui.requestChoice("Select discretization strategy, end with -1", availablePreProcesses)) != -1) {
            preprocesses.add(((IDataSetPreProcess)availablePreProcesses[discIndex].newInstance()));
        }
        IDataSetPreProcess[] processes = new IDataSetPreProcess[preprocesses.size()];
        processes = preprocesses.toArray(processes);
        this.discretization = new Discretization(processes);
        
        int classifierIndex;
        List<Pair<IBuilder, Integer>> builders = new LinkedList<>();
        while ((classifierIndex = ui.requestChoice("Select classifier, end with -1", availableClassifiers)) != -1) {
            IBuilder ib = (IBuilder)availableClassifiers[classifierIndex].newInstance();
            int nof = ui.requestInt("How many would you like?");
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
        
        buildClassifiers(builders, trainingData);
    }
    public void buildClassifiers(List<Pair<IBuilder, Integer>> builders, DataSet baseDataSet) {
        this.ensemble = new ClassifierEnsemble();
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
        ensemble.test(testingData);
    }

    public static void main(String[] args) throws Exception{
        AdaBooster b = new AdaBooster(new TextUI());
        b.start();
    }
}
