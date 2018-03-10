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

	public static void populateAdventureCards(Deck adventureDeck) throws FileNotFoundException {
		
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
		
		//Format Foe_Name;Amount;BattlePoints;AlternateBattlePoints
		Scanner foeScanner = new Scanner(new File("./src/resources/FoeList.txt"));
		while (foeScanner.hasNext()) {
			String[] cardInfo = foeScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				Foe f = new Foe(cardInfo[0].replace('_', ' '), Integer.parseInt(cardInfo[2]), Integer.parseInt(cardInfo[3]));
				adventureDeck.addCard(f);
			}
		}
	}
	
	public static void populateStoryCards(Deck storyDeck) throws FileNotFoundException {
		
		//Format QuestName_Name;Amount;NumStages
		Scanner questScanner = new Scanner(new File("./src/resources/QuestList.txt"));
		while(questScanner.hasNext()) {
			String[] cardInfo = questScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				QuestCard f = new QuestCard(cardInfo[0].replace('_', ' '), Integer.parseInt(cardInfo[2]));
				storyDeck.addCard(f);
			}
		}
		
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
}

	
	public static void populateRiggedAdventureCards(Deck adventureDeck) {
		
		//Extra
		adventureDeck.addCard(new Foe("Giant", 40, 40));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Giant", 15, 15));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		
		//Player 1 Draw 6
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Mordred", 30, 30));
		adventureDeck.addCard(new Foe("Saxon Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		
		//Player 4 Draw 1
		adventureDeck.addCard(new Weapon("Sword", 10));
		
		//Player 3 Draw 1
		adventureDeck.addCard(new Foe("Thieves", 5, 5));
		
		//Player 4 Draw 1
		adventureDeck.addCard(new Weapon("Lance", 20));
		
		//Player 3 Draw 1
		adventureDeck.addCard(new Foe("Giant", 40, 40));
		
		//Player 2 Draw 1
		adventureDeck.addCard(new Weapon("Dagger", 5));
		
		//Player 4 Hand
		adventureDeck.addCard(new Foe("Dragon", 50, 70));
		adventureDeck.addCard(new Foe("Saxon Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Mordred", 30, 30));
		adventureDeck.addCard(new Foe("Saxon Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		
		//Player 3 Hand
		adventureDeck.addCard(new Foe("Giant", 40, 40));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Giant", 15, 15));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		
		//Player 2 Hand
		adventureDeck.addCard(new Foe("Robber Knight", 15, 15));
		adventureDeck.addCard(new Foe("Evil Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Lance", 20));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Robber Knight", 15, 15));
		adventureDeck.addCard(new Foe("Evil Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Lance", 20));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		
		
		//Player 1 hand
		adventureDeck.addCard(new Foe("Saxons", 10, 20));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Sword", 10));
		adventureDeck.addCard(new Weapon("Dagger", 5));
		adventureDeck.addCard(new Foe("Black Knight", 25, 35));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Saxons", 10, 20));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Sword", 10));
		adventureDeck.addCard(new Weapon("Dagger", 5));
		adventureDeck.addCard(new Foe("Black Knight", 25, 35));
		adventureDeck.addCard(new Weapon("Horse", 10));
	}
	
	public static void populateRiggedStoryCards(Deck storyDeck) throws FileNotFoundException {
		populateStoryCards(storyDeck);
		storyDeck.addCard(new QuestCard("Boar Hunt", 2));
	}
	
	public static void populateRiggedAdventureCards(Deck adventureDeck) {
		
		//Extra
		adventureDeck.addCard(new Foe("Giant", 40, 40));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Giant", 15, 15));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		
		//Player 1 Draw 6
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Mordred", 30, 30));
		adventureDeck.addCard(new Foe("Saxon Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		
		//Player 4 Draw 1
		adventureDeck.addCard(new Weapon("Sword", 10));
		
		//Player 3 Draw 1
		adventureDeck.addCard(new Foe("Thieves", 5, 5));
		
		//Player 4 Draw 1
		adventureDeck.addCard(new Weapon("Lance", 20));
		
		//Player 3 Draw 1
		adventureDeck.addCard(new Foe("Giant", 40, 40));
		
		//Player 2 Draw 1
		adventureDeck.addCard(new Weapon("Dagger", 5));
		
		//Player 4 Hand
		adventureDeck.addCard(new Foe("Dragon", 50, 70));
		adventureDeck.addCard(new Foe("Saxon Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Mordred", 30, 30));
		adventureDeck.addCard(new Foe("Saxon Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		
		//Player 3 Hand
		adventureDeck.addCard(new Foe("Giant", 40, 40));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Giant", 15, 15));
		adventureDeck.addCard(new Foe("Saxon Knight", 15, 25));
		adventureDeck.addCard(new Weapon("Excalibur", 30));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		
		//Player 2 Hand
		adventureDeck.addCard(new Foe("Robber Knight", 15, 15));
		adventureDeck.addCard(new Foe("Evil Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Lance", 20));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Robber Knight", 15, 15));
		adventureDeck.addCard(new Foe("Evil Knight", 20, 30));
		adventureDeck.addCard(new Weapon("Lance", 20));
		adventureDeck.addCard(new Weapon("Battle-ax", 15));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Horse", 10));
		
		
		//Player 1 hand
		adventureDeck.addCard(new Foe("Saxons", 10, 20));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Sword", 10));
		adventureDeck.addCard(new Weapon("Dagger", 5));
		adventureDeck.addCard(new Foe("Black Knight", 25, 35));
		adventureDeck.addCard(new Weapon("Horse", 10));
		adventureDeck.addCard(new Foe("Saxons", 10, 20));
		adventureDeck.addCard(new Foe("Boar", 5, 15));
		adventureDeck.addCard(new Weapon("Sword", 10));
		adventureDeck.addCard(new Weapon("Dagger", 5));
		adventureDeck.addCard(new Foe("Black Knight", 25, 35));
		adventureDeck.addCard(new Weapon("Horse", 10));
	}
	
	public static void populateRiggedStoryCards(Deck storyDeck) throws FileNotFoundException {
		populateStoryCards(storyDeck);
		storyDeck.addCard(new QuestCard("Boar Hunt", 2));
	}

}

