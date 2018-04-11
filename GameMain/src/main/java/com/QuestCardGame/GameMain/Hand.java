package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

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
	
	public boolean hasIncreasingBattlePointsForStages(int stages, int increaseBy, ArrayList<AdventureCard> cardsInPlay) {
		int battlePointsNeededForStage = increaseBy; 	//take the first valid card with BP > increaseBy
		int amourAndAllyBattlePoints = 0;				//track separately because carried over each stage
		boolean isAmourPlayed = false;					//only can play 1 per quest
		Hand cardsToPlay = new Hand();					//stores cards for each stage - DOESN'T PLAY THEM
		
		if(this.size() < stages)
			return false;
		
		//examine what's already in play (should only be allies)
		for(AdventureCard c: cardsInPlay) {
			if(c.getCardType() == AdventureCard.AdventureType.ALLY)
				amourAndAllyBattlePoints += c.getBattlePoint(false);
		}
		
		this.sortDescendingByCardTypeBattlePoints();
		
		//based on what's in play, decide what we can play at each stage
		int currentStageBattlePoints = amourAndAllyBattlePoints;
		for(int i = 0; i < stages; i++) {
			cardsToPlay.clear();
			
			if(i != 0) {
				battlePointsNeededForStage = currentStageBattlePoints + increaseBy;
				currentStageBattlePoints = amourAndAllyBattlePoints;
			}
			
			for(AdventureCard c : this) {
				if(battlePointsNeededForStage <= 0)
					break;
				
				if(isValidPlay(cardsToPlay, c)) {		
					if(c.getCardType() == AdventureCard.AdventureType.AMOURS) {
						if(isAmourPlayed) {
							continue;
						}
						else {
							isAmourPlayed = true;
							amourAndAllyBattlePoints += c.getBattlePoint(false);
						}
					}
					else if(c.getCardType() == AdventureCard.AdventureType.ALLY) {
						amourAndAllyBattlePoints += c.getBattlePoint(false);
					}
					cardsToPlay.add(c);
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
		else if(newCard.getCardType() == AdventureCard.AdventureType.TEST)
			return false;
		
		for(AdventureCard c : h) {
			if(c.getCardType() == AdventureCard.AdventureType.AMOURS)
				return false;
		}
		
		for(AdventureCard c : h) {
			if(c.getName().compareTo(newCard.getName()) == 0)
				return false;
		}
		
		return true;
	}
	
	// This should be checking that foe + weapons can increase at each
	// stage. I'm not entirely sure how to do that right now.
	public boolean hasCardsToSponsorQuest(int numStages) {
		boolean hasTestCard = false;
		int numFoesNeededForQuest = numStages;
		for(AdventureCard c: this) {
			if(c.getCardType() == AdventureCard.AdventureType.TEST) {
				hasTestCard = true;
				break;
			}
		}
		
		if(hasTestCard)
			numFoesNeededForQuest--;
		
		this.sortAscendingByBattlePoints();
		
		int numFoesInHand = 0;
		int prevFoeBattlePoints = 0;
		for(AdventureCard c: this) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) > prevFoeBattlePoints) {
				numFoesInHand++;
				prevFoeBattlePoints = c.getBattlePoint(false);
			}
		}
		
		return numFoesInHand >= numFoesNeededForQuest; 
	}
	
	public AdventureCard getTestCard() {
		for(AdventureCard c : this) {
			if(c.getCardType() == AdventureCard.AdventureType.TEST)
				return c;
		}
		return null;
	}
	
	public AdventureCard getStrongestFoe() {
		this.sortDescendingByBattlePoints();
		for(AdventureCard c : this) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE)
				return c;
		}
		return null;
	}
	
	public AdventureCard getFoe(int battlePoints) {
		this.sortAscendingByBattlePoints();
		for(AdventureCard c : this) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) >= battlePoints)
				return c;
		}
		return null;
	}
	
	public boolean isValidForQuestStage(AdventureCard newCard, ArrayList<AdventureCard> stage) {
		for(AdventureCard c : stage) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && newCard.getCardType() == AdventureCard.AdventureType.FOE)
				return false;
			else if(c.getName() == newCard.getName())
				return false;
		}
		return true;
	}
	
	public ArrayList<AdventureCard> getDuplicateWeapons(){
		HashSet<String> duplicateNames = new HashSet<String>();
		ArrayList<AdventureCard> duplicateWeapons = new ArrayList<AdventureCard>();
		String prevName = "";
		this.sort(new AdventureCardComparator());
		for(AdventureCard c : this) {
			String cardName = c.getName();
			if(c.getCardType() == AdventureCard.AdventureType.WEAPON) {
				if(!duplicateNames.add(cardName) && prevName.equals(cardName))
					duplicateWeapons.add(c);
			}
			prevName = cardName;
		}
		
		return duplicateWeapons;	
	}
	
	public int getNumDuplicates(ArrayList<AdventureCard> without) {
		TreeSet<AdventureCard> uniqueCards = new TreeSet<AdventureCard>(new AdventureCardComparator());
		int numInWithoutAndHand = 0; 
		for(AdventureCard c : this) {
			if(!without.contains(c))
				uniqueCards.add(c);
			else
				numInWithoutAndHand++;
		}
		return this.size() - uniqueCards.size() - numInWithoutAndHand;
	}
	
	
	public int getNumUniqueCards(AdventureCard.AdventureType cardType) {
		TreeSet<AdventureCard> uniqueCards = new TreeSet<AdventureCard>(new AdventureCardComparator());
		for(AdventureCard c : this) {
			if(c.getCardType() == cardType)
				uniqueCards.add(c);
		}
		return uniqueCards.size();
	}
	
	public ArrayList<AdventureCard> getUniqueCards(AdventureCard.AdventureType cardType, int maxNumberToGet){
		TreeSet<AdventureCard> uniqueCards = new TreeSet<AdventureCard>(new AdventureCardComparator());
		for(AdventureCard c : this) {
			if(c.getCardType() == cardType && uniqueCards.size() < maxNumberToGet)
				uniqueCards.add(c);
		}
		return new ArrayList<AdventureCard>(uniqueCards);
	}
	
	public ArrayList<AdventureCard> getDuplicateCards(ArrayList<AdventureCard> without){
		TreeSet<AdventureCard> uniqueCards = new TreeSet<AdventureCard>(new AdventureCardComparator());
		ArrayList<AdventureCard> duplicateCards = new ArrayList<AdventureCard>();
		for(AdventureCard c : this) {
			if(!without.contains(c) && !uniqueCards.add(c))
				duplicateCards.add(c);
		}
		return duplicateCards;
	}
}
