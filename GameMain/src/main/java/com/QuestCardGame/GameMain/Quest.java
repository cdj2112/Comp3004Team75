package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

import com.QuestCardGame.GameMain.AdventureCard.AdventureType;

import org.apache.logging.log4j.LogManager;

public class Quest {
	private static final Logger logger = LogManager.getLogger(Quest.class);


	private Stage[] stages;
	private ArrayList<Player> players;
	private ListIterator<Player> iter;
	private Player currentPlayer;
	private ArrayList<AdventureCard> discardPile;
	private int currentStage = -1;
	private int totalStages;
	private boolean isQuestOver;
	private int numCardsUsed = 0;
	private int extraReward = 0;
	private String target;
	private String name;
	private int biddingRound = 0;
	private int currentBids = 0;
	private boolean bidMade = false;
	
	private ArrayList<AdventureCard> questStash;


	Quest(QuestCard qc, int e) {
		stages = new Stage[qc.getStages()];
		for(int i =0; i<qc.getStages();i++) {
			stages[i] = new Stage();
		}
		totalStages = qc.getStages();
		discardPile = new ArrayList<AdventureCard>();
		players = new ArrayList<Player>();
		isQuestOver = false;
		extraReward = e;
		target = qc.getTarget();
		name = qc.getName();
		questStash = new ArrayList<AdventureCard>();
		logger.info("Quest {" + qc.getName() +"} started: " + totalStages + " stages.");
	}
	
	public String getTarget() {
		return target;
	}

	public boolean validateQuest() {
		int previousBP = -1;
		int tests = 0;
		for(Stage s: stages) {
			if(s.getIsTest() && tests == 0) {
				tests++;
				continue;
			}
			
			if(s.getBattlePoints(target) <= previousBP) {
				return false;
			}
			previousBP = s.getBattlePoints(target);
			if(s.getNumFoes() != 1) {
				return false;
			}
		}
		return true;

	}
	
	public boolean addCardToStage(AdventureCard newCard, int s) {
		if(newCard.getCardType() == AdventureType.TEST) {
			for(Stage st : stages) {
				if(st.getIsTest()) return false;
			}
		}
		boolean isAdded = stages[s].addCard(newCard);
		if(isAdded)
			numCardsUsed++;
		return isAdded;
	}

	public boolean addPlayer(Player p) {
		return players.add(p);
	}

	public boolean removePlayer(Player p) {
		return players.remove(p);
	}

	public void startQuest() {
		iter = players.listIterator();
		currentStage = 0;
	}

	public Player getNextPlayer() {
		if(iter.hasNext()) {
			currentPlayer = iter.next();
			return currentPlayer;
		}
		else {
			currentPlayer = null;
			iter = players.listIterator(); //reset to beginning
			if(currentStage >= 0 && currentStage < totalStages && stages[currentStage].getIsTest()){
				biddingRound++;
			}
			if(players.size() == 0) {
				logger.info("Quest no longer has any participants and is over");
				isQuestOver = true;
				clearQuest();
			}
			return null;
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean hasPlayer(Player p) {
		return players.contains(p);
	}

	public int getCurrentStageIndex() {
		return currentStage;
	}

	public int getCurrentStageBattlePoints() {
		return stages[currentStage].getBattlePoints(target);
	}

	/**
	 * Determines whether the player advances to the next
	 * stage of the quest, or wins the quest if it is the
	 * last stage
	 * @param 	p - the player to evaluate
	 * @return 	true if the player wins
	 * 			false otherwise
	 */

	public boolean evaluatePlayer(Player p) {
		int pointsToBeat = stages[currentStage].getBattlePoints(target);
		boolean playerWins = p.getBattlePoints(name) >= pointsToBeat;
		boolean isLastPlayer = (players.indexOf(p) == players.size() - 1);

		if(!playerWins) {
			logger.info("Player "+p.getPlayerNumber()+" loses stage "+p.getBattlePoints(name)+" BP to "+pointsToBeat+" BP");
			iter.remove();
		} else {
			logger.info("Player "+p.getPlayerNumber()+" wins stage "+p.getBattlePoints(name)+" BP to "+pointsToBeat+" BP");
		}
		
		discardPile.clear();
		removeCardsOfType(p, AdventureCard.AdventureType.WEAPON);

		//currentStage starts at 0
		if(isLastPlayer && players.size()!=0) {
			currentStage++;
			if(currentStage >= totalStages) {
				isQuestOver = true;
				awardQuestWinners();
			}
		} else if(players.size()==0) {
			logger.info("All players have dropped out or been eliminated");
			isQuestOver = true;
			clearQuest();
		}

		if(currentStage == totalStages) removeCardsOfType(p, AdventureCard.AdventureType.AMOURS);
		return playerWins;
	}
	
	public void startBidding() {
		biddingRound = 0;
		currentBids = stages[currentStage].getMinBid() - 1; //Bids to beat is one less than min bids
		if(players.size() == 1 && currentBids < 2) currentBids = 2; //If only one person have min of 3 bids
	}
	
	public boolean makeBid(int b) {
		if(b > currentBids) {
			currentBids = b;
			bidMade = true;
			return true;
		}
		return false;
	}
	
	public void playerDropout() {
		iter.remove();
	}
	
	public void closeBidding() {
		currentBids = 0;
		biddingRound = 0;
		currentStage++;
		if(currentStage >= totalStages) {
			isQuestOver = true;
			awardQuestWinners();
		}
	}

	public boolean isQuestOver() {
		return isQuestOver;
	}

	public ArrayList<AdventureCard> getDiscardPile(){
		return discardPile;
	}

	private void awardQuestWinners() {
		int shieldsToAward = totalStages + extraReward;
		for(Player p : players) {
			p.addShields(shieldsToAward);
		}
		clearQuest();
	}

	private void removeCardsOfType(Player p, AdventureCard.AdventureType t) {

		ArrayList<AdventureCard> playerHand = p.getPlay();

		for(Iterator<AdventureCard> it = playerHand.iterator(); it.hasNext();) {
			AdventureCard c = it.next();

			if(c.getCardType() == t) {
				discardPile.add(c);
				it.remove();
			}
		}
	}

	public int getNumStages() {
		return totalStages;
	}

	public Stage[] getStages() {
		return stages;
	}

	public int getCardsUsed() {
		return numCardsUsed;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public boolean isPlayingTest() {
		return stages[currentStage].getIsTest();
	}
	
	public int getBids() {
		return currentBids;
	}
	
	public void clearQuest() {
		for(Stage s: stages) {
			ArrayList<AdventureCard> cards = s.getCards();
			for(AdventureCard c : cards) {
				discardPile.add(c);
			}
		}
	}
	
	public boolean bidMade() {
		return bidMade;
	}
	
	public int getBiddingRound() {
		return biddingRound;
	}
	
	public void addToStash(AdventureCard c) {
		questStash.add(c);
	}
	
	public ArrayList<AdventureCard> getStash(){
		return questStash;
	}
	
	public void clearStash() {
		questStash.clear();
	}
}
