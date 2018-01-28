package com.QuestCardGame.GameMain;

public class Game {

	private Player player;
	private Deck deck;
	
	Game(){
		player = new Player();
		deck = new Deck(10);
	}
	
	public Card playerDraw() {
		Card c = deck.drawCard();
		player.drawCard(c);
		return c;
	}
	
	public Player getPlayer() {
		return player;
	}
}
