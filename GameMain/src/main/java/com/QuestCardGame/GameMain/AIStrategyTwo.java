package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;

public class AIStrategyTwo implements AIPlayerStrategy {

	//Always participate
	public boolean doIJoinTournament(Game g) {
		return true;
	}

	//Play 50 BP or best possible hand
	public ArrayList<AdventureCard> playCardsForTournament(Game g) {
		ArrayList<AdventureCard> cardsToPlay = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> hand = g.getCurrentActivePlayerObj().getHand();

//		TODO
//		cardsToPlay = getCardsForPoints(50);
//		if(cardsToPlay.size() == 0)
//			cardsToPlay = getBestPossibleHand(hand);
		
		return cardsToPlay;
	}

	public boolean doISponsorAQuest(Game g) {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<Integer, ArrayList<AdventureCard>> createQuest(Player current, QuestCard qc) {
		// TODO Auto-generated method stub
		return null;
	}

	//Can increase BP each stage by 10 pts AND
	//has 2 foes less than 25 BP
	public boolean doIJoinQuest(Player current, int numStages) {
		ArrayList<AdventureCard> hand = current.getHand();
		int foesToDiscard = getNumFoesToDiscard(hand, 25);
		boolean isValidBattlePoints = false;

		//TODO 	check for increasing BP by 10 pts
		//isValidBattlePoints = doIHaveIncreasingBattlePoints(hand, numStages, 10);
		
		return  foesToDiscard >= 2 && isValidBattlePoints;
	}

	public ArrayList<AdventureCard> playCardsForQuestStage(Game g) {
		ArrayList<AdventureCard> cardsToPlay = new ArrayList<AdventureCard>();

//		if(currentStage == totalStages) {
//			//TODO getBestPossibleHand(hand)
//		}
//		else {
//			//TODO play +10 using amour, ally, weapon in that order
//		}
		return cardsToPlay;
	}

	public int getBidForTest(Game g) {
		int numToBid = 0;
		//int currentRound = g.getActiveQuest().getTestRound();
		
//		if(currentRound == 1) {
//			numToBid = getNumFoesToDiscard(current.getHand());
//		}
//		else if(currentRound == 2) {
//			//TODO add # duplicates to bid
//			//numToBid += getNumFoesToDiscard(current.getHand()) + getNumDuplicates(current.getHand());
//		}
		
		return numToBid;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest(Player current) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getNumFoesToDiscard(ArrayList<AdventureCard> hand, int maxBattlePoints) {
		int numFoesToDiscard = 0;
		for(AdventureCard c: hand) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) < maxBattlePoints)
				numFoesToDiscard++;
		}
		return numFoesToDiscard;
	}


}
