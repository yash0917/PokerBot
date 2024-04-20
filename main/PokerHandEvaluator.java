package main;

import java.util.*;

public class PokerHandEvaluator {

    public static boolean hasRoyalFlush(List<Card> combinedHand) {
        Map<Card.Suit, List<Card>> cardsBySuit = groupCardsBySuit(combinedHand);
        Set<Card.Rank> requiredRanks = EnumSet.of(Card.Rank.TEN, Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING, Card.Rank.ACE);

        for (List<Card> cards : cardsBySuit.values()) {
            if (cards.size() >= 5) {
                Set<Card.Rank> presentRanks = new HashSet<>();
                for (Card card : cards) {
                    presentRanks.add(card.getRank());
                }
                if (presentRanks.containsAll(requiredRanks)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasStraightFlush(List<Card> combinedHand) {
        Map<Card.Suit, List<Card>> cardsBySuit = groupCardsBySuit(combinedHand);
        for (List<Card> cards : cardsBySuit.values()) {
            if (cards.size() >= 5 && hasConsecutiveRanks(cards)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasFourOfAKind(List<Card> combinedHand) {
        Map<Card.Rank, Integer> rankCounts = new HashMap<>();
        for (Card card : combinedHand) {
            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
        }

        return rankCounts.values().stream().anyMatch(count -> count >= 4);
    }

    public static boolean hasFullHouse(List<Card> combinedHand) {
        Map<Card.Rank, Integer> rankCounts = new HashMap<>();
        for (Card card : combinedHand) {
            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
        }

        int triples = 0, pairs = 0;
        for (int count : rankCounts.values()) {
            if (count >= 3) {
                triples++;
            } else if (count == 2) {
                pairs++;
            }
        }

        return triples >= 1 && (triples + pairs >= 2);
    }

    public static boolean hasFlush(List<Card> combinedHand) {
        Map<Card.Suit, List<Card>> cardsBySuit = groupCardsBySuit(combinedHand);
        return cardsBySuit.values().stream().anyMatch(cards -> cards.size() >= 5);
    }

    public static boolean hasStraight(List<Card> combinedHand) {
        Map<Integer, Integer> rankCounts = new HashMap<>();
        combinedHand.forEach(card -> rankCounts.put(card.getRank().getValue(), rankCounts.getOrDefault(card.getRank().getValue(), 0) + 1));

        List<Integer> sortedRanks = new ArrayList<>(rankCounts.keySet());
        Collections.sort(sortedRanks);
        if (sortedRanks.contains(14)) { // Add Ace as low (value 1) to support Ace low straights
            sortedRanks.add(1);
        }

        int consecutiveCount = 0;
        int prevRank = -1;
        for (int rank : sortedRanks) {
            if (rank == prevRank + 1) {
                consecutiveCount++;
                if (consecutiveCount == 5) {
                    return true;
                }
            } else {
                consecutiveCount = 1;
            }
            prevRank = rank;
        }
        return false;
    }

    public static boolean hasThreeOfAKind(List<Card> combinedHand) {
        Map<Card.Rank, Integer> rankCounts = new HashMap<>();
        for (Card card : combinedHand) {
            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCounts.values().stream().anyMatch(count -> count >= 3);
    }

    public static boolean hasTwoPair(List<Card> combinedHand) {
        Map<Card.Rank, Integer> rankCounts = new HashMap<>();
        for (Card card : combinedHand) {
            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCounts.values().stream().filter(count -> count == 2).count() >= 2;
    }

    public static boolean hasPair(List<Card> combinedHand) {
        Set<Card.Rank> seenRanks = new HashSet<>();
        for (Card card : combinedHand) {
            if (!seenRanks.add(card.getRank())) {
                return true; // Found a pair
            }
        }
        return false;
    }

    // Helper method to group cards by suit
    private static Map<Card.Suit, List<Card>> groupCardsBySuit(List<Card> combinedHand) {
        Map<Card.Suit, List<Card>> cardsBySuit = new HashMap<>();
        for (Card card : combinedHand) {
            cardsBySuit.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }
        return cardsBySuit;
    }

    // Helper method to check if a list of cards has consecutive ranks
    private static boolean hasConsecutiveRanks(List<Card> cards) {
        Collections.sort(cards, Comparator.comparingInt(card -> card.getRank().getValue()));

        int straightCount = 1;
        int prevRank = cards.get(0).getRank().getValue();

        for (int i = 1; i < cards.size(); i++) {
            int currentRank = cards.get(i).getRank().getValue();
            if (currentRank == prevRank + 1) {
                straightCount++;
                if (straightCount == 5) {
                    return true;
                }
            } else {
                straightCount = 1;
            }
            prevRank = currentRank;
        }

        return false;
    }
}
