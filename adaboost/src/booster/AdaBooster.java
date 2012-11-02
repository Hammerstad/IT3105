/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package booster;

import classifier.DataSet;
import classifier.IBuilder;
import classifier.IClassifier;
import java.util.LinkedList;
import java.util.List;
import ui.IUserInterface;
import util.DataSetReader;
import util.Pair;

/**
 *
 * @author Nicklas
 */
public class AdaBooster implements IBooster {

    public static IBuilder[] availableClassifiers = new IBuilder[]{};
    private final IUserInterface ui;
    private DataSet data;
    private DataSet trainingData;
    private DataSet testingData;
    private ClassifierEnsemble ensemble;

    public AdaBooster(IUserInterface ui) {
        this.ui = ui;
        this.ensemble = new ClassifierEnsemble();
    }

    public void start() {
        String dataFile = ui.requestString("Data set: ");
        double trainingTestSplit = ui.requestDouble("Percantage used for training?");

        int classifierIndex;
        List<Pair<IBuilder, Integer>> builders = new LinkedList<>();

        while ((classifierIndex = ui.requestChoice("Select classifier, end with -1", availableClassifiers)) != -1) {
            IBuilder ib = availableClassifiers[classifierIndex];
            int nof = ui.requestInt("How many would you like?");
            builders.add(new Pair<>(ib, nof));
        }

        //Read dataset to data
        DataSetReader dsr = new DataSetReader();
        data = new DataSet(dsr.read(dataFile));
        
        
        //Split into trainingData and testData
        int trainingSize = (int) (data.length() * trainingTestSplit);
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

    public static void main(String[] args) {
        AdaBooster b = new AdaBooster(null);
        b.start();
    }
}
