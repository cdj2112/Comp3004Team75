package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;

public class AIStrategyOne implements AIPlayerStrategy {

	public boolean doIJoinTournament(Game g) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AdventureCard> playCardsForTournament(Game g) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean doISponsorAQuest(Game g) {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<Integer, ArrayList<AdventureCard>> createQuest(Player current, QuestCard qc) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean doIJoinQuest(Player current, int numStages) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AdventureCard> playCardsForQuestStage(Game g) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBidForTest(Game g) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest(Player current) {
		// TODO Auto-generated method stub
		return null;
	}

}
