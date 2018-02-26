package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;

public class AIStrategyTwo extends Player implements AIPlayerStrategy {

	int previousQuestStageBattlePoints;
	
	public AIStrategyTwo() {
		super();
		previousQuestStageBattlePoints = 0;
	}
	
	//Always participate
	public boolean doIJoinTournament(Game g) {
		return true;
	}

	//Play 50 BP or best possible hand
	public ArrayList<AdventureCard> playCardsForTournament(Game g) {
		Hand cardsToPlay;
		Hand hand =  g.getCurrentActivePlayerObj().getHand();

		cardsToPlay = hand.getCardsForPoints(50);
		if(cardsToPlay == null)
			cardsToPlay = hand.getBestPossibleHand();
		
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
		Hand h = current.getHand();
		int foesToDiscard = h.getNumFoesToDiscard(25);
		boolean isValidBattlePoints = false;

		isValidBattlePoints = h.hasIncreasingBattlePointsForStages(numStages, 10);
		
		return  foesToDiscard >= 2 && isValidBattlePoints;
	}

	public ArrayList<AdventureCard> playCardsForQuestStage(Game g) {
		ArrayList<AdventureCard> cardsToPlay = new ArrayList<AdventureCard>();
		int currentStage = g.getActiveQuest().getCurrentStageIndex() + 1; //currentStage starts at 0
		int totalStages = g.getActiveQuest().getNumStages();

		if(currentStage == totalStages) {
			cardsToPlay = this.getHand().getBestPossibleHand();
		}
		else {
			cardsToPlay = this.getHand().getHandToPlayForQuestStage(previousQuestStageBattlePoints + 10);
		}
		previousQuestStageBattlePoints = this.getBattlePointsForHand((Hand)cardsToPlay);
		
		return cardsToPlay.size() > 0 ? cardsToPlay : null;
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
}
