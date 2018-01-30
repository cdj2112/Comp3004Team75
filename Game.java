package com.QuestCardGame.GameMain;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Game {

	private Player player;
	private Deck storyDeck;
	private Deck adventureDeck;
	private List<String> adventage;
	private List<String> attribute;

	Game()  {
		player = new Player();
		try {
			iniCards();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		int z = 0;
		for (int i = 0; i < 12; i++) {			
			int x = 0;
			while(x == 0) {
				Random generator = new Random();
				int y = generator.nextInt(97);
				if(adventage.get(y) != "null") {
					x = 1; 
					z = y;
					
				}	
			}
          
	  if(z<49){
		  int m = Integer.parseInt(attribute.get(z));
	      adventureDeck.addCard(new WeaponCard(adventage.get(z), m));
	  }else if(z>=49 && z<99){
		  String s[] = attribute.get(z).split("&");
		  int s1 = Integer.parseInt(s[0]);
		  int s2 = Integer.parseInt(s[1]);
          adventureDeck.addCard(new FoeCard(adventage.get(z), s1,s2));
	  }else if(x>=99 && x<107){
		  adventureDeck.addCard(new TestCard(adventage.get(z)));
	  }else if(x>=107 && x<117){
		  adventureDeck.addCard(new AllieCard(adventage.get(z)));
	  }else{
		  adventureDeck.addCard(new AmourCard(adventage.get(z)));
	  }
	  usedCard(z);
		}
	}


	
	public void usedCard(int x) {
		adventage.set(x,"null");	
	}
	
	private void iniCards() throws FileNotFoundException {
		adventage = new ArrayList<String>();
		attribute = new ArrayList<String>();
		Scanner s = new Scanner(new File("AdventureDeck.txt"));
		Scanner s1 = new Scanner(new File("AdventureAttribute.txt"));
		while (s.hasNext()) {
			adventage.add(s.next());
        }
		s.close();
		while (s1.hasNext()) {
			attribute.add(s1.next());
        }
        s1.close();
	}
};

