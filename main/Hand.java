package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void sort() {
        Collections.sort(cards);
    }

    @Override
    public String toString() {
        StringBuilder handString = new StringBuilder();
        for (Card card : cards) {
            handString.append(card.toString()).append("\n");
        }
        return handString.toString();
    }

    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public Card getFirstCard() {
        return cards.isEmpty() ? null : cards.get(0);
    }

    public Card getSecondCard() {
        return cards.size() > 1 ? cards.get(1) : null;
    }
}
