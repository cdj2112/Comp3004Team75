package com.QuestCardGame.GameMain;

public class Game {

	private Player player;
	private Deck storyDeck;
	
	Game(){
		player = new Player();
		initStoryDeck();
	}
	
	public void playerDraw(Player p, Deck d) {
		p.drawCard(d.drawCard());
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
}
