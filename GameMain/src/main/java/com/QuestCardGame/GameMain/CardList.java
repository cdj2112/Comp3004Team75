package com.QuestCardGame.GameMain;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class CardList {
	private List<String> adventureList;
	private List<String> attributeList;
	private Map<String, Integer> storyList;
	// Integer is stand for the amount of copies of a same card

	public CardList() {
		adventureList = new ArrayList<String>();
		attributeList = new ArrayList<String>();
		storyList = new HashMap<String, Integer>();
		try {
			generateList();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.print(adventureList.get(1));
	}

	public List<String> getAdventureList() {
		return adventureList;
	}
	public List<String> getAttributeList() {
		return attributeList;
	}
	public Map<String, Integer> getStoryList() {
		return storyList;
	}

	private void generateList() throws FileNotFoundException {
		// hardcode for now, may get from text file later
		/* Adventure Card Initialization */
		// Allies
		Scanner s = new Scanner(new File("AdventureDeck.txt"));
		Scanner s1 = new Scanner(new File("AdventureAttribute.txt"));
		while (s.hasNext()) {
			adventureList.add(s.next());
		}
		s.close();
		while (s1.hasNext()) {
			attributeList.add(s1.next());
		}
		s1.close();
		/* Adventure Card Initialization finished */

		/* Story Card Initialization */
		// Quests
		storyList.put("Search for the Holy Grail", 1);
		storyList.put("Test of the Green Knight", 1);
		storyList.put("Search for the Questing Beast", 1);
		/* Strry Card Initialization finished */

	}
	public void updateList(int x) {
		// not yet implenmented

	}
}
