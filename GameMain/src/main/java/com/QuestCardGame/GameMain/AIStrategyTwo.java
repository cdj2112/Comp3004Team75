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
			cardsToPlay = hand.getBestPossibleHand(this.getPlay());
		
		game.playerPlayCards(this, cardsToPlay);
		
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

	public Map<Integer, ArrayList<AdventureCard>> createQuest() {
		// TODO Auto-generated method stub
		return null;
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
				return null;
			case ACCEPTING_QUEST:
				return this.doIJoinQuest();
			case PRE_QUEST_DISCARD:
				return this.getCardsToDiscard();
			case PLAYING_QUEST:
				return this.playCardsForQuestStage();
//			case ACCEPTING_TOURNAMENT:
//				return this.doIJoinTournament();
//			case PLAYING_TOURNAMENT:
//				return this.playCardsForTournament();				
			case END_TURN_DISCARD:
				return this.getCardsToDiscard();
			default:
				return null;
		}
	}
	
	public void endTurn() {
		previousQuestStageBattlePoints = 0;
	}
}
