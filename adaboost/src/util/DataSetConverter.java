package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DataSetConverter {
	private Scanner sc;
	private boolean automatic;

	public DataSetConverter(boolean automatic) {
		this.automatic = automatic;
	}

	private ArrayList<String[]> read(String filename) {
		ArrayList<String[]> table = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("./resources/" + filename + ".data")))) {
			// Read file
			String temp = br.readLine();
			while (temp != null) {
				table.add(temp.split(","));
				temp = br.readLine();
			}
		} catch (Exception e) {
			// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return table;
	}

	private void tryToRead() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Pick which dataset we will convert today:\n"
				+ resources());
		String dataset = sc.next();
		ArrayList<String[]> unfinishedDataSet = read(dataset);
		processUnfinishedDataSet(unfinishedDataSet, dataset);
		sc.close();
	}

	private void processUnfinishedDataSet(
			ArrayList<String[]> unfinishedDataSet, String dataset) {
		sc = new Scanner(System.in);

		// Process each column in the data-set
		for (int i = 0; i < unfinishedDataSet.get(0).length; i++) {
			processColumn(unfinishedDataSet, i);
		}
		sc.close();
		writeToDisk(unfinishedDataSet, dataset);
	}

	private void processColumn(ArrayList<String[]> unfinishedDataSet,
			int columnNumber) {
		Set<String> uniqueValues = new HashSet<>();
		for (String[] element : unfinishedDataSet) {
			uniqueValues.add(element[columnNumber]);
		}
		String answer = "auto";
		if (!automatic) {
			System.out.println("Distinct values: " + uniqueValues.toString());
			System.out
					.println("Do you wish to change them? (Type \"auto\" if you want them auto assigned to integers.");
			answer = sc.next();
		}
		Object[] uniqueValuesToList = uniqueValues.toArray();
		System.out.println(Arrays.toString(uniqueValuesToList));
		if (answer.equals("auto") || answer.equals("Auto")) {
			String[] newValues = new String[uniqueValues.size()];
			for (int i = 0; i < uniqueValues.size(); i++) {
				newValues[i] = String.valueOf(i);
			}
			System.out.println(Arrays.toString(newValues));			
			for (int i = 0; i < uniqueValues.size(); i++) {
				for (String[] element : unfinishedDataSet) {
					if (element[columnNumber]
							.equals(uniqueValuesToList[i])) {
						element[columnNumber] = newValues[i];
					}
				}
			}
		} else if (answer.equals("yes") || answer.equals("y")
				|| answer.equals("Yes") || answer.equals("Y")) {
			System.out
					.println("Type new values(integers) separated by commas, for instance \"1,2,3,4\" if the previous output was [a, b, c, d].");
			answer = sc.next();
			String[] newValues = answer.split(",");
			for (int i = 0; i < uniqueValues.size(); i++) {
				for (String[] element : unfinishedDataSet) {
					if (element[columnNumber]
							.equals((String) uniqueValuesToList[i])) {
						element[columnNumber] = newValues[i];
					}
				}
			}
		}
		if (!automatic)
			System.out.println("Proceding to next attribute.\n");
	}

	private void writeToDisk(ArrayList<String[]> finishedDataSet, String dataset) {
		try {
			// Create file
			FileOutputStream fos = new FileOutputStream("./resources/"
					+ dataset + ".txt");
			DataOutputStream dos = new DataOutputStream(fos);
			for (String[] element : finishedDataSet) {
				for (int i = 0; i < element.length; i++) {
					//System.out.print(element[i]);
					dos.write(element[i].getBytes());
					if (i != element.length - 1) {
						//System.out.print(',');
						dos.write(",".getBytes());
					}

				}
				//System.out.println();
				fos.write('\n');
			}
			// Close the output stream
			dos.close();
			fos.close();
		} catch (Exception e) {
			// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static String resources() {
		StringBuilder sb = new StringBuilder();
		File[] filesAvailable = new File("./resources/").listFiles();
		for (File file : filesAvailable) {
			String filename = file.getName();
			if (!filename.endsWith(".data")) {
				continue;
			}
			sb.append(filename.substring(0, filename.length() - 5))
					.append('\t');
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		/*
		 * System.out.print(
		 * "If you want to automaticly convert the dataset, type 'a' (without the quotationmarks)"
		 * ); Scanner tull = new Scanner(System.in); String auto = tull.next();
		 * tull.close(); boolean automatic = false; if(auto.equals("a") ||
		 * auto.equals("A")){ automatic = true; }
		 */
		DataSetConverter dsc = new DataSetConverter(true);
		dsc.tryToRead();
	}
}
