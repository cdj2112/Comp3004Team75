package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand extends ArrayList<AdventureCard>{
	
	public void sortAscendingByBattlePoints() {
		Collections.sort(this, new Comparator<AdventureCard>() {
			public int compare(AdventureCard a, AdventureCard b) {
				return Integer.compare(a.getBattlePoint(false), b.getBattlePoint(false));
			}
		});
	}
	
	public void sortDescendingByBattlePoints() {
		Collections.sort(this, new Comparator<AdventureCard>() {
			public int compare(AdventureCard a, AdventureCard b) {
				return Integer.compare(b.getBattlePoint(false), a.getBattlePoint(false));
			}
		});
	}
	
	public void sortDescendingByCardTypeBattlePoints() {
		Collections.sort(this, new Comparator<AdventureCard>() {
			public int compare(AdventureCard a, AdventureCard b) {
				int battlePointCompare = Integer.compare(b.getBattlePoint(false), a.getBattlePoint(false));
				int typeCompare = Integer.compare(b.getCardType().ordinal(), a.getCardType().ordinal());
				return typeCompare == 0 ? battlePointCompare : typeCompare;
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
	
	public Hand getBestPossibleHand(ArrayList<AdventureCard> cardsInPlay) {
		Hand cardsToPlay = new Hand();
		boolean isAmourPlayed = false;
		
		for(AdventureCard c: cardsInPlay) {
			if(c.getCardType() == AdventureCard.AdventureType.AMOURS)
				isAmourPlayed = true;
		}
		
		this.sortDescendingByCardTypeBattlePoints();
		
		for(AdventureCard c: this) {
			if(c.getCardType() == AdventureCard.AdventureType.AMOURS && isAmourPlayed)
				continue;
			else if(isValidPlay(cardsToPlay, c))
				cardsToPlay.add(c);
		}
		return cardsToPlay;
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
		
		this.sortDescendingByCardTypeBattlePoints();
		
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
	
	public Hand getHandToPlayForQuestStage(int requiredBattlePoints, ArrayList<AdventureCard> cardsInPlay) {
		Hand handToPlay = new Hand();
		
		// subtract amour and ally battle points because they're 
		// included in requiredBattlePoints. They should be included
		// in this round too, so we only need the difference
		boolean containsAmour = false;
		for(AdventureCard c : cardsInPlay) {
			if(c.getCardType() == AdventureCard.AdventureType.AMOURS) {
				containsAmour = true;
				requiredBattlePoints -= c.getBattlePoint(false);
			}
			else if(c.getCardType() == AdventureCard.AdventureType.ALLY) {
				requiredBattlePoints -= c.getBattlePoint(false);
			}
		}
		
		//this: the hand who's calling the function
		//		it will always be the AI's hand
		for(AdventureCard c : this) {
			if(requiredBattlePoints <= 0)
				break;
			
			if(isValidPlay(handToPlay, c)) {
				if(c.getCardType() == AdventureCard.AdventureType.AMOURS && containsAmour)
					continue;
				handToPlay.add(c);
				requiredBattlePoints -= c.getBattlePoint(false);
			}		
		}
		
		return handToPlay;
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
