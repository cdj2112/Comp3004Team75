package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand extends ArrayList<AdventureCard>{

	public void sortDescendingByBattlePoints() {
		Collections.sort(this, new Comparator<AdventureCard>() {
			public int compare(AdventureCard a, AdventureCard b) {
				return Integer.compare(b.getBattlePoint(false), a.getBattlePoint(false));
			}
		});
	}
	
	public Hand getCardsForPoints(int pointTotal) {
		Hand cards = new Hand();
		int pointsSoFar = 0;
		
		if(pointTotal <= 0)
			return null;
	
		this.sortDescendingByBattlePoints();
		
		for(AdventureCard c: this) {
			if(pointsSoFar >= pointTotal)
				break;
			if(isValidPlay(cards, c))
				cards.add(c);
		}
		
		return cards.size() > 0 ? cards : null;
	}
	
	public Hand getBestPossibleHand() {
		Hand cards = new Hand();
		
		this.sortDescendingByBattlePoints();
		
		for(AdventureCard c: this) {
			if(isValidPlay(cards, c))
				cards.add(c);
		}
		return cards;
	}
	
	public boolean isValidPlay(Hand h, AdventureCard newCard) {
		if(newCard.getCardType() == AdventureCard.AdventureType.FOE)
			return false;
		
		for(AdventureCard c : h) {
			if(c.getName().compareTo(newCard.getName()) == 0)
				return false;
		}
		
		return true;
	}
}
