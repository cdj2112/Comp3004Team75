package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;

public class AIStrategyOne extends AIPlayer {
	
	public AIStrategyOne(Game g) {
		super();
		game = g;
	}
	
	public boolean doIJoinTournament() {
		boolean joinTournament = false;
		if(aPlayerCanWinGame(true) || aPlayerCanEvolve(true)) {
			joinTournament = true;
			//game.acceptTournament();
		}
		else {
			joinTournament = false;
			//game.declineTournament();
		}
		return joinTournament;
	}

	//TODO need to add condition for another player in tournament to win/evolve to play best hand
	public ArrayList<AdventureCard> playCardsForTournament() {
		ArrayList<AdventureCard> cardsForTournament = new ArrayList<AdventureCard>();
		if(canIWinGame()) {
			cardsForTournament = this.getHand().getBestPossibleHand(this.getPlay());
		}
		else {
			cardsForTournament = this.getHand().getDuplicateWeapons();
		}
		game.playerPlayCards(this, cardsForTournament);
		return cardsForTournament;
	}

	public boolean doISponsorAQuest() {
		int numStages = ((QuestCard)game.getActiveStoryCard()).getStages(); 
		boolean hasCardsToSponsor = this.getHand().hasCardsToSponsorQuest(numStages);
		
		//has to have cards, and nobody else can win/evolve		
		if(hasCardsToSponsor && !(aPlayerCanWinGame(false) || aPlayerCanEvolve(false))) { 
			game.acceptSponsor();
			return true;
		}
		else {
			game.declineSponsor();
			return false;
		}	
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
	
	private boolean aPlayerCanWinGame(boolean includingMyself) {
		StoryCard storyCard = (StoryCard)game.getActiveStoryCard();
		int potentialReward = storyCard.getShieldReward();
		boolean aPlayerCanWin = false;
		
		for(int i = 0; i < game.getNumPlayers(); i++) {
			Player p = game.getPlayer(i);
			if(p == this && !includingMyself)
				continue;
			if(p.getRankName().equals("Champion Knight") && p.getShieldsNeeded() <= potentialReward)
				aPlayerCanWin = true;				
		}
		return aPlayerCanWin;
	}
	
	private boolean aPlayerCanEvolve(boolean includingMyself) {
		StoryCard storyCard = (StoryCard)game.getActiveStoryCard();
		int potentialReward = storyCard.getShieldReward();
		boolean aPlayerCanEvolve = false;
		
		for(int i = 0; i < game.getNumPlayers(); i++) {
			Player p = game.getPlayer(i);
			if(p == this && !includingMyself)
				continue;
			if(p.getShieldsNeeded() <= potentialReward)
				aPlayerCanEvolve = true;				
		}
		return aPlayerCanEvolve;
	}

}
