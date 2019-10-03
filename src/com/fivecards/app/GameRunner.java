package com.fivecards.app;

import java.util.List;
import java.util.Scanner;

import com.fivecards.model.Card;
import com.fivecards.model.Game;
import com.fivecards.model.Player;
import com.fivecards.utils.RandomGenerator;

public class GameRunner {

	private Game game = new Game();
	Scanner scanner = new Scanner(System.in);
	private Player me, systemPlayer;

	public void runGame() {
		int option = 0;

		do {
			System.out.println("Five Cards");
			System.out.println("1. Play with system");
			System.out.println("2. Exit");

			option = Integer.parseInt(scanner.nextLine());
			dispatchOption(option);
		} while (option >= 1 && option <= 2);
	}

	private void dispatchOption(int option) {
		switch (option) {
		case 1: {
			playWithSystem();
			break;
		}
		case 2: {
			System.exit(0);
			break;
		}
		}
	}

	private void playWithSystem() {
		int option = 0;
		addPlayers();
		initiateGame();
		do {
			showCards(me);
			showMyPoints();
			System.out.println("What are you going to do?");

			System.out.println("1. Challenge for points: " + game.calculatePoints(me));
			System.out.println("2. Pick Open card: " + game.getOpenCard().getDisplayNumber() + ", and Drop my choice");
			System.out.println("3. Drop same cards");
			option = Integer.parseInt(scanner.nextLine());
			dispatchGameOption(option);
		} while (option >= 1 && option <= 2);
	}

	private void showMyPoints() {
		int points = game.calculatePoints(me);
		System.out.println("My Points: " + points);
		System.out.println();
	}

	private void dispatchGameOption(int option) {
		switch (option) {
		case 1: {

			break;
		}
		case 2: {
			pickOpenCardAndDropMyChoice();
			letSystemPlay();
			break;
		}
		}
	}

	private void letSystemPlay() {

		if (game.getOpenCard().getValue() >= 6) {
			List<Integer> cardIndices = game.getCardIndicesSameOfOpenCard(systemPlayer);
			if (cardIndices.size() > 0) {
				dropSameCards(systemPlayer, cardIndices);
			} else {
				pickEitherOpenCardOrFromDeck();
			}
		} else {
			pickEitherOpenCardOrFromDeck();
		}
		showCards(systemPlayer);

	}

	private void pickEitherOpenCardOrFromDeck() {
		int option = RandomGenerator.generateRandomNumber(1, 2);
		Card droppingCard = game.getBiggestCard(systemPlayer);
		systemPlayer.dropCard(droppingCard);
		game.setOpenCard(droppingCard);
		System.out.println("System dropped: " + game.getOpenCard().getDisplayNumber());
		if (option == 1) {
			systemPlayer.getCards().add(game.getOpenCard());
		} else if (option == 2) {
			Card card = game.pickCardFromDeck();
			systemPlayer.getCards().add(card);
		}
	}

	private void dropSameCards(Player player, List<Integer> cardIndices) {
		for (int i = 0; i < cardIndices.size(); i++) {
			player.getCards().remove((int) cardIndices.get(i));
		}
	}

	private void pickOpenCardAndDropMyChoice() {
		System.out.println("Which card you want to drop?");
		listMyCards();
		int cardIndex = Integer.parseInt(scanner.nextLine());
		Card droppingCard = me.getCards().get(cardIndex);
		me.getCards().remove(cardIndex - 1);
		me.getCards().add(game.getOpenCard());
		game.setOpenCard(droppingCard);
	}

	private void initiateGame() {
		System.out.println();
		System.out.println("System is shuffling cards...");
		game.startGame();
		game.showRandomJokerCard();
		Card card = game.pickCardFromDeck();
		game.setOpenCard(card);
		System.out.println("Open card: " + game.getOpenCard().getDisplayNumber());
	}

	private void showCards(Player player) {
		System.out.print(player.getName() + " Cards: ");
		for (Card card : player.getCards()) {
			System.out.print(card.getDisplayNumber() + "  ");
		}
		System.out.println();
	}

	private void listMyCards() {
		System.out.println("My Cards: ");
		for (int i = 0; i < me.getCards().size(); i++) {
			Card card = me.getCards().get(i);
			System.out.println((i + 1) + ". " + card.getDisplayNumber());
		}
	}

	private void addPlayers() {
		System.out.println("Enter your name");

		String name = scanner.nextLine();
		me = new Player();
		me.setName(name);
		me.setId(1);

		systemPlayer = new Player();
		systemPlayer.setName("System");
		systemPlayer.setId(2);

		game.addPlayer(me);
		game.addPlayer(systemPlayer);
	}
}
