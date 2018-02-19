package com.QuestCardGame.GameMain;

import java.util.ArrayList;

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
	
	boolean doISponsorAQuest(Player current, ArrayList<Player> allPlayers);
	
	boolean doIJoinQuest(Player current, ArrayList<Player> allPlayers);
	
	int getBid(int currentBid, int currentRound, Player current);
	
	ArrayList<AdventureCard> discardAfterWinningTest(Player current);
	
	
	
	
	
	
	

}
