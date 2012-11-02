/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package booster;

import classifier.IClassifier;
import sun.security.provider.certpath.BuildStep;
import ui.IUserInterface;

/**
 *
 * @author Nicklas
 */
public class AdaBooster implements IBooster {
    private final IUserInterface ui;
    private double[][] data;
    private double[][] trainingData;
    private double[][] testingData;
    private int NOF_NBC;
    private int NOF_DTC;
    private double trainingTestSplit;
    private ClassifierEnsemble ensemble;
    
    public AdaBooster(IUserInterface ui) {
        this.ui = ui;
        this.ensemble = new ClassifierEnsemble();
    }
    public void setup() {
        NOF_NBC = ui.requestInt("How many Naive Bayesian classifiers?");
        NOF_DTC = ui.requestInt("How many Decision Tree classifiers?");
        trainingTestSplit = ui.requestDouble("Percantage used for training?");
        String dataFile = ui.requestString("Data set: ");
        
        //Read dataset to data
        
        //Split into trainingData and testData
    }
    public void buildClassifiers() {
        for (int i = 0;i < NOF_NBC; i++){
            //Build bayesian classifiers
        }
        for (int i = 0;i < NOF_DTC; i++){
            //Build decisiton classifiers
        }
    }
    public void classifyTestSet() {
        ensemble.test(testingData);
    }
    public static void main(String[] args) {
        AdaBooster b = new AdaBooster(null);
        b.setup();
        b.buildClassifiers();
        b.classifyTestSet();
    }
}
