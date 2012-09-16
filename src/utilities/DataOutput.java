/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicklas
 */
public class DataOutput {

    private static Map<String, DataOutput> multiton;
    private String filename;
    private FileWriter writer;

    private DataOutput(String filename) {
        this.filename = filename;
    }

    private boolean init() {
        if (writer == null) {
            try {
                writer = new FileWriter(filename + ".txt");
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }

    public int writeLine(String data) {
        return write(data+"\n");
    }

    public int write(String data) {
        if (!init()) {
            return -1;
        }
        try {
            writer.append(data);
            return data.length();
        }catch(Exception e){
            return -1;
        }
    }
}
