package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.ListIterator;

public class Quest {

	private Stage[] stages;
	private ArrayList<Player> players;
	private ListIterator<Player> iter;
	private int currentStage;
	private int totalStages;
	
	Quest(QuestCard qc) {
		stages = new Stage[qc.getStages()];
		totalStages = qc.getStages();
	}
	
	public boolean validateQuest() {
		//int previousBP = -1;
		for(Stage s: stages) {
			/*if(s.getBattlePoints()<=previousBP) {
				return false;
			}
			previousBP = s.getBattlePoints();*/
			if(s.getNumCards()==0) {
				return false;
			}
		}
		return true;
		
	}
	
	public boolean addCardToStage(Card c, int s) {
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
		currentStage = 1;
	}
	
	public Player getNextPlayer() {
		if(iter.hasNext())
			return iter.next();
		else
			return null;
	}
	
	public void eliminateStageLosers() {
		int pointsToBeat = stages[currentStage].getBattlePoints();
		
		for(Player p : players) {
			if(p.getBattlePoints() < pointsToBeat)
				removePlayer(p);
		}
		
		currentStage++;
		
		//reset the current player to the first one
		iter = players.listIterator();
		
		if(currentStage > totalStages) {
			if(players.size() > 0)
				awardQuestWinners();
			endOfQuestCleanup();
		}
	}
	
	private void endOfStageCleanup() {
		//TODO:
		//remove weapons
	}
	
	private void awardQuestWinners() {
		int shieldsToAward = totalStages;
		for(Player p : players)
			p.addShields(shieldsToAward);
	}
	
	private void endOfQuestCleanup() {
		//TODO:
		//remove armour
	}
}
