package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Quest {
	private static final Logger logger = LogManager.getLogger(Quest.class);


	private Stage[] stages;
	private ArrayList<Player> players;
	private ListIterator<Player> iter;
	private Player currentPlayer;
	private ArrayList<AdventureCard> discardPile;
	private int currentStage;
	private int totalStages;
	private boolean isQuestOver;
	private int numCardsUsed = 0;

	Quest(QuestCard qc) {
		stages = new Stage[qc.getStages()];
		for(int i =0; i<qc.getStages();i++) {
			stages[i] = new Stage();
		}
		totalStages = qc.getStages();
		discardPile = new ArrayList<AdventureCard>();
		players = new ArrayList<Player>();
		isQuestOver = false;
		logger.info("Quest {" + qc.getName() +"} started: " + totalStages + " stages.");
	}

	public boolean validateQuest() {
		int previousBP = -1;
		for(Stage s: stages) {
			if(s.getBattlePoints() <= previousBP) {
				return false;
			}
			previousBP = s.getBattlePoints();
			if(s.getNumFoes() != 1) {
				return false;
			}
		}
		return true;

	}
	
	public boolean addCardToStage(AdventureCard newCard, int s) {	
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
		return stages[currentStage].getBattlePoints();
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
		int pointsToBeat = stages[currentStage].getBattlePoints();
		boolean playerWins = p.getBattlePoints() >= pointsToBeat;
		boolean isLastPlayer = (players.indexOf(p) == players.size() - 1);

		if(!playerWins) {
			logger.info("Player "+p.getPlayerNumber()+" looses stage "+p.getBattlePoints()+" BP to "+pointsToBeat+" BP");
			iter.remove();
		} else {
			logger.info("Player "+p.getPlayerNumber()+" wins stage "+p.getBattlePoints()+" BP to "+pointsToBeat+" BP");
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

	public boolean isQuestOver() {
		return isQuestOver;
	}

	public ArrayList<AdventureCard> getDiscardPile(){
		return discardPile;
	}

	private void awardQuestWinners() {
		int shieldsToAward = totalStages;
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
	
	public void clearQuest() {
		for(Stage s: stages) {
			ArrayList<AdventureCard> cards = s.getCards();
			for(AdventureCard c : cards) {
				discardPile.add(c);
			}
		}
	}
}
