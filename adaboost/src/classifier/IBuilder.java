/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

/**
 *
 * @author Nicklas
 */
public interface IBuilder {
    public Pair<IClassifier,DataSet>  build(DataSet ds);
}
