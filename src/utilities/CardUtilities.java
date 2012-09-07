package utilities;

import java.util.Arrays;
import java.util.Comparator;

import poker.Card;

public class CardUtilities {
	public static boolean isStraightFlush(Card[] hand) {
		if (hand.length < 5)
			return false;
		Arrays.sort(hand, new Comparator<Card>() {
			@Override
			public int compare(Card arg0, Card arg1) {
				return arg0.value - arg1.value;
			}
		});
		for (int i = hand.length; i >= 5; i--) {
			Card[] temp = Arrays.copyOfRange(hand, i - 5, i);
			if (isStraight(temp) && isFlush(temp)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isStraight(Card[] hand) {
		if (hand.length < 5)
			return false;
		Arrays.sort(hand, new Comparator<Card>() {
			@Override
			public int compare(Card arg0, Card arg1) {
				return arg0.value - arg1.value;
			}
		});
		for (int i = hand.length; i >= 5; i--) {
			Card[] temp = Arrays.copyOfRange(hand, i - 5, i);
			for (int j = 0; j < temp.length - 1; j++) {
				if (temp[j].value != temp[j + 1].value - 1) {
					return false;
				}
			}
		}
		return true;

	}

	public static boolean isFlush(Card[] hand) {
		if (hand.length < 5)
			return false;
		int[] numberOfDifferentSuits = new int[4];
		for (Card element : hand) {
			numberOfDifferentSuits[element.suit.getSuitValue()]++;
		}
		for (int element : numberOfDifferentSuits) {
			if (element >= 5)
				return true;
		}
		return false;
	}

	public static boolean isFullHouse(Card[] hand) {
		if (hand.length < 5)
			return false;
		int[] numberOfDifferentValues = new int[13];
		for (Card element : hand) {
			numberOfDifferentValues[element.value - 1]++;
		}
		boolean foundThreeOfAKind = false;
		boolean foundAPair = false;
		for (int element : numberOfDifferentValues) {
			if (element == 3 & !foundThreeOfAKind) {
				foundThreeOfAKind = true;
				continue;
			}
			if (element >= 2) {
				foundAPair = true;
			}
		}
		return foundAPair && foundThreeOfAKind;
	}

	public static boolean isFourOfAKind(Card[] hand) {
		if (hand.length < 4)
			return false;
		int[] numberOfDifferentValues = new int[13];
		for (Card element : hand) {
			numberOfDifferentValues[element.value - 1]++;
		}
		for (int element : numberOfDifferentValues) {
			if (element >= 4)
				return true;
		}
		return false;
	}

	public static boolean isThreeOfAKind(Card[] hand) {
		if (hand.length < 3)
			return false;
		int[] numberOfDifferentValues = new int[13];
		for (Card element : hand) {
			numberOfDifferentValues[element.value - 1]++;
		}
		for (int element : numberOfDifferentValues) {
			if (element >= 3)
				return true;
		}
		return false;
	}

	public static boolean isTwoPairs(Card[] hand) {
		if (hand.length < 4)
			return false;
		int[] numberOfDifferentValues = new int[13];
		for (Card element : hand) {
			numberOfDifferentValues[element.value - 1]++;
		}
		int numberOfPairs = 0;
		for (int element : numberOfDifferentValues) {
			if (element >= 2) {
				numberOfPairs++;
			}
		}
		return numberOfPairs >= 2;
	}

	public static boolean isPair(Card[] hand) {
		int[] numberOfDifferentValues = new int[13];
		for (Card element : hand) {
			numberOfDifferentValues[element.value - 1]++;
		}
		for (int element : numberOfDifferentValues) {
			if (element >= 2) {
				return true;
			}
		}
		return false;
	}
}
