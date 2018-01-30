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
		    int m,n,y;
		    m = generator.nextInt(30);
		    y = generator.nextInt(30);
			if(x==0) {
			n = generator.nextInt(5);
			adventureDeck.addCard(new WeaponCard(tempObeject(n), m));}
			else if(x==1) {
				n = generator.nextInt(10);	
				adventureDeck.addCard(new FoeCard(tempObeject1(n), m, y));}
			else if(x==2) {
				n = generator.nextInt(4);	
				adventureDeck.addCard(new TestCard(tempObeject2(n)));}
			else if(x==3) {
				n = generator.nextInt(9);
				adventureDeck.addCard(new AllieCard(tempObeject3(n),m));}
			else if(x==4) {
				n = generator.nextInt(2);
				adventureDeck.addCard(new AmourCard(tempObeject4(n),m));}
	}
	}
	
	
	//temp method need store name, bp ,card frequence...in datebase later.
	private String tempObeject(int x) {
		String name = "";
	if(x == 0) {name = "Excalibur";}
		else if (x == 1) {name = "Lance";}
		else if (x == 2) {name = "Battex-ax";}
		else if (x == 3) {name = "sword";}
		else if (x == 4) {name = "horse";}
		else if (x == 5) {name = "Dagger";}
	return name;
	}
	
	private String tempObeject1(int x) {
		String name = "";
	if(x == 0) {name = "Dragon";}
		else if (x == 1) {name = "Giant";}
		else if (x == 2) {name = "Mordred";}
		else if (x == 3) {name = "greenKnight";}
		else if (x == 4) {name = "BlackKnight";}
		else if (x == 5) {name = "EvilKnight";}
		else if (x == 6) {name = "SaxonKnight";}
		else if (x == 7) {name = "RobberKnight";}
		else if (x == 8) {name = "Saxons";}
		else if (x == 9) {name = "Boar";}
		else if (x == 10) {name = "Thieves";}
	return name;
	}
	
	private String tempObeject2(int x) {
		String name = "";
	if(x == 0) {name = "Valor";}
		else if (x == 1) {name = "Temptation";}
		else if (x == 2) {name = "MorganLeFey";}
		else if (x == 3) {name = "QuestionBeat";}
	return name;
	}
	
	private String tempObeject3(int x) {
		String name = "";
	if(x == 0) {name = "Galahad";}
		else if (x == 1) {name = "Lancelot";}
		else if (x == 2) {name = "Arthur";}
		else if (x == 3) {name = "Tristan";}
		else if (x == 4) {name = "Pellinore";}
		else if (x == 5) {name = "Gawain";}
		else if (x == 6) {name = "Perecial";}
		else if (x == 7) {name = "Guinevere";}
		else if (x == 8) {name = "Iseult";}
		else if (x == 9) {name = "Merlin";}
	return name;
	}
	
	private String tempObeject4(int x) {
		String name = "";
	if(x == 0) {name = "Amour1";}
		else if (x == 1) {name = "Amour2";}
		else if (x == 2) {name = "Amour3";}

	return name;
	}
}

