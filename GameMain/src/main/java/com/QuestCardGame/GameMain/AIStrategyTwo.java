package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;

public class AIStrategyTwo implements AIPlayerStrategy {

	public boolean doIJoinTournament(Player current, ArrayList<Player> allPlayers) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AdventureCard> playCardsForTournament(Player current) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean doISponsorAQuest(Player current, ArrayList<Player> allPlayers) {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<Integer, ArrayList<AdventureCard>> createQuest(Player current, QuestCard qc) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean doIJoinQuest(Player current, ArrayList<Player> allPlayers) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AdventureCard> playCardsForQuestStage(Player current, int currentStage, int totalStages) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBidForTest(int currentBid, int currentRound, Player current) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest(Player current) {
		// TODO Auto-generated method stub
		return null;
	}

}
