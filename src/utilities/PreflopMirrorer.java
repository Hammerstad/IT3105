package utilities;

/**
 * Class to make sure the preflop table is mirrored around the diagonal.
 */
public class PreflopMirrorer {

	/**
	 * Basic constructor for preflop mirrorer.
	 */
	public PreflopMirrorer() {

	}
	
	/**
	 * Mirrors a 4 dimensional double matrix around the diagonal
	 * @param inn
	 * @return inn - mirrored
	 */
	public double[][][][] mirror(double[][][][] inn) {
		for (int i = 0; i < 2; i++) {
			for (int players = 2; players < 10; players++) {
				for (int a = 0; a < 13; a++) {
					for (int b = 0; b <= a; b++) {
						inn[i][players - 2][b][a] = inn[i][players - 2][a][b];
					}
				}
			}
		}
		return inn;
	}
}
