package util;

public class MathHelper {

	public static double sum(double[] array) {
		double sum = 0;
		int size = array.length;
		for (int i = 0; i < size; i++) {
			sum += array[i];
		}
		return sum;
	}
}
