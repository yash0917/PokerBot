package main;

import java.util.ArrayList;
import java.util.List;

public class CommunityCards {

    private List<Card> flop;
    private Card turn;
    private Card river;

    public CommunityCards() {
        flop = new ArrayList<>();
    }

    public void addFlopCard(Card card) {
        if (flop.size() < 3) {
            flop.add(card);
        } else {
            System.out.println("Flop can only have at most 3 cards.");
        }
    }

    public void setTurnCard(Card card) {
        turn = card;
    }

    public void removeTurnCard() {
        turn = null;
    }

    public void setRiverCard(Card card) {
        river = card;
    }

    public void removeRiverCard() {
        river = null;
    }

    public List<Card> getFlopCards() {
        return new ArrayList<>(flop);
    }

    public Card getTurnCard() {
        return turn;
    }

    public Card getRiverCard() {
        return river;
    }

    public List<Card> getAllCards() {
        List<Card> allCards = new ArrayList<>(flop);
        if (turn != null) {
            allCards.add(turn);
        }
        if (river != null) {
            allCards.add(river);
        }
        return allCards;
    }

    @Override
    public String toString() {
        StringBuilder communityCardsString = new StringBuilder("Flop: ");
        for (Card card : flop) {
            communityCardsString.append(card.toString()).append(" ");
        }
        if (turn != null) {
            communityCardsString.append("\nTurn: ").append(turn.toString());
        }
        if (river != null) {
            communityCardsString.append("\nRiver: ").append(river.toString());
        }
        return communityCardsString.toString();
    }
}
