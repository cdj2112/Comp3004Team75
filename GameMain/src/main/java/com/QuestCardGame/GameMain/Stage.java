package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public class Stage {
	private ArrayList<Card> cards;

	public void addCard(Card c) {
		cards.add(c);
	}

	public int getBattlePoints() {
		int total = 0;
		for (Card c : cards) {
			total+=c.getId();
		}
		return total;
	}
	
	public int getNumCards() {
		return cards.size();
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
}
