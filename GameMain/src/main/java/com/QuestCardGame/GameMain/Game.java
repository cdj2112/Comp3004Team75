package com.QuestCardGame.GameMain;

public class Game {

	private Player player;
	private Deck deck;
	
	Game(){
		player = new Player();
		deck = new Deck(10);
	}
	
	public void playerDraw() {
		Card c = deck.drawCard();
		player.drawCard(c);
	}
	
	public Player getPlayer() {
		return player;
	}
}
