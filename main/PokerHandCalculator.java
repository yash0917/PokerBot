package main;

import java.util.*;

public class PokerHandCalculator {

	private static final int HIGH_CARD = 0;
	private static final int PAIR = 1;
	private static final int TWO_PAIR = 2;
	private static final int THREE_OF_A_KIND = 3;
	private static final int STRAIGHT = 4;
	private static final int FLUSH = 5;
	private static final int FULL_HOUSE = 6;
	private static final int FOUR_OF_A_KIND = 7;
	private static final int STRAIGHT_FLUSH = 8;
	private static final int ROYAL_FLUSH = 9;

	public static int[] calculatePostFlopOdds(List<Hand> hands, CommunityCards communityCards) {
		int[] handsWon = new int[hands.size()];
		List<Card> remainingCards = getRemainingCards(communityCards, hands);
		int totalSimulations = 0;

		for (int turnIndex = 0; turnIndex < remainingCards.size(); turnIndex++) {
			Card turnCard = remainingCards.get(turnIndex);
			communityCards.setTurnCard(turnCard);

			for (int riverIndex = 0; riverIndex < remainingCards.size(); riverIndex++) {
				if (riverIndex == turnIndex) continue; // Skip the same card for river
				Card riverCard = remainingCards.get(riverIndex);
				communityCards.setRiverCard(riverCard);

				int[] winner = calculatePostRiverWinner(hands, communityCards);
				for (int z = 0; z < hands.size(); z++) {
					handsWon[z] += winner[z];
				}
				totalSimulations++;

				communityCards.removeRiverCard();
			}
			communityCards.removeTurnCard();
		}

		// Convert win counts to percentages
		for (int i = 0; i < handsWon.length; i++) {
			handsWon[i] = (handsWon[i] * 100) / totalSimulations;
		}

		return handsWon;
	}

	public static int[] calculatePostTurnOdds(List<Hand> hands, CommunityCards communityCards) {
		int[] handsWon = new int[hands.size()];
		List<Card> remainingCards = getRemainingCards(communityCards, hands);
		int totalSimulations = 0;

		for (Card riverCard : remainingCards) {
			communityCards.setRiverCard(riverCard);

			int[] winner = calculatePostRiverWinner(hands, communityCards);
			for (int i = 0; i < hands.size(); i++) {
				handsWon[i] += winner[i];
			}
			totalSimulations++;

			communityCards.removeRiverCard();
		}

		// Convert win counts to percentages
		for (int i = 0; i < handsWon.length; i++) {
			handsWon[i] = (handsWon[i] * 100) / totalSimulations;
		}

		return handsWon;
	}

	public static int[] calculatePostRiverWinner(List<Hand> hands, CommunityCards communityCards) {
		int bestHandValue = Integer.MIN_VALUE;
		List<Integer> bestPlayers = new ArrayList<>();
		int[] handsWon = new int[hands.size()];

		for (int i = 0; i < hands.size(); i++) {
			Hand hand = hands.get(i);
			int handValue = finalizeWinner(hand, communityCards);
			if (handValue > bestHandValue) {
				bestHandValue = handValue;
				bestPlayers.clear();
				bestPlayers.add(i);
			} else if (handValue == bestHandValue) {
				bestPlayers.add(i);
			}
		}

		for (int playerIndex : bestPlayers) {
			handsWon[playerIndex]++;
		}

		return handsWon;
	}

	public static String winnerToString(int[] wins, List<Hand> hands) {
		StringBuilder sb = new StringBuilder();
		int totalWins = Arrays.stream(wins).sum();
		if (totalWins == 0) {
			return "No conclusive games. Probabilities cannot be calculated.";
		}

		for (int i = 0; i < hands.size(); i++) {
			int playerNumber = i + 1;
			double winPercentage = (double) wins[i] / totalWins * 100;
			sb.append("Player #" + playerNumber + ": ").append(String.format("%.2f", winPercentage)).append("%\n");
		}
		return sb.toString().trim();
	}

	public static int finalizeWinner(Hand hand, CommunityCards communityCards) {
		List<Card> combinedHand = new ArrayList<>(hand.getCards());
		combinedHand.addAll(communityCards.getAllCards());
		return handValue(combinedHand);
	}

	private static int handValue(List<Card> combinedHand) {
		if (PokerHandEvaluator.hasRoyalFlush(combinedHand)) {
			return ROYAL_FLUSH;
		} else if (PokerHandEvaluator.hasStraightFlush(combinedHand)) {
			return STRAIGHT_FLUSH;
		} else if (PokerHandEvaluator.hasFourOfAKind(combinedHand)) {
			return FOUR_OF_A_KIND;
		} else if (PokerHandEvaluator.hasFullHouse(combinedHand)) {
			return FULL_HOUSE;
		} else if (PokerHandEvaluator.hasFlush(combinedHand)) {
			return FLUSH;
		} else if (PokerHandEvaluator.hasStraight(combinedHand)) {
			return STRAIGHT;
		} else if (PokerHandEvaluator.hasThreeOfAKind(combinedHand)) {
			return THREE_OF_A_KIND;
		} else if (PokerHandEvaluator.hasTwoPair(combinedHand)) {
			return TWO_PAIR;
		} else if (PokerHandEvaluator.hasPair(combinedHand)) {
			return PAIR;
		} else {
			return HIGH_CARD;
		}
	}

	private static List<Card> getRemainingCards(CommunityCards communityCards, List<Hand> hands) {
		Set<Card> usedCards = new HashSet<>();
		for (Hand hand : hands) {
			usedCards.addAll(hand.getCards());
		}
		usedCards.addAll(communityCards.getAllCards());

		List<Card> deck = createFullDeck();
		deck.removeIf(usedCards::contains);
		return deck;
	}

	private static List<Card> createFullDeck() {
		List<Card> deck = new ArrayList<>();
		for (Card.Suit suit : Card.Suit.values()) {
			for (Card.Rank rank : Card.Rank.values()) {
				deck.add(new Card(rank, suit));
			}
		}
		return deck;
	}
}
