package com.QuestCardGame.GameMain;

import java.util.ArrayList;

import com.QuestCardGame.GameMain.AdventureCard.AdventureType;

public class Stage {

	private ArrayList<AdventureCard> cards;
	
	public Stage(){
		cards = new ArrayList<AdventureCard>();
	}

	public boolean addCard(AdventureCard newCard) {
		if(newCard.getCardType() == AdventureType.ALLY || newCard.getCardType() == AdventureType.AMOURS) {
			return false;
		}
		
		for(AdventureCard c : cards) {
			if(c.getCardType() == AdventureType.FOE && newCard.getCardType() == AdventureType.FOE)
				return false;
			else if(c.getName() == newCard.getName())
				return false;
		}		
		cards.add(newCard);
		return true;
	}

	public int getBattlePoints(String target) {
		int total = 0;
		for (AdventureCard c : cards) {
			boolean bonus = target != null && c.getName().contains(target);
			total+=c.getBattlePoint(bonus); //no special bp for now
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
