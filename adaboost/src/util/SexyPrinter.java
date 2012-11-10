package util;

import java.util.Arrays;
import java.util.List;

public class SexyPrinter {

	public static void print(List<double[]> list){
		for(double[] element : list){
			System.out.println(Arrays.toString(element));
		}
	}
	
	public static void bigPrint(List<List<double[]>> list){
		for(List<double[]> classes : list){
			System.out.println("New class:");
			print(classes);
			System.out.println();
		}
	}
}
