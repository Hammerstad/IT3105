package util;

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */
public class MathHelper {

	/**
	 * Sums all elements in an array with double elements.
	 * @param array
	 * @return sum
	 */
	public static double sum(double[] array) {
		double sum = 0;
		int size = array.length;
		for (int i = 0; i < size; i++) {
			sum += array[i];
		}
		return sum;
	}
	
	/**
	 * Multiplies all elements in an array with double elements..
	 * @param array
	 * @return product.
	 */
	public static double product(double[] array) {
		double product = 1;
		int size = array.length;
		for (int i = 0; i < size; i++) {
			product *= array[i];
		}
		return product;
	}
}
