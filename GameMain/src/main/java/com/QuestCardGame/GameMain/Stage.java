package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public class Stage {

	private ArrayList<AdventureCard> cards;
	
	public Stage(){
		cards = new ArrayList<AdventureCard>();
	}

	public boolean addCard(AdventureCard newCard) {
		for(AdventureCard c : cards) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && newCard.getCardType() == AdventureCard.AdventureType.FOE)
				return false;
			else if(c.getName() == newCard.getName())
				return false;
		}		
		cards.add(newCard);
		return true;
	}

	public int getBattlePoints() {
		int total = 0;
		for (AdventureCard c : cards) {
			total+=c.getBattlePoint(false); //no special bp for now
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
}
