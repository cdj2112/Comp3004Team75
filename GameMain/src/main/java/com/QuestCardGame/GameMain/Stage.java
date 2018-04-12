package com.QuestCardGame.GameMain;

import java.util.ArrayList;

import com.QuestCardGame.GameMain.AdventureCard.AdventureType;

public class Stage {

	private ArrayList<AdventureCard> cards;
	private boolean isTest;
	
	public Stage(){
		cards = new ArrayList<AdventureCard>();
		isTest = false;
	}

	public boolean addCard(AdventureCard newCard) {
		if(newCard.getCardType() == AdventureType.ALLY || newCard.getCardType() == AdventureType.AMOURS) {
			return false;
		}
		
		
		if(isTest) {
			return false;
		}
		
		if(newCard.getCardType() == AdventureType.TEST && cards.size() > 0) {
			return false;
		}
		
		for(AdventureCard c : cards) {
			if(c.getCardType() == AdventureType.FOE && newCard.getCardType() == AdventureType.FOE)
				return false;
			else if(c.getName() == newCard.getName())
				return false;
		}
		
		isTest = newCard.getCardType() == AdventureType.TEST;
		cards.add(newCard);
		return true;
	}

	public int getBattlePoints(String target) {
		int total = 0;
		for (AdventureCard c : cards) {
			boolean bonus = target != null && c.getName().contains(target);
			total+=c.getBattlePoint(bonus); 
		}
		return total;
	}
	
	public int getNumCards() {
		return cards.size();
	}
	
	public ArrayList<AdventureCard> getCards() {
		return cards;
	}
	
	public int getNumFoes() {
		int numFoes = 0;
		for(AdventureCard c : cards) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE)
				numFoes++;
		}
		return numFoes;
	}
	
	public boolean getIsTest() {
		return isTest;
	}
	
	public int getMinBid(String name) {
		Test t = (Test)(cards.get(0));
		return t.getMinBid(name);
	}
}
