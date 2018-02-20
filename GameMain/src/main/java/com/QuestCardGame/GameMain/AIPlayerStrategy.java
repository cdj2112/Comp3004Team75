package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;

public interface AIPlayerStrategy {
	
	// interface proposal for strategy
	//
	// need player objects to decide if the current player can evolve/win
	// and to check if anyone else in the game can evolve/win right now.
	// 
	// maybe we don't need the entire player objects, and it's probably bad design
	// but this leaves the door open for advanced strategies which examine
	// the cards each player has played on the table, their current rank etc.
	
	boolean doIJoinTournament(Player current, ArrayList<Player> allPlayers);
	
	ArrayList<AdventureCard> playCardsForTournament(Player current);
	
	boolean doISponsorAQuest(Player current, ArrayList<Player> allPlayers);
	
	Map<Integer, ArrayList<AdventureCard>> createQuest(Player current, QuestCard qc);
	
	boolean doIJoinQuest(Player current, int numStages);
	
	ArrayList<AdventureCard> playCardsForQuestStage(Player current, int currentStage, int totalStages);
	
	int getBidForTest(int currentBid, int currentRound, Player current);
	
	ArrayList<AdventureCard> discardAfterWinningTest(Player current);

	
	
	
	
	
	
	
	
	

}
