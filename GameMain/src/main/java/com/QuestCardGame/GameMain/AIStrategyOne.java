package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class AIStrategyOne extends AIPlayer {
	
	private static final Logger logger = LogManager.getLogger("AILogger");
	
	public AIStrategyOne(Game g, Player p) {
		super();
		player = p;
		game = g;
	}
	
	public boolean doIJoinTournament() {
		boolean joinTournament = false;
		if(aPlayerCanWinGame(true) || aPlayerCanEvolve(true)) {
			joinTournament = true;
			//game.acceptTournament();
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] ENTERED the tournament");
		}
		else {
			joinTournament = false;
			//game.declineTournament();
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] did NOT ENTER the tournament");
		}
		return joinTournament;
	}

	//TODO need to add condition for another player in tournament to win/evolve to play best hand
	public ArrayList<AdventureCard> playCardsForTournament() {
		ArrayList<AdventureCard> cardsForTournament = new ArrayList<AdventureCard>();
		if(canIWinGame()) {
			cardsForTournament = player.getHand().getBestPossibleHand(player.getPlay());
		}
		else {
			cardsForTournament = player.getHand().getDuplicateWeapons();
		}
		game.playerPlayCards(player, cardsForTournament);
		return cardsForTournament;
	}

	public boolean doISponsorAQuest() {
		QuestCard quest = ((QuestCard)game.getActiveStoryCard());
		int numStages = quest.getStages(); 
		boolean hasCardsToSponsor = player.getHand().hasCardsToSponsorQuest(numStages);
		
		//has to have cards, and nobody else can win/evolve		
		if(hasCardsToSponsor && !(aPlayerCanWinGame(false) || aPlayerCanEvolve(false))) { 
			game.acceptSponsor();
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] SPONSORED the quest [" + quest.getName() + "]");
			return true;
		}
		else {
			game.declineSponsor();
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] did NOT SPONSOR the quest [" + quest.getName() + "]");
			return false;
		}	
	}
	
	//last stage 50
	//second last test (if possible)
	//strongest foe with any weapon i have duplicates of 
	public ArrayList<AdventureCard> createQuest() {
		ArrayList<AdventureCard> cardsForQuest = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> cardsForStage = new ArrayList<AdventureCard>();			
		int numStages = game.getActiveQuest().getNumStages();

		//intentionally going backwards due to requirements
		for(int i = numStages-1; i >= 0; i--) {
			cardsForStage = getCardsForQuestStage(i, numStages);
			for(AdventureCard c : cardsForStage) {
				game.sponsorAddCardToStage(c, i);
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] added [" + c.getName() + "] to stage [" + i + "]");
			}
			cardsForQuest.addAll(cardsForStage);
		}
		
		game.finalizeQuest();
		return cardsForQuest;
	}

	/**
	 * Requirement states allies/weapons per stage >= 2
	 * I will assume "/" means OR, not AND.
	 */
	public ArrayList<AdventureCard> doIJoinQuest() {
		QuestCard activeQuest = ((QuestCard)game.getActiveStoryCard());
		int numStages = activeQuest.getStages();
		int numAllies = player.getHand().getNumUniqueCards(AdventureCard.AdventureType.ALLY);
		int numWeapons = player.getHand().getNumUniqueCards(AdventureCard.AdventureType.WEAPON);
		int numFoesToDiscardForTest = player.getHand().getNumFoesToDiscard(20);
		int alliesPerStage = numAllies/numStages;
		int weaponsPerStage = numWeapons/numStages;
		
		if((alliesPerStage >= 2 || weaponsPerStage >= 2) && numFoesToDiscardForTest >= 2) {
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] ACCEPTED quest [" + activeQuest.getName() + "]");
			return game.acceptDeclineQuest(player, true);
		}
		else {
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] DECLINED quest [" + activeQuest.getName() + "]");
			return game.acceptDeclineQuest(player,  false);		
		}
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		ArrayList<AdventureCard> cardsToPlay = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> cardsInPlay = player.getPlay();
		int cardsNeededForStage = 2;
		int currentStage = game.getActiveQuest().getCurrentStageIndex() + 1; //currentStage starts at 0
		int totalStages = game.getActiveQuest().getNumStages();
		
		if(currentStage == totalStages) {
			cardsToPlay = player.getHand().getBestPossibleHand(cardsInPlay);
		}
		else {
			player.getHand().sortDescendingByBattlePoints(); //need strongest allies/amour
			ArrayList<AdventureCard> cards = player.getHand().getUniqueCards(AdventureCard.AdventureType.ALLY, 2);
			cardsToPlay.addAll(cards);
			if(cardsToPlay.size() < cardsNeededForStage && !iHaveAmourInPlay()) {
				cards = player.getHand().getUniqueCards(AdventureCard.AdventureType.AMOURS, 1); //only can play 1 anyway
				cardsToPlay.addAll(cards);
			}
			if(cardsToPlay.size() < cardsNeededForStage) {
				player.getHand().sortAscendingByBattlePoints(); //need weakest weapons
				cards = player.getHand().getUniqueCards(AdventureCard.AdventureType.WEAPON, cardsNeededForStage - cardsToPlay.size());
				cardsToPlay.addAll(cards);
			}
		}		
		game.playerPlayCards(player, cardsToPlay);
		game.finalizePlay();
		return cardsToPlay;
	}

	public int getBidForTest() {
		int numToBid = 0;
		int currentRound = 0; //g.getActiveQuest().getTestRound();
		
		//don't bid unless first round
		if(currentRound == 0) {
			numToBid = player.getHand().getNumFoesToDiscard(20);
		}
		else {
			numToBid = 0;
		}
					
		return numToBid;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest() {
		Hand hand = player.getHand();
		ArrayList<AdventureCard> cardsToDiscard = new ArrayList<AdventureCard>();
		for(AdventureCard c : hand) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) <= 20) {
				cardsToDiscard.add(c);
				game.playerDiscardAdventrueCard(player, c);
			}
		}
		
		return cardsToDiscard;
	}
	
	public void endTurn() {
		
	}
	
	private boolean canIWinGame() {
		StoryCard storyCard = (StoryCard)game.getActiveStoryCard();
		int potentialReward = storyCard.getShieldReward();
		
		//rank 2 is Champion Knight
		return(player.rank == 2 && player.getShieldsNeeded() <= potentialReward);	
	}
	
	private boolean aPlayerCanWinGame(boolean includingMyself) {
		StoryCard storyCard = (StoryCard)game.getActiveStoryCard();
		int potentialReward = storyCard.getShieldReward();
		boolean aPlayerCanWin = false;
		
		for(int i = 0; i < game.getNumPlayers(); i++) {
			Player p = game.getPlayer(i);
			if(p == this.player && !includingMyself)
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
			if(p == this.player && !includingMyself)
				continue;
			if(p.getShieldsNeeded() <= potentialReward)
				aPlayerCanEvolve = true;				
		}
		return aPlayerCanEvolve;
	}
	
	private boolean iHaveAmourInPlay() {
		for(AdventureCard c : player.getPlay()) {
			if(c.getCardType() == AdventureCard.AdventureType.AMOURS)
				return true;
		}
		return false;
	}
	
	private ArrayList<AdventureCard> getCardsForQuestStage(int stage, int totalStages){
		player.getHand().sortDescendingByBattlePoints();
		AdventureCard strongestFoe = player.getHand().getStrongestFoe();
		ArrayList<AdventureCard> cardsForStage = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> weaponsInHand = player.getHand().getUniqueCards(AdventureCard.AdventureType.WEAPON, 12);
		ArrayList<AdventureCard> duplicateWeapons = player.getHand().getDuplicateWeapons();
		duplicateWeapons.sort(new BattlePointComparatorDescending());
		int stageBattlePoints = 0;
		
		
		//last stage - need 50 pts
		if(stage + 1 == totalStages) {
			stageBattlePoints += strongestFoe.getBattlePoint(false);
			cardsForStage.add(strongestFoe);
			for(AdventureCard c : weaponsInHand) {
				stageBattlePoints += c.getBattlePoint(false);
				cardsForStage.add(c);
				if(stageBattlePoints >= 50)
					break;
			}
		} //second last stage, test if possible
		else if (stage == totalStages) {
			AdventureCard test = player.getHand().getTestCard();
			if(test != null)
				cardsForStage.add(test);
			else {
				cardsForStage.add(strongestFoe);
				if(duplicateWeapons.size() > 0)
					cardsForStage.add(duplicateWeapons.get(0));
			}
		}
		else {
			cardsForStage.add(strongestFoe);
			if(duplicateWeapons.size() > 0)
				cardsForStage.add(duplicateWeapons.get(0));
		}
		return cardsForStage;
	}
	
	public boolean isAIPlayer() {
		return true;
	}

}
