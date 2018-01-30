package com.QuestCardGame.GameMain;

public class Game {

	private Player player;
	private Deck storyDeck;
	private Deck adventureDeck;

	Game() {
		player = new Player();
		initStoryDeck();
		initAdventureDeck();
	}

	public void playerDrawAdventureCard(Player p) {
		Card c = adventureDeck.drawCard();

		if (c != null)
			p.drawCard(c);
	}

	public Player getPlayer() {
		return player;
	}

	private void initStoryDeck() {
		storyDeck = new Deck();

		storyDeck.addCard(new QuestCard("Journey Through the Enchanted Forest", 3));
		storyDeck.addCard(new QuestCard("Vanquish King Arthur's Enemies", 3));
		storyDeck.addCard(new QuestCard("Repel the Saxon Raiders", 2));
	}

	private void initAdventureDeck() {
		adventureDeck = new Deck();

		for (int i = 0; i < 6; i++) {
			adventureDeck.addCard(new WeaponCard("Dagger", 5));
		}
	}
}
