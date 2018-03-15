package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;

public class AIStrategyOne extends AIPlayer {
	
	public AIStrategyOne(Game g) {
		super();
		game = g;
	}
	
	public boolean doIJoinTournament() {
		//game.acceptDeclineTour(this, false);
		return false;
	}

	public ArrayList<AdventureCard> playCardsForTournament() {
		ArrayList<AdventureCard> cardsForTournament = new ArrayList<AdventureCard>();
		if(canIWinGame()) {
			cardsForTournament = this.getHand().getBestPossibleHand(this.getPlay());
		}
		else {
			cardsForTournament = this.getHand().getDuplicateWeapons();
		}
		return cardsForTournament;
	}

	public boolean doISponsorAQuest() {
		game.declineSponsor();
		return false;
	}

	public ArrayList<AdventureCard> createQuest() {
		ArrayList<AdventureCard> cardsForQuest = new ArrayList<AdventureCard>();
		return cardsForQuest;
	}

	public ArrayList<AdventureCard> doIJoinQuest() {	
		return game.acceptDeclineQuest(this, false);
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		ArrayList<AdventureCard> cardsForStage = new ArrayList<AdventureCard>();
		return cardsForStage;
	}

	public int getBidForTest() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<AdventureCard> getCardsToDiscard(){
		return null;
	}
	
	public void endTurn() {
		
	}
	
	private boolean canIWinGame() {
		StoryCard storyCard = (StoryCard)game.getActiveStoryCard();
		int potentialReward = storyCard.getShieldReward();
		
		//rank 2 is Champion Knight
		return(rank == 2 && getShieldsNeeded() <= potentialReward);	
	}

}
