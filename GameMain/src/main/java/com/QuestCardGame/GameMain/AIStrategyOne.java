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
		int numStages = ((QuestCard)game.getActiveStoryCard()).getStages();
		int numAllies = this.getHand().getNumUniqueCards(AdventureCard.AdventureType.ALLY);
		int numWeapons = this.getHand().getNumUniqueCards(AdventureCard.AdventureType.WEAPON);
		int numFoesToDiscardForTest = this.getHand().getNumFoesToDiscard(20);
		int alliesPerStage = numAllies/numStages;
		int weaponsPerStage = numWeapons/numStages;
		
		if(alliesPerStage >= 2 && weaponsPerStage >= 2 && numFoesToDiscardForTest >= 2)
			return game.acceptDeclineQuest(this, true);
		else
			return game.acceptDeclineQuest(this,  false);
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		ArrayList<AdventureCard> cardsToPlay = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> cardsInPlay = this.getPlay();
		int cardsNeededForStage = 2;
		int currentStage = game.getActiveQuest().getCurrentStageIndex() + 1; //currentStage starts at 0
		int totalStages = game.getActiveQuest().getNumStages();
		
		if(currentStage == totalStages) {
			cardsToPlay = this.getHand().getBestPossibleHand(cardsInPlay);
		}
		else {
			this.getHand().sortDescendingByBattlePoints(); //need strongest allies/amour
			ArrayList<AdventureCard> cards = this.getHand().getUniqueCards(AdventureCard.AdventureType.ALLY, 2);
			cardsToPlay.addAll(cards);
			if(cardsToPlay.size() < cardsNeededForStage && !iHaveAmourInPlay()) {
				cards = this.getHand().getUniqueCards(AdventureCard.AdventureType.AMOURS, 1); //only can play 1 anyway
				cardsToPlay.addAll(cards);
			}
			if(cardsToPlay.size() < cardsNeededForStage) {
				this.getHand().sortAscendingByBattlePoints(); //need weakest weapons
				cards = this.getHand().getUniqueCards(AdventureCard.AdventureType.WEAPON, cardsNeededForStage - cardsToPlay.size());
				cardsToPlay.addAll(cards);
			}
		}		
		return cardsToPlay;
	}

	public int getBidForTest() {
		int numToBid = 0;
		int currentRound = 0; //g.getActiveQuest().getTestRound();
		
		//don't bid unless first round
		if(currentRound == 0) 
			numToBid = 0;
		else	
			numToBid = this.getHand().getNumFoesToDiscard(20);
		
		return numToBid;
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
	
	private boolean iHaveAmourInPlay() {
		for(AdventureCard c : this.getPlay()) {
			if(c.getCardType() == AdventureCard.AdventureType.AMOURS)
				return true;
		}
		return false;
	}

}
