package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;

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
		//game.acceptDeclineTour(this, true);
		return true;
	}

	//Play 50 BP or best possible hand
	public ArrayList<AdventureCard> playCardsForTournament() {
		Hand cardsToPlay;
		Hand hand =  game.getCurrentActivePlayerObj().getHand();

		cardsToPlay = hand.getCardsForPoints(50);
		if(cardsToPlay == null)
			cardsToPlay = hand.getBestPossibleHand(this.getPlay());
		
		game.playerPlayCards(this, cardsToPlay);
		//game.finalizePlayTour();
		
		return cardsToPlay;
	}

	public boolean doISponsorAQuest() {
		boolean sponsorQuest = true;
		int numStages = ((QuestCard)game.getActiveStoryCard()).getStages();
		
		for(int i = 0; i < game.getNumPlayers() && sponsorQuest; i++) {
			Player p = game.getPlayer(i);
			if(p == this) {
				if(!this.getHand().hasCardsToSponsorQuest(numStages))
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
		Hand sponsorHand = this.getHand();
		
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
		Hand hand = this.getHand();
		int foesToDiscard = hand.getNumFoesToDiscard(25);
		boolean isValidBattlePoints = false;
		int numStages = game.getActiveQuest().getNumStages();

		isValidBattlePoints = hand.hasIncreasingBattlePointsForStages(numStages, 10, this.getPlay());		
		boolean acceptQuest = foesToDiscard >= 2 && isValidBattlePoints;
		
		return game.acceptDeclineQuest(this, acceptQuest);
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		Hand cardsToPlay = new Hand();
		int currentStage = game.getActiveQuest().getCurrentStageIndex() + 1; //currentStage starts at 0
		int totalStages = game.getActiveQuest().getNumStages();
		ArrayList<AdventureCard> cardsInPlay = this.getPlay();

		if(currentStage == totalStages) {
			cardsToPlay = this.getHand().getBestPossibleHand(cardsInPlay);
		}
		else {
			cardsToPlay = this.getHand().getHandToPlayForQuestStage(previousQuestStageBattlePoints + 10, cardsInPlay);
		}
		previousQuestStageBattlePoints = this.getBattlePointsForHand(cardsToPlay);
		
		game.playerPlayCards(this, cardsToPlay);
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
			game.playerDiscardAdventrueCard(this, c);
			numCardsToDiscard--;
		}
		return cardsToDiscard;
	}
	
	public ArrayList<AdventureCard> playTurn(){
		Game.GameStatus gameStatus = game.getGameStatus();
		
		switch (gameStatus) {
			case SPONSORING:
				this.doISponsorAQuest();
				return new ArrayList<AdventureCard>();
			case ACCEPTING_QUEST:
				return this.doIJoinQuest();
			case PRE_QUEST_DISCARD:
				return this.getCardsToDiscard();
			case BUILDING_QUEST:
				return this.createQuest();
			case PLAYING_QUEST:
				return this.playCardsForQuestStage();
//			case ACCEPTING_TOUR:
//				return this.doIJoinTournament();
//			case PLAYING_TOUR:
//				return this.playCardsForTournament();	
//			case PRE_TOUR_DISCARD: 
//				return this.getCardsToDiscard();
			case END_TURN_DISCARD:
				return this.getCardsToDiscard();
			case IDLE:
				return new ArrayList<AdventureCard>();
			default:
				return new ArrayList<AdventureCard>();
		}
	}
	
	public void endTurn() {
		previousQuestStageBattlePoints = 0;
	}
}
