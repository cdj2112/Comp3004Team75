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
	
	public void sortAscendingByBattlePoints() {
		Collections.sort(this, new Comparator<AdventureCard>() {
			public int compare(AdventureCard a, AdventureCard b) {
				return Integer.compare(a.getBattlePoint(false), b.getBattlePoint(false));
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
	
	public int getNumFoesToDiscard(int maxBattlePoints) {
		int numFoesToDiscard = 0;
		for(AdventureCard c: this) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) < maxBattlePoints)
				numFoesToDiscard++;
		}
		return numFoesToDiscard;
	}
	
	public boolean hasIncreasingBattlePointsForStages(int stages, int increaseBy) {
		int currentStageBattlePoints = 0;
		int battlePointsNeededForStage = increaseBy; 	//take the first valid card with BP > increaseBy
		boolean isAmourPlayed = false;					//only can play 1 per quest
		Hand h = new Hand();							//stores cards for each stage
		
		if(this.size() < stages)
			return false;
		
		this.sortDescendingByBattlePoints();
		
		for(int i = 0; i < stages; i++) {
			h.clear();
			
			if(i != 0) {
				battlePointsNeededForStage = currentStageBattlePoints + increaseBy;
				currentStageBattlePoints = 0;
			}
			
			for(AdventureCard c : this) {
				if(battlePointsNeededForStage <= 0)
					break;
				
				if(isValidPlay(h, c) && !isAmourPlayed) {
					h.add(c);
					if(c.getCardType() == AdventureCard.AdventureType.AMOURS)
						isAmourPlayed = true;
					battlePointsNeededForStage -= c.getBattlePoint(false);
					currentStageBattlePoints += c.getBattlePoint(false);
				}		
			}
			
			if(battlePointsNeededForStage > 0)
				return false;			
		}
		return true;
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
