package com.QuestCardGame.GameMain;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class CardList {
	// Integer is stand for the amount of copies of a same card

	/*
	 * public CardList() { adventureList = new ArrayList<String>(); attributeList =
	 * new ArrayList<String>(); storyList = new HashMap<String, Integer>(); try {
	 * generateList(); } catch (FileNotFoundException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } // System.out.print(adventureList.get(1));
	 * }
	 *
	 * public List<String> getAdventureList() { return adventureList; } public
	 * List<String> getAttributeList() { return attributeList; } public Map<String,
	 * Integer> getStoryList() { return storyList; }
	 */

	public static void populateAdventureCards(Deck adventureDeck, Game g) throws FileNotFoundException {

		//Format Weapon_Name;Amount;BattlePoints
		Scanner weaponScanner = new Scanner(new File("./src/resources/WeaponList.txt"));
		while (weaponScanner.hasNext()) {
			String[] cardInfo = weaponScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				Weapon w = new Weapon(cardInfo[0].replace('_', ' '), Integer.parseInt(cardInfo[2]));
				adventureDeck.addCard(w);
			}
		}
		weaponScanner.close();

		// Format Foe_Name;Amount;BattlePoints;AlternateBattlePoints
		Scanner foeScanner = new Scanner(new File("./src/resources/FoeList.txt"));
		while (foeScanner.hasNext()) {
			String[] cardInfo = foeScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				Foe f = new Foe(cardInfo[0].replace('_', ' '), Integer.parseInt(cardInfo[2]),
						Integer.parseInt(cardInfo[3]));
				adventureDeck.addCard(f);
			}
		}
		foeScanner.close();
		
		//Format Test_Name;Amount;MinBids;BonusMinBids;Target
		Scanner testScanner = new Scanner(new File("./src/resources/TestList.txt"));
		while(testScanner.hasNext()) {
			String[] cardInfo = testScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for(int i = 0; i<amount; i++) {
				Test t = new Test(cardInfo[0].replace('_', ' '), Integer.parseInt(cardInfo[2]), Integer.parseInt(cardInfo[3]), cardInfo[4].replace('_', ' '));
				adventureDeck.addCard(t);
			}
		}
		testScanner.close();

		// Format Ally_Name;Amount;BattlePoints;Bids;AlternateBids;AlternateBattlePoints;Target;UseFinder
		Scanner AllyScanner = new Scanner(new File("./src/resources/AllyList.txt"));
		while (AllyScanner.hasNext()) {
			String[] cardInfo = AllyScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				Ally a = new Ally(cardInfo[0].replace('_', ' '), Integer.parseInt(cardInfo[2]),
						Integer.parseInt(cardInfo[3]), Integer.parseInt(cardInfo[4]), Integer.parseInt(cardInfo[5]),
						cardInfo[6].replace('_', ' '));
				boolean useFinder = Boolean.parseBoolean(cardInfo[7]);
				if (useFinder) {
					PlayFinder pf = new PlayFinder(g, cardInfo[6].replace('_', ' '));
					a.setFinder(pf);
				}
				adventureDeck.addCard(a);
			}
		}
		AllyScanner.close();

		for (int i = 0; i < 8; i++) {
			Amours am = new Amours();
			adventureDeck.addCard(am);
		}

	}

	public static void populateStoryCards(Deck storyDeck, Game g) throws FileNotFoundException {

		// Format QuestName_Name;Amount;NumStages
		Scanner questScanner = new Scanner(new File("./src/resources/QuestList.txt"));
		while (questScanner.hasNext()) {
			String[] cardInfo = questScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				String t = cardInfo[3].replace('_', ' ');
				if (t.equals("NULL"))
					t = null;
				else if (t.equals("ALL"))
					t = "";
				QuestCard f = new QuestCard(cardInfo[0].replace('_', ' '), Integer.parseInt(cardInfo[2]), t);
				storyDeck.addCard(f);
			}
		}
		questScanner.close();

		// Format Tournament_Name;bonusShields
		Scanner tournamentScanner = new Scanner(new File("./src/resources/TournamentList.txt"));
		while (tournamentScanner.hasNext()) {
			String[] cardInfo = tournamentScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				TournamentCard f = new TournamentCard(cardInfo[0].replace('_', ' '), Integer.parseInt(cardInfo[2]));
				storyDeck.addCard(f);
			}
		}
		tournamentScanner.close();
	
		//Format EventName_Name;Amount
		Scanner eventScanner = new Scanner(new File("./src/resources/EventList.txt"));
		EventFactory factory = new EventFactory(g);
		while(eventScanner.hasNext()) {
			String[] cardInfo = eventScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				EventCard f = new EventCard(cardInfo[0].replace('_', ' '));
				factory.effectCard(f);
				storyDeck.addCard(f);
			}
		}
		eventScanner.close();
}
	
	public static void populateRiggedAdventureCards(Deck adventureDeck, Game g) {
		
		try {
			populateAdventureCards(adventureDeck, g);
		} catch (Exception e) {
			System.out.println(e);
		}
		adventureDeck.shuffleDeck();

		//Player 4 Hand
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Foe("Robber Knight", 15, 15));
		adventureDeck.addCard(new Ally("King Pellinore", 10, 0, 4, 10, "Search for the Questing Beast"));
		adventureDeck.addCard(new Ally("Sir Gawain", 10, 0, 0, 20, "Test of the Green Knight"));
		adventureDeck.addCard(new Amours());
		adventureDeck.addCard(new Amours());
		adventureDeck.addCard(new Amours());
		adventureDeck.addCard(new Foe("Saxon Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));

		//Player 3 Hand
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Foe("Robber Knight", 15, 15));
		adventureDeck.addCard(new Ally("Sir Gawain", 10, 0, 0, 20, "Test of the Green Knight"));
		Ally sT = new Ally("Sir Tristan", 10, 0, 0, 20, "Queen Iseult");
		PlayFinder pf = new PlayFinder(g, "Queen Iseult");
		sT.setFinder(pf);
		adventureDeck.addCard(sT);
		adventureDeck.addCard(new Amours());
		adventureDeck.addCard(new Ally("Sir Galahad", 15, 0, 0, 15, "NULL"));
		adventureDeck.addCard(new Amours());
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));

		//Player 2 Hand
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Foe("Robber Knight", 15, 15));
		adventureDeck.addCard(new Ally("King Arthur", 10, 2, 2, 10, "NULL"));
		Ally qI = new Ally("Queen Iseult", 0, 2, 4, 0, "Sir Tristan");
		pf = new PlayFinder(g, "Sir Tristan");
		qI.setFinder(pf);
		adventureDeck.addCard(qI);
		adventureDeck.addCard(new Ally("Sir Percival", 5, 0, 0, 20, "Search for the Holy Grail"));
		adventureDeck.addCard(new Ally("Sir Lancelot", 15, 0, 0, 25, "Defend the Queen's Honor"));
		adventureDeck.addCard(new Amours());
		adventureDeck.addCard(new Foe("Dragon", 50, 70));
		adventureDeck.addCard(new Weapon("Dagger", 5));
		adventureDeck.addCard(new Weapon("Lance", 20));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));

		// Player 1 hand
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Foe("Robber Knight", 15, 15));
		adventureDeck.addCard(new Weapon("Dagger", 5));
		adventureDeck.addCard(new Weapon("Sword", 10));
		adventureDeck.addCard(new Test("Test of Morgan Le Fey", 3, 3, "NULL"));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Foe("Mordred", 30, 30));
		adventureDeck.addCard(new Foe("Giant", 40, 40));
		adventureDeck.addCard(new Test("Test of the Questing Beast", 1, 4, "Search for the Questing Beast"));
		adventureDeck.addCard(new Weapon("Lance", 20));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
	}
		
	public static void populateRiggedStoryCards(Deck storyDeck, Game g) throws FileNotFoundException {
		populateStoryCards(storyDeck, g);
		storyDeck.shuffleDeck();
		
		EventFactory factory = new EventFactory(g);
		
		EventCard cctc = new EventCard("Court Called to Camelot");
		factory.effectCard(cctc);
		storyDeck.addCard(cctc);
		
		EventCard qf = new EventCard("Queens Favor");
		factory.effectCard(qf);
		storyDeck.addCard(qf);
		
		EventCard kcta = new EventCard("King's Call to Arms");
		factory.effectCard(kcta);
		storyDeck.addCard(kcta);
		
		EventCard kr = new EventCard("King's Recognition");
		factory.effectCard(kr);
		storyDeck.addCard(kr);
		
		EventCard pl = new EventCard("Plague");
		factory.effectCard(pl);
		storyDeck.addCard(pl);
		
		EventCard po = new EventCard("Pox");
		factory.effectCard(po);
		storyDeck.addCard(po);
		
		storyDeck.addCard(new QuestCard("Defend the Queen's Honor", 4, ""));
		storyDeck.addCard(new QuestCard("Search for the Questing Beast", 4, null));
		storyDeck.addCard(new QuestCard("Search for the Holy Grail", 5, ""));
		storyDeck.addCard(new QuestCard("Search for the Questing Beast", 4, null));
		storyDeck.addCard(new QuestCard("Test of the Green Knight", 4, "Green Knight"));
		storyDeck.addCard(new QuestCard("Slay the Dragon", 3, "Dragon"));
		storyDeck.addCard(new TournamentCard("Camelot", 3));
		
		EventCard pttr = new EventCard("Prosperity Throughout the Realm");
		factory.effectCard(pttr);
		storyDeck.addCard(pttr);
		
		EventCard cd = new EventCard("Chivalrous Deed");
		factory.effectCard(cd);
		storyDeck.addCard(cd);
		
		storyDeck.addCard(new QuestCard("Boar Hunt", 2, "Boar"));
	}

}
