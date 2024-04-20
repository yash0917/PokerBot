package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        CommunityCards communityCards = new CommunityCards();

        // Prompt the user for the number of players
        int numPlayers = promptForIntegerInput(scanner, "Enter the number of players: ");

        // Prompt the user for each player's hand
        List<Hand> playerHands = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            Hand hand = new Hand();
            System.out.println("Enter cards for Player " + (i + 1) + "'s hand (e.g., 2H for 2 of hearts): ");
            for (int j = 0; j < 2; j++) {
                hand.addCard(promptForCardInput(scanner, "Enter card " + (j + 1) + ": "));
            }
            playerHands.add(hand);
        }

        // Prompt the user for the flop cards
        System.out.println("Enter the flop cards (e.g., AH for ace of hearts): ");
        for (int i = 0; i < 3; i++) {
            communityCards.addFlopCard(promptForCardInput(scanner, "Enter flop card " + (i + 1) + ": "));
        }
        System.out.println("Post-flop winning probability: " + "\n" + PokerHandCalculator.winnerToString(PokerHandCalculator.calculatePostFlopOdds(playerHands, communityCards), playerHands));
        
        // Prompt the user for the turn card
        System.out.println("Enter the turn card (e.g., AH for ace of hearts): ");
        communityCards.setTurnCard(promptForCardInput(scanner, "Enter turn card: "));
        
        System.out.println("Post-turn winning probability: " + "\n" + PokerHandCalculator.winnerToString(PokerHandCalculator.calculatePostTurnOdds(playerHands, communityCards), playerHands));
        
        // Prompt the user for the river card
        System.out.println("Enter the river card (e.g., AH for ace of hearts): ");
        communityCards.setRiverCard(promptForCardInput(scanner, "Enter river card: "));
        
        System.out.println("Post-river winning probability: " + "\n" + PokerHandCalculator.winnerToString(PokerHandCalculator.calculatePostRiverWinner(playerHands, communityCards), playerHands));
        
        // Close the scanner
        scanner.close();
    }

    private static int promptForIntegerInput(Scanner scanner, String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer.");
            scanner.next(); // Consume invalid input
        }
        return scanner.nextInt();
    }

    private static Card promptForCardInput(Scanner scanner, String message) {
        System.out.print(message);
        while (true) {
            String input = scanner.next().toUpperCase(); // Convert input to uppercase
            if (input.length() < 2) {
                System.out.println("Invalid card format. Please use the format like '2H' for 2 of hearts.");
                continue;
            }
            String rankStr = input.substring(0, input.length() - 1);
            char suitChar = input.charAt(input.length() - 1);

            try {
                Card.Rank rank = parseRank(rankStr);
                Card.Suit suit = parseSuit(suitChar);
                return new Card(rank, suit);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid rank or suit. Please enter a valid card.");
            }
        }
    }

    private static Card.Rank parseRank(String rankStr) {
        switch (rankStr) {
            case "A":
                return Card.Rank.ACE;
            case "K":
                return Card.Rank.KING;
            case "Q":
                return Card.Rank.QUEEN;
            case "J":
                return Card.Rank.JACK;
            case "10":
                return Card.Rank.TEN;
            case "9":
                return Card.Rank.NINE;
            case "8":
                return Card.Rank.EIGHT;
            case "7":
                return Card.Rank.SEVEN;
            case "6":
                return Card.Rank.SIX;
            case "5":
                return Card.Rank.FIVE;
            case "4":
                return Card.Rank.FOUR;
            case "3":
                return Card.Rank.THREE;
            case "2":
                return Card.Rank.TWO;
            default:
                throw new IllegalArgumentException("Invalid rank: " + rankStr);
        }
    }

    private static Card.Suit parseSuit(char suitChar) {
        switch (suitChar) {
            case 'H':
                return Card.Suit.HEARTS;
            case 'D':
                return Card.Suit.DIAMONDS;
            case 'C':
                return Card.Suit.CLUBS;
            case 'S':
                return Card.Suit.SPADES;
            default:
                throw new IllegalArgumentException("Invalid suit: " + suitChar);
        }
    }
}
