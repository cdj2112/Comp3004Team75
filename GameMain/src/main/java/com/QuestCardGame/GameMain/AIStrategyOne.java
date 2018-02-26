package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;

public class AIStrategyOne extends Player implements AIPlayerStrategy {

	Game game;
	
	public AIStrategyOne(Game g) {
		super();
		game = g;
	}
	
	public boolean doIJoinTournament() {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AdventureCard> playCardsForTournament() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean doISponsorAQuest() {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<Integer, ArrayList<AdventureCard>> createQuest(QuestCard qc) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean doIJoinQuest(int numStages) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBidForTest() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest() {
		// TODO Auto-generated method stub
		return null;
	}

}
