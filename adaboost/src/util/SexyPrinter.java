package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import classifier.dataset.DataSet;
import classifier.dataset.Instance;

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */

public class SexyPrinter {

	/**
	 * Prints a list rather... sexy!
	 * 
	 * @param list
	 */
	public static void print(List<double[]> list) {
		for (double[] element : list) {
			System.out.println(Arrays.toString(element));
		}
	}

	/**
	 * Prints a list rather... sexy!
	 * 
	 * @param list
	 */
	public static void print(ArrayList<String[]> list) {
		for (Object[] element : list) {
			System.out.println(Arrays.toString(element));
		}
	}

	/**
	 * Prints a list with a list with an array rather... sexy!
	 * 
	 * @param list
	 */
	public static void bigPrint(List<List<double[]>> list) {
		for (List<double[]> classes : list) {
			System.out.println("New class:");
			print(classes);
			System.out.println();
		}
	}

	/**
	 * Prints an array rather... sexy!
	 * 
	 * @param array
	 */
	public static void print(double[][] array) {
		for (double[] element : array) {
			System.out.println(Arrays.toString(element));
		}
	}

	/**
	 * Prints a DataSet rather... sexy!
	 * 
	 * @param ds
	 */
	public static void print(DataSet ds) {
		for (Instance element : ds.getInstances()) {
			System.out.println(element);
		}
	}
}
