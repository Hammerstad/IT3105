package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import classifier.DataSet;
import classifier.Instance;

public class DataSetReader {
	DecimalFormat df;
	DecimalFormatSymbols symbols;

	public DataSetReader() {
		df = new DecimalFormat();
		symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(symbols);
	}

	public ArrayList<double[]> read(String filename) {
		ArrayList<double[]> table = new ArrayList<>();
		if (filename.isEmpty()) {
			filename = "yeast";
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./resources/" + filename + ".txt")))) {
			// Read file
			String temp = br.readLine();
			String[] tempToArray;
			double[] instance;
			while (temp != null) {
				tempToArray = temp.split(",");
				instance = new double[tempToArray.length];
				for (int i = 0; i < tempToArray.length; i++) {
					instance[i] = df.parse(tempToArray[i]).doubleValue();
				}
				table.add(instance);
				temp = br.readLine();
			}
		} catch (Exception e) {
			// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		} 
		return table;
	}
	
//	public static void main (String[] args){
//		DataSetReader dr = new DataSetReader();
//		ArrayList<double[]> list = dr.read("");
//		for(double[] element:list){
//			Instance i = new Instance(element);
//			System.out.println(i);
//		}
//		DataSet ds = new DataSet(list);
//		System.out.println(ds);
//		DataSet subset = ds.subset(0, 40);
//		//SexyPrinter.print(list);
//	}
}
