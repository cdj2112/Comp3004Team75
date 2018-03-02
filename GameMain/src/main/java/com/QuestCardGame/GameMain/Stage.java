package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public class Stage {

	private ArrayList<AdventureCard> cards;
	private int battlePoint;

	public Stage(){
		cards = new ArrayList<AdventureCard>();
		battlePoint = 0;
	}

	public void addCard(AdventureCard c, boolean isSpecialBettlePoint) {
		cards.add(c);
		battlePoint += c.getBattlePoint(isSpecialBettlePoint);
	}

	public ArrayList<AdventureCard> returnAllCards(){
		ArrayList<AdventureCard> returnCards = new ArrayList<AdventureCard>();
		returnCards.addAll(cards);
		cards.alear();
		battlePoint = 0;
		return returnCards;
	}

	public int getBattlePoints() {
		return battlePoint;
	}

	public int getNumCards() {
		return cards.size();
	}

	public ArrayList<AdventureCard> getCards() {
		return cards;
	}
}
