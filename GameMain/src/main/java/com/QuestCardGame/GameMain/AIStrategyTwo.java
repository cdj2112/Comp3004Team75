package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class AIStrategyTwo extends AIPlayer {

	int previousQuestStageBattlePoints;
	private static final Logger logger = LogManager.getLogger("AILogger");
	
	public AIStrategyTwo(Game g, Player p) {
		super();
		game = g;
		player = p;
		previousQuestStageBattlePoints = 0;
	}
	
	//Always participate
	public boolean doIJoinTournament() {
		game.acceptDeclineTour(player, true);
		logger.info("AI strategy [TWO] always enters a tournament");
		logger.info("AI Player " + player.getPlayerNumber() + " with strategy [TWO] ENTERED the tournament");
		return true;
	}

	//Play 50 BP or best possible hand
	public ArrayList<AdventureCard> playCardsForTournament() {
		Hand cardsToPlay;
		Hand hand =  game.getCurrentActivePlayerObj().getHand();
		int totalBattlePoints = 0;

		logger.info("AI Player " + player.getPlayerNumber() + " with strategy [TWO] will try to play a hand with >= 50 pts.");
		cardsToPlay = hand.getCardsForPoints(50);
		if(cardsToPlay == null) {
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [TWO] failed to make a 50 pt hand. Will now play best possible hand.");
			cardsToPlay = hand.getBestPossibleHand(player.getPlay());
		}
		
		for(AdventureCard c: cardsToPlay) {
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [TWO] played [" + c.getName() + "] for tournament.");
			totalBattlePoints += c.getBattlePoint(false);
		}
		logger.info("AI Player " + player.getPlayerNumber() + " with strategy [TWO] played [" + totalBattlePoints + "] battle points for tournament");
		game.playerPlayCards(player, cardsToPlay);
		game.finalizePlayTour();
		
		return cardsToPlay;
	}
	
	public ArrayList<AdventureCard> evalTour() {
		return game.EvalTour();
	}

	public boolean doISponsorAQuest() {
		boolean sponsorQuest = true;
		QuestCard quest = ((QuestCard)game.getActiveStoryCard());
		int numStages = quest.getStages();
		
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
		
		if(sponsorQuest) {
			game.acceptSponsor();
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [TWO] SPONSORED the quest [" + quest.getName() + "]");
		}
		else {
			game.declineSponsor();
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [TWO] did NOT SPONSOR the quest [" + quest.getName() + "]");
		}
		return sponsorQuest;
	}

	public ArrayList<AdventureCard> createQuest() {
		int numStages = ((QuestCard)game.getActiveStoryCard()).getStages();
		int prevStageBattlePoints = 0;
		ArrayList<AdventureCard> allCardsForQuest = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> cardsForStage = new ArrayList<AdventureCard>();
		
		for(int stage = 0; stage < numStages; stage++) {
			cardsForStage = getCardsForQuestStage(stage, numStages, prevStageBattlePoints + 1);
			prevStageBattlePoints = 0;
			for(AdventureCard c : cardsForStage) {
				game.sponsorAddCardToStage(c, stage);
				prevStageBattlePoints += c.getBattlePoint(false);
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] added [" + c.getName() + "] to stage [" + stage + "]");
			}
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] played total battle points [" + prevStageBattlePoints + "] for stage [" + stage + "]");
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
		QuestCard activeQuest = ((QuestCard)game.getActiveStoryCard());
		int numStages = activeQuest.getStages();

		isValidBattlePoints = hand.hasIncreasingBattlePointsForStages(numStages, 10, player.getPlay());		
		boolean acceptQuest = foesToDiscard >= 2 && isValidBattlePoints;
		
		if(acceptQuest) {
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] ACCEPTED quest [" + activeQuest.getName() + "]");
		}
		else {
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] DECLINED quest [" + activeQuest.getName() + "]");

		}
		return game.acceptDeclineQuest(this.player, acceptQuest);
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		Hand cardsToPlay = new Hand();
		int currentStage = game.getActiveQuest().getCurrentStageIndex() + 1; //currentStage starts at 0
		int totalStages = game.getActiveQuest().getNumStages();
		ArrayList<AdventureCard> cardsInPlay = player.getPlay();

		if(currentStage == totalStages) {
			cardsToPlay = player.getHand().getBestPossibleHand(cardsInPlay);
			logger.info("Final quest stage. AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] will play best hand.");
		}
		else {
			logger.info("Not final quest stage. AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] will play 10 higher than previous stage of [" + previousQuestStageBattlePoints + "]");
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
		
		for(AdventureCard c: cardsToPlay)
			logger.info("Not final quest stage. AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] played [" + c.getName() + "]");
		logger.info("Not final quest stage. AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] played battle point total [" + previousQuestStageBattlePoints + "]"); //previous is updated above so this is okay
	
		game.playerPlayCards(this.player, cardsToPlay);
		game.finalizePlay();
		
		return cardsToPlay;
	}

	public int getBidForTest() {
		int numToBid = 0;
		int currentTestRound = game.getActiveQuest().getBiddingRound();
		String storyCard = game.getActiveStoryCard().getName();
		Hand playerHand = player.getHand();
		
		logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] has cards:" + buildCardListForLogger(player.getHand()));
		
		if(currentTestRound == 0) {
			numToBid = playerHand.getNumFoesToDiscard(25) + player.getBids(storyCard);
			int currentBid = game.getActiveQuest().getBids();
			logger.info("First round of test. AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] will bid number of foes < 25 battle points.");
			if(numToBid > currentBid) {
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] bid [" + numToBid + "]");
				game.placeBid(numToBid);
			} else {
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] bid is too low and will drop out");
				game.playerDropOut();
			}
		}
		else if(currentTestRound == 1) {
			ArrayList<AdventureCard> canDiscard = new ArrayList<AdventureCard>();
			for(AdventureCard c : player.getHand()) {
				if(c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) <= 25)
					canDiscard.add(c);
			}
			numToBid = canDiscard.size() + playerHand.getNumDuplicates(canDiscard) + player.getBids(storyCard);
			int currentBid = game.getActiveQuest().getBids();
			logger.info("Second round of test. AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] will bid number of foes < 25 and number of duplicate cards.");
			if(numToBid > currentBid) {
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] bid [" + numToBid + "]");
				game.placeBid(numToBid);
			} else {
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] bid is too low and will drop out");
				game.playerDropOut();
			}
		} else {
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] will drop out in third round");
			game.playerDropOut();
		}
		
		return numToBid;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest() {
		int bid = game.getActiveQuest().getBids();
		
		ArrayList<AdventureCard> cardsToDiscard = new ArrayList<AdventureCard>();
		
		//foes 25 and below are discarded for the first round
		for(AdventureCard c : player.getHand()) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) <= 25)
				cardsToDiscard.add(c);
		}
		
		//if we played two rounds, also discard the duplicates
		if(bid - cardsToDiscard.size() > 0) {
			cardsToDiscard.addAll(player.getHand().getDuplicateCards(cardsToDiscard));
		}
		
		logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] won the test.");

		for(AdventureCard c : cardsToDiscard) {
			game.playerDiscardAdventrueCard(player, c);
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [TWO] discarded [" + c.getName() + "]");
		}	
		return cardsToDiscard;
	}
	
	public ArrayList<AdventureCard> evalQuestStage() {
		int i = game.getPlayerIndex(player);
		return game.evaluatePlayerEndOfStage(i);
	}
	
	public void endTurn() {
		previousQuestStageBattlePoints = 0;
	}
	
	public boolean isAIPlayer() {
		return true;
	}
	
	public ArrayList<AdventureCard> getCardsForQuestStage(int stage, int totalStages, int requiredBattlePoints){
		ArrayList<AdventureCard> cardsForStage = new ArrayList<AdventureCard>();
		Hand playerHand = player.getHand();
		//second last stage should be a test if possible
		if(totalStages - stage + 1 == 1) {
			AdventureCard test = playerHand.getTestCard();
			if(test != null)
				cardsForStage.add(test);
		}
		else if(stage + 1 == totalStages) {
			AdventureCard foe = playerHand.getStrongestFoe();
			if(foe != null)
				cardsForStage.add(foe);
			for(AdventureCard c : playerHand) {
				if(playerHand.isValidForQuestStage(c, cardsForStage))
					cardsForStage.add(c);
			}
		}
		else {
			AdventureCard foe = playerHand.getFoe(requiredBattlePoints);
			if(foe != null)
				cardsForStage.add(foe);
		}	
		return cardsForStage;
	}
}
