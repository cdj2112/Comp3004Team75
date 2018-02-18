package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;

public class Quest {

	private Stage[] stages;
	private ArrayList<Player> players;
	private ListIterator<Player> iter;
	private Player currentPlayer;
	private ArrayList<AdventureCard> discardPile;
	private int currentStage;
	private int totalStages;
	private boolean isQuestOver;
	
	Quest(QuestCard qc) {
		stages = new Stage[qc.getStages()];
		
		for(int i = 0; i < qc.getStages(); i++)
			stages[i] = new Stage();
		
		totalStages = qc.getStages();
		discardPile = new ArrayList<AdventureCard>();
		players = new ArrayList<Player>();
		isQuestOver = false;
	}
	
	public boolean validateQuest() {
		int previousBP = -1;
		for(Stage s: stages) {
			if(s.getBattlePoints() <= previousBP) {
				return false;
			}
			previousBP = s.getBattlePoints();
			if(s.getNumCards()==0) {
				return false;
			}
		}
		return true;
		
	}
	
	public boolean addCardToStage(AdventureCard c, int s) {
		stages[s].addCard(c);
		return true;
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
		
		discardPile.clear();
		removeCardsOfType(p, AdventureCard.AdventureType.WEAPON);
				
		if(!playerWins)
			iter.remove();
		
		//currentStage starts at 0
		if(isLastPlayer) {
			currentStage++;
			if(currentStage >= totalStages) {
				isQuestOver = true;
				awardQuestWinners();
			}
		}
		
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
		for(Player p : players)
			p.addShields(shieldsToAward);
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
}
