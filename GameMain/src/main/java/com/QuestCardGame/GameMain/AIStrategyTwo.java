package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;

public class AIStrategyTwo extends Player implements AIPlayerStrategy {

	int previousQuestStageBattlePoints;
	Game game;
	
	public AIStrategyTwo(Game g) {
		super();
		game = g;
		previousQuestStageBattlePoints = 0;
	}
	
	//Always participate
	public boolean doIJoinTournament() {
		return true;
	}

	//Play 50 BP or best possible hand
	public ArrayList<AdventureCard> playCardsForTournament() {
		Hand cardsToPlay;
		Hand hand =  game.getCurrentActivePlayerObj().getHand();

		cardsToPlay = hand.getCardsForPoints(50);
		if(cardsToPlay == null)
			cardsToPlay = hand.getBestPossibleHand();
		
		return cardsToPlay;
	}

	public boolean doISponsorAQuest() {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<Integer, ArrayList<AdventureCard>> createQuest(QuestCard qc) {
		// TODO Auto-generated method stub
		return null;
	}

	//Can increase BP each stage by 10 pts AND
	//has 2 foes less than 25 BP
	public boolean doIJoinQuest(int numStages) {
		Hand h = this.getHand();
		int foesToDiscard = h.getNumFoesToDiscard(25);
		boolean isValidBattlePoints = false;

		isValidBattlePoints = h.hasIncreasingBattlePointsForStages(numStages, 10);
		
		return  foesToDiscard >= 2 && isValidBattlePoints;
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		Hand cardsToPlay = new Hand();
		int currentStage = game.getActiveQuest().getCurrentStageIndex() + 1; //currentStage starts at 0
		int totalStages = game.getActiveQuest().getNumStages();

		if(currentStage == totalStages) {
			cardsToPlay = this.getHand().getBestPossibleHand();
		}
		else {
			cardsToPlay = this.getHand().getHandToPlayForQuestStage(previousQuestStageBattlePoints + 10);
		}
		previousQuestStageBattlePoints = this.getBattlePointsForHand(cardsToPlay);
		
		return cardsToPlay.size() > 0 ? cardsToPlay : null;
	}

	public int getBidForTest() {
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

	public ArrayList<AdventureCard> discardAfterWinningTest() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * For now just get rid of the lowest BP cards
	 */
	public ArrayList<AdventureCard> getCardsToDiscard(){
		Hand playerHand = this.getHand();
		int numCardsToDiscard = playerHand.size() - 12;
		ArrayList<AdventureCard> cardsToDiscard = new ArrayList<AdventureCard>();
		
		if(numCardsToDiscard <= 0)
			return null;
		
		playerHand.sortAscendingByBattlePoints();
		
		for(AdventureCard c: playerHand) {
			if(numCardsToDiscard == 0)
				break;
			cardsToDiscard.add(c);
			numCardsToDiscard--;
		}
		return cardsToDiscard;
	}
	
	public void endTurn() {
		previousQuestStageBattlePoints = 0;
	}
	
	
}
