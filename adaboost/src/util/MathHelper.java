package util;

public class MathHelper {

	public static double sum(double[] array) {
		double sum = 0;
		for (int i = 0, size = array.length; i <= size; i++) {
			sum += array[i];
		}
		return sum;
	}
}
