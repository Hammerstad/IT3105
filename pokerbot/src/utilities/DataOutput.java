/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is our own little logger. Writes to file, the directory is </br>/logging/(package).(NAME_OF_CLASS).txt
 */
public class DataOutput {

	private static Map<String, DataOutput> multiton = new HashMap<String, DataOutput>();
	private String filename;
	private FileWriter writer;

	private DataOutput(String filename) {
		this.filename = filename;
	}

	public static DataOutput getInstance(@SuppressWarnings("rawtypes") Class cls) {
		String name = cls.getName();
		if (!multiton.containsKey(name)) {
			multiton.put(name, new DataOutput(name));
		}
		return multiton.get(name);
	}

	public static void close() {
		for (DataOutput o : multiton.values()) {
			try {
				if (o != null && o.writer != null) {
					o.writer.flush();
					o.writer.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	private boolean init() {
		if (writer == null) {
			try {
				writer = new FileWriter("logging" + File.separator + filename + ".txt");
				return true;
			} catch (IOException ex) {
				return false;
			}
		}
		return true;
	}

	public int writeLine(String data) {
		return write(data + "\n");
	}

	public int write(String data) {
		if (!init()) {
			return -1;
		}
		try {
			writer.append(data);
			return data.length();
		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
			return -1;
		}
	}
}
