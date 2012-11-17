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

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */
public class DataSetConverter {
	/**
	 * The scanner.
	 */
	private Scanner sc;

	/**
	 * Whether or not to automatically convert the DataSet.
	 */
	private boolean automatic;

	/**
	 * Default constructor for DataSetConverter.
	 * 
	 * @param automatic
	 *            - whether or not to automatically convert the DataSet.
	 */
	public DataSetConverter(boolean automatic) {
		this.automatic = automatic;
	}

	/**
	 * Tries to read a specified file (from scanner input).
	 */
	public void tryToRead() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Pick which dataset we will convert today:\n" + resources());
		String dataset = sc.next();
		ArrayList<String[]> unfinishedDataSet = read(dataset);
		processUnfinishedDataSet(unfinishedDataSet, dataset);
		sc.close();
	}

	/**
	 * Reads a file and creates an ArrayList with String arrays in it. Throws exceptions in your face if you read a file which doesn't exist.
	 * 
	 * @param filename
	 *            - file to read
	 * @return ArrayList<String[]>
	 */
	private ArrayList<String[]> read(String filename) {
		ArrayList<String[]> table = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./resources/" + filename + ".data")))) {
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

	/**
	 * Processes an unfinished DataSet. 
	 * @param unfinishedDataSet - ArrayList<String[]>
	 * @param dataset - name of the DataSet.
	 */
	private void processUnfinishedDataSet(ArrayList<String[]> unfinishedDataSet, String dataset) {
		sc = new Scanner(System.in);

		// Process each column in the data-set
		for (int i = 0; i < unfinishedDataSet.get(0).length; i++) {
			processColumn(unfinishedDataSet, i);
		}
		sc.close();
		writeToDisk(unfinishedDataSet, dataset);
	}

	/**
	 * Process a column in an unfinished DataSet.
	 * @param unfinishedDataSet - ArrayList<String[]>
	 * @param columnNumber - which column.
	 */
	private void processColumn(ArrayList<String[]> unfinishedDataSet, int columnNumber) {
		Set<String> uniqueValues = new HashSet<>();
		for (String[] element : unfinishedDataSet) {
			uniqueValues.add(element[columnNumber]);
		}
		String answer = "auto";
		if (!automatic) {
			System.out.println("Distinct values: " + uniqueValues.toString());
			System.out.println("Do you wish to change them? (Type \"auto\" if you want them auto assigned to integers.");
			answer = sc.next();
		}
		Object[] uniqueValuesToList = uniqueValues.toArray();
		if (answer.equals("auto") || answer.equals("Auto")) {
			String[] newValues = new String[uniqueValues.size()];
			for (int i = 0; i < uniqueValues.size(); i++) {
				newValues[i] = String.valueOf(i);
			}
			for (int i = 0; i < uniqueValues.size(); i++) {
				for (String[] element : unfinishedDataSet) {
					if (element[columnNumber].equals(uniqueValuesToList[i])) {
						element[columnNumber] = newValues[i];
					}
				}
			}
		} else if (answer.equals("yes") || answer.equals("y") || answer.equals("Yes") || answer.equals("Y")) {
			System.out.println("Type new values(integers) separated by commas, for instance \"1,2,3,4\" if the previous output was [a, b, c, d].");
			answer = sc.next();
			String[] newValues = answer.split(",");
			for (int i = 0; i < uniqueValues.size(); i++) {
				for (String[] element : unfinishedDataSet) {
					if (element[columnNumber].equals((String) uniqueValuesToList[i])) {
						element[columnNumber] = newValues[i];
					}
				}
			}
		}
		if (!automatic)
			System.out.println("Proceding to next attribute.\n");
	}

	/**
	 * Writes a finished DataSet (ArrayList<String[]>) to disk. In the /resources/ folder.
	 * @param finishedDataSet - the DataSet
	 * @param filename - filename of your choice
	 */
	private void writeToDisk(ArrayList<String[]> finishedDataSet, String filename) {
		try {
			// Create file
			FileOutputStream fos = new FileOutputStream("./resources/" + filename + ".txt");
			DataOutputStream dos = new DataOutputStream(fos);
			for (String[] element : finishedDataSet) {
				for (int i = 0; i < element.length; i++) {
					// System.out.print(element[i]);
					dos.write(element[i].getBytes());
					if (i != element.length - 1) {
						// System.out.print(',');
						dos.write(",".getBytes());
					}

				}
				// System.out.println();
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

	/**
	 * Prints a string with all the "*.data" files you can choose to convert.
	 * @return String with filenames
	 */
	private static String resources() {
		StringBuilder sb = new StringBuilder();
		File[] filesAvailable = new File("./resources/").listFiles();
		for (File file : filesAvailable) {
			String filename = file.getName();
			if (!filename.endsWith(".data")) {
				continue;
			}
			sb.append(filename.substring(0, filename.length() - 5)).append('\t');
		}
		return sb.toString();
	}

	public void gottaReadThemAll(){
		String[] files = resources().split("\t");
		System.out.println("Processing "+files.length+" files.");
		for(int i = 0; i < files.length; i++){
			ArrayList<String[]> unfinishedDataSet = read(files[i]);
			processUnfinishedDataSet(unfinishedDataSet, files[i]);
			System.out.println("Finished processing "+files[i]+". "+(files.length-i-1)+" files remaining.");
		}
		System.out.println("Processing done.");
	}
	
	/**
	 * Converts stuff
	 * @param args - meh
	 */
	public static void main(String[] args) {
		DataSetConverter dsc = new DataSetConverter(true);
		dsc.gottaReadThemAll();
	}
}
