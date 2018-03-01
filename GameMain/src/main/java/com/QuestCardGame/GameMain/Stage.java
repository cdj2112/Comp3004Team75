package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public class Stage {

	private ArrayList<AdventureCard> cards;

	public Stage() {
		cards = new ArrayList<AdventureCard>();
	}

	public void addCard(AdventureCard c) {
		cards.add(c);
	}

	public int getBattlePoints() {
		int total = 0;
		for (AdventureCard c : cards) {
			total += c.getBattlePoint(false); // no special bp for now
		}
		return total;
	}

	public int getNumCards() {
		return cards.size();
	}

	public ArrayList<AdventureCard> getCards() {
		return cards;
	}
}
