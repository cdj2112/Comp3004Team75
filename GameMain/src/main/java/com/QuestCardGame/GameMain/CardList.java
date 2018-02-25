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
		Scanner weaponScanner = new Scanner(new File("./src/resources/WeaponList.txt"));
		while (weaponScanner.hasNext()) {
			String[] cardInfo = weaponScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			for (int i = 0; i < amount; i++) {
				Weapon w = new Weapon(cardInfo[0], Integer.parseInt(cardInfo[2]));
				adventureDeck.addCard(w);
			}
		}
		
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

	}
}
