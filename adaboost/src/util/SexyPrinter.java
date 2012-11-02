package util;

import java.util.ArrayList;
import java.util.Arrays;

public class SexyPrinter {

	public static void print(ArrayList<double[]> list){
		for(double[] element : list){
			System.out.println(Arrays.toString(element));
		}
	}
}
