/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */
public class Pair<S, T> {
	public S first;
	public T second;

	/**
	 * Creates a pair!
	 * @param first
	 * @param second
	 */
	public Pair(S first, T second) {
		this.first = first;
		this.second = second;
	}
}
