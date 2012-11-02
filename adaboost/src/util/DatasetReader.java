package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class DatasetReader {
	DecimalFormat df;
	DecimalFormatSymbols symbols;

	public DatasetReader() {
		df = new DecimalFormat();
		symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(symbols);
	}

	public ArrayList<double[]> read(String filename) {
		ArrayList<double[]> table = new ArrayList<>();
		if (filename.isEmpty()) {
			filename = "yeast.txt";
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./resources/" + filename)))) {
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
	
	public static void main (String[] args){
		DatasetReader dr = new DatasetReader();
		ArrayList<double[]> list = dr.read("");
		SexyPrinter.print(list);
	}
}
