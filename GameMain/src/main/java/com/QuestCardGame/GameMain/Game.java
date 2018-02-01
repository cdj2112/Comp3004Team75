package com.QuestCardGame.GameMain;

public class Game {

	private Player [] players;
	private Deck storyDeck;
	private Deck adventureDeck;
	private int activePlayer;

	Game() {
		players = new Player[4];
		for(int i=0; i<4;i++) {
			players[i] = new Player();
		}
		activePlayer = 0;
		initStoryDeck();
		initAdventureDeck();
	}

	public void playerDrawAdventureCard(Player p) {
		Card c = adventureDeck.drawCard();

		if (c != null)
			p.drawCard(c);
	}
	
	public Card getStoryCard() {
		Card c = storyDeck.drawCard();
		
		if(c != null)
			return c;
		else
			return null;
	}
	
	public Player getPlayer() {
		return players[0];
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
