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
    private ClassifierEnsemble ensemble;
    
    public AdaBooster(IUserInterface ui) {
        this.ui = ui;
        this.ensemble = new ClassifierEnsemble();
    }
    public void setup() {
        NOF_NBC = ui.requestInt("How many Naive Bayesian classifiers?");
        NOF_DTC = ui.requestInt("How many Decision Tree classifiers?");
        double trainingTestSplit = ui.requestDouble("Percantage used for training?");
        String dataFile = ui.requestString("Data set: ");
        
        //Read dataset to data
        
        //Split into trainingData and testData
        int trainingSize = (int)(data.length*trainingTestSplit);
        int attrLen = data[0].length;
        double startWeight = 1.0/attrLen;
        trainingData = new double[trainingSize][attrLen+1];
        testingData = new double[data.length-trainingSize][attrLen];
        
        //copy data to training
        for (int i = 0; i < trainingData.length; i++){
            System.arraycopy(data[i], 0, trainingData[i], 0, attrLen);
            trainingData[i][attrLen] = startWeight;
        }
        //copy data to testing
        for (int i = 0; i < testingData.length; i++){
            System.arraycopy(data[i+trainingSize], 0, testingData[i], 0, attrLen);
        }
        buildClassifiers(trainingData);
    }
    public void buildClassifiers(double[][] trainginData) {
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
        b.classifyTestSet();
    }
}
