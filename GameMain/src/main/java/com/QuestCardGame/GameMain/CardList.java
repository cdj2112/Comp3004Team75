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
		System.out.println("Open Weapon Scanner");
		while (weaponScanner.hasNext()) {
			String[] cardInfo = weaponScanner.next().split(";");
			int amount = Integer.parseInt(cardInfo[1]);
			System.out.println(Arrays.toString(cardInfo));
			for (int i = 0; i < amount; i++) {
				System.out.println(i);
				Weapon w = new Weapon(cardInfo[0], Integer.parseInt(cardInfo[2]));
				adventureDeck.addCard(w);
			}
		}
		}
	}

	}
}
