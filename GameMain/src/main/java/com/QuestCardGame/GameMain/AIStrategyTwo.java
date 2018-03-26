package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;

public class AIStrategyTwo extends AIPlayer {

	int previousQuestStageBattlePoints;
	
	public AIStrategyTwo(Game g, Player p) {
		super();
		game = g;
		player = p;
		previousQuestStageBattlePoints = 0;
	}
	
	//Always participate
	public boolean doIJoinTournament() {
		//game.acceptDeclineTour(this, true);
		return true;
	}

	//Play 50 BP or best possible hand
	public ArrayList<AdventureCard> playCardsForTournament() {
		Hand cardsToPlay;
		Hand hand =  game.getCurrentActivePlayerObj().getHand();

		cardsToPlay = hand.getCardsForPoints(50);
		if(cardsToPlay == null)
			cardsToPlay = hand.getBestPossibleHand(player.getPlay());
		
		game.playerPlayCards(player, cardsToPlay);
		//game.finalizePlayTour();
		
		return cardsToPlay;
	}

	public boolean doISponsorAQuest() {
		boolean sponsorQuest = true;
		int numStages = ((QuestCard)game.getActiveStoryCard()).getStages();
		
		for(int i = 0; i < game.getNumPlayers() && sponsorQuest; i++) {
			Player p = game.getPlayer(i);
			if(p == this.player) {
				if(!player.getHand().hasCardsToSponsorQuest(numStages))
					sponsorQuest = false;
			}			
			else if(p.getNumShields() + numStages >= p.getShieldsNeeded()) {
				sponsorQuest = false;
			}
		}
		
		if(sponsorQuest)
			game.acceptSponsor();
		else
			game.declineSponsor();
		
		return sponsorQuest;
	}

	public ArrayList<AdventureCard> createQuest() {
		int numStages = ((QuestCard)game.getActiveStoryCard()).getStages();
		int prevStageBattlePoints = 0;
		ArrayList<AdventureCard> allCardsForQuest = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> cardsForStage = new ArrayList<AdventureCard>();
		Hand sponsorHand = player.getHand();
		
		for(int stage = 0; stage < numStages; stage++) {
			cardsForStage = sponsorHand.getCardsForQuestStage(stage, numStages, prevStageBattlePoints + 1);
			prevStageBattlePoints = 0;
			for(AdventureCard c : cardsForStage) {
				game.sponsorAddCardToStage(c, stage);
				prevStageBattlePoints += c.getBattlePoint(false);
			}
			allCardsForQuest.addAll(cardsForStage);
		}
		game.finalizeQuest();
		return allCardsForQuest;
	}

	//Can increase BP each stage by 10 pts AND
	//has 2 foes less than 25 BP
	public ArrayList<AdventureCard> doIJoinQuest() {
		Hand hand = player.getHand();
		int foesToDiscard = hand.getNumFoesToDiscard(25);
		boolean isValidBattlePoints = false;
		int numStages = game.getActiveQuest().getNumStages();

		isValidBattlePoints = hand.hasIncreasingBattlePointsForStages(numStages, 10, player.getPlay());		
		boolean acceptQuest = foesToDiscard >= 2 && isValidBattlePoints;
		
		return game.acceptDeclineQuest(this.player, acceptQuest);
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		Hand cardsToPlay = new Hand();
		int currentStage = game.getActiveQuest().getCurrentStageIndex() + 1; //currentStage starts at 0
		int totalStages = game.getActiveQuest().getNumStages();
		ArrayList<AdventureCard> cardsInPlay = player.getPlay();

		if(currentStage == totalStages) {
			cardsToPlay = player.getHand().getBestPossibleHand(cardsInPlay);
		}
		else {
			cardsToPlay = player.getHand().getHandToPlayForQuestStage(previousQuestStageBattlePoints + 10, cardsInPlay);
		}
		previousQuestStageBattlePoints = player.getBattlePointsForHand(cardsToPlay);
		
		int cP = player.getHand().size() - cardsToPlay.size() - 12;
		player.getHand().sortAscendingByBattlePoints();
		int i = 0; 
		while(cP > 0) {
			AdventureCard c = player.getHand().get(i);
			if(player.getHand().isValidPlay(cardsToPlay, c)) {
				cardsToPlay.add(c);
				cP--;
			} else {
				i++;
			}
		}
		
		game.playerPlayCards(this.player, cardsToPlay);
		game.finalizePlay();
		
		return cardsToPlay;
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
	
	public void endTurn() {
		previousQuestStageBattlePoints = 0;
	}
	
	public boolean isAIPlayer() {
		return true;
	}
}
