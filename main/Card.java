package main;

public class Card implements Comparable<Card> {

    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public int compareTo(Card o) {
        int rankCompare = Integer.compare(this.rank.getValue(), o.rank.getValue());
        if (rankCompare == 0) {
            return this.suit.compareTo(o.suit);
        }
        return rankCompare;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
