package utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import poker.Card;
import poker.Suit;

public class CardUtilities {

    public static int[] classification(Card[] hand) {
        Card[] copy = Arrays.copyOf(hand, hand.length);
        int[] classification;
        if ((classification = isStraightFlush(copy)) != null) {
        } else if ((classification = isFourOfAKind(copy)) != null) {
        } else if ((classification = isFullHouse(copy)) != null) {
        } else if ((classification = isFlush(copy)) != null) {
        } else if ((classification = isStraight(copy)) != null) {
        } else if ((classification = isThreeOfAKind(copy)) != null) {
        } else if ((classification = isTwoPairs(copy)) != null) {
        } else if ((classification = isPair(copy)) != null) {
        } else if ((classification = isHighCard(copy)) != null) {
        } else {
            classification = new int[6];
        }
        return paddToSix(classification);
    }

    private static int[] paddToSix(int[] unpadded) {
        if (unpadded.length == 6) {
            return unpadded;
        } else {
            int[] o = new int[6];
            System.arraycopy(unpadded, 0, o, 0, unpadded.length);
            return o;
        }
    }

    private static int[] isStraightFlush(Card[] hand) {
        if (hand.length < 5) {
            return null;
        }
        Arrays.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card arg0, Card arg1) {
                return arg0.value - arg1.value;
            }
        });
        for (int i = hand.length; i >= 5; i--) {
            Card[] temp = Arrays.copyOfRange(hand, i - 5, i);
            if (isStraight(temp) != null && isFlush(temp) != null) {
                //Found straight flush
                Arrays.sort(temp);
                return new int[]{9, temp[0].value};
            }
        }
        return null;
    }

    private static int[] isStraight(Card[] hand) {
        if (hand.length < 5) {
            return null;
        }
        Arrays.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card arg0, Card arg1) {
                return arg1.value - arg0.value;
            }
        });
        int highVal = 0;
        for (int i = hand.length; i >= 5; i--) {
            Card[] temp = Arrays.copyOfRange(hand, i - 5, i);
            for (int j = 0; j < temp.length - 1; j++) {
                if (temp[j].value != temp[j + 1].value + 1) {
                    return null;
                }
            }
            highVal = temp[0].value;
        }
        return new int[]{5, highVal};

    }

    private static int[] isFlush(Card[] hand) {
        if (hand.length < 5) {
            return null;
        }
        int[] numberOfDifferentSuits = new int[4];
        for (Card element : hand) {
            numberOfDifferentSuits[element.suit.getSuitValue()]++;
        }
        for (int elementNO = 0; elementNO < 4; elementNO++) {
//        for (int element : numberOfDifferentSuits) {
            int element = numberOfDifferentSuits[elementNO];
            if (element >= 5) {
                //Found a flush
                int[] output = new int[6];
                int[] pickFive = new int[element];
                int pickFiveInd = 0;
                output[0] = 6;

                Arrays.sort(hand);
                for (Card c : hand) {
                    if (c.suit.getSuitValue() == elementNO) {
                        pickFive[pickFiveInd++] = c.value;
                    }
                }
                System.arraycopy(pickFive, 0, output, 1, 5);

                return output;
            }
//        }
        }
        return null;
    }

    private static int[] isFullHouse(Card[] hand) {
        if (hand.length < 5) {
            return null;
        }
        int[] numberOfDifferentValues = new int[14];
        for (Card element : hand) {
            numberOfDifferentValues[element.value - 1]++;
        }
        int foundThreeOfAKind = 0;
        int foundAPair = 0;
        for (int elementNo = 0; elementNo < 14; elementNo++) {
            int element = numberOfDifferentValues[elementNo];
//        for (int element : numberOfDifferentValues) {
            if (element == 3 & foundThreeOfAKind <= 0) {
                foundThreeOfAKind = elementNo + 1;
                continue;
            }
            if (element >= 2) {
                foundAPair = elementNo + 1;
            }
        }
        if (foundThreeOfAKind * foundAPair > 0) {
            return new int[]{7, foundThreeOfAKind, foundAPair};
        } else {
            return null;
        }
    }

    private static int[] isFourOfAKind(Card[] hand) {
        if (hand.length < 4) {
            return null;
        }
        int[] numberOfDifferentValues = new int[14];
        for (Card element : hand) {
            numberOfDifferentValues[element.value - 1]++;
        }
        for (int elementNo = 0; elementNo < numberOfDifferentValues.length; elementNo++) {
            int element = numberOfDifferentValues[elementNo];
//        for (int element : numberOfDifferentValues) {
            if (element >= 4) {
                //Found four of kind => output == [8, fourOfAKindValue, RestCard]
                int[] o = new int[3];
                o[0] = 8;
                o[1] = elementNo + 1;
                Arrays.sort(hand);
                for (Card c : hand) {
                    if (c.value != elementNo + 1) {
                        o[2] = c.value;
                        break;
                    }
                }
                return o;
            }
        }
        return null;
    }

    private static int[] isThreeOfAKind(Card[] hand) {
        if (hand.length < 3) {
            return null;
        }
        int[] numberOfDifferentValues = new int[14];
        for (Card element : hand) {
            numberOfDifferentValues[element.value - 1]++;
        }
        for (int elementNo = 0; elementNo < numberOfDifferentValues.length; elementNo++) {
            int element = numberOfDifferentValues[elementNo];
            if (element >= 3) {
                int[] o = new int[4];
                o[0] = 4;
                o[1] = elementNo + 1;
                Arrays.sort(hand);
                int highs = 2;
                for (Card c : hand) {
                    if (c.value != elementNo + 1) {
                        o[highs++] = c.value;
                        if (highs == 4) {
                            break;
                        }
                    }
                }
                return o;
            }
        }
        return null;
    }

    private static int[] isTwoPairs(Card[] hand) {
        if (hand.length < 4) {
            return null;
        }
        int[] numberOfDifferentValues = new int[14];
        for (Card element : hand) {
            numberOfDifferentValues[element.value - 1]++;
        }
        int numberOfPairs = 0;
        int[] pairs = new int[3];
        for (int elementNo = 0; elementNo < numberOfDifferentValues.length; elementNo++) {
//        for (int element : numberOfDifferentValues) {
            int element = numberOfDifferentValues[elementNo];
            if (element >= 2) {
                pairs[numberOfPairs++] = elementNo + 1;
            }
        }
        Arrays.sort(pairs);
        //Reversing list
        int l = pairs.length;
        for (int i = 0; i < l / 2; i++) {
            pairs[i] ^= pairs[l - 1 - i];
            pairs[l - 1 - i] ^= pairs[i];
            pairs[i] ^= pairs[l - 1 - i];
        }

        if (numberOfPairs >= 2) {

            int[] o = new int[4];
            o[0] = 3;
//            Arrays.sort(pairs);
            o[1] = pairs[0];
            o[2] = pairs[1];

            Arrays.sort(hand);
            Arrays.sort(pairs);
            for (Card c : hand) {
                if (Arrays.binarySearch(pairs, c.value) < 0) {
                    o[3] = c.value;
                    break;
                }
            }
            return o;
        }
        return null;
    }

    private static int[] isPair(Card[] hand) {
        int[] numberOfDifferentValues = new int[14];
        for (Card element : hand) {
            numberOfDifferentValues[element.value - 1]++;
        }
        for (int elementNo = 0; elementNo < numberOfDifferentValues.length; elementNo++) {
            int element = numberOfDifferentValues[elementNo];
            if (element >= 2) {
                int[] o = new int[5];
                o[0] = 2;
                o[1] = elementNo + 1;

                Arrays.sort(hand);
                int highsInd = 0;
                for (Card c : hand) {
                    if (c.value != elementNo + 1) {
                        o[2 + highsInd++] = c.value;
                        if (highsInd == 3) {
                            break;
                        }
                    }
                }
                return o;
            }
        }
        return null;
    }

    private static int[] isHighCard(Card[] hand) {
        int l = (hand.length >= 5) ? 5 : hand.length;
        int[] o = new int[l + 1];
        o[0] = 1;
        Arrays.sort(hand);
        for (int i = 0; i < l; i++) {
            o[i+1] = hand[i].value;
        }
        return o;
    }
}
