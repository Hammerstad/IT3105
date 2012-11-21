package util;

import java.lang.reflect.Array;

public class ShallowCopy {

	public static <T> T[] copy(T[] array, Class<? extends T> cls) {
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) Array.newInstance(cls, array.length);
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}

	public static int[] copy(int[] array) {
		int[] newArray = new int[array.length];
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}
}
