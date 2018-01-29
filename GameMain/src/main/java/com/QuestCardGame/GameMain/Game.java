package com.QuestCardGame.GameMain;

public class Game {

	private Player player;
	private Deck storyDeck;
	private Deck adventureDeck;
	
	Game(){
		player = new Player();
		initStoryDeck();
	}
	
	public Card playerDrawAdventureCard(Player p) {
		Card c = adventureDeck.drawCard();
		
		if(c != null)
			p.drawCard(c);
		
		return c;
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
		
		//TODO: add cards
	}
}
