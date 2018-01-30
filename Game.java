package com.QuestCardGame.GameMain;
import java.util.*;

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
		
		for (int i = 0; i < 12; i++) {
			Random generator = new Random();
			int x = generator.nextInt(5);
		    int n;
			if(x==0) {
			n = generator.nextInt(5);
			adventureDeck.addCard(new WeaponCard(n));}
			else if(x==1) {
				n = generator.nextInt(10);	
				adventureDeck.addCard(new FoeCard(n));}
			else if(x==2) {
				n = generator.nextInt(4);	
				adventureDeck.addCard(new TestCard(n));}
			else if(x==3) {
				n = generator.nextInt(9);
				adventureDeck.addCard(new AllieCard(n));}
			else if(x==4) {
				n = generator.nextInt(2);
				adventureDeck.addCard(new AmourCard(n));}
	}
	}

}

