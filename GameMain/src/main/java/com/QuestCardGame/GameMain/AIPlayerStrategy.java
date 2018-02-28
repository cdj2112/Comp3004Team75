package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;

public interface AIPlayerStrategy {
	
	// interface proposal for strategy
	//
	// the way i'm envisioning this is the UI will query the ai player for 
	// the cards to play/decision to make, and the player will send back the info.
	// then the flow of control will go through the game as it normally would for 
	// the other players.
	
	boolean doIJoinTournament();
	
	ArrayList<AdventureCard> playCardsForTournament();
	
	boolean doISponsorAQuest();
	
	Map<Integer, ArrayList<AdventureCard>> createQuest();
	
	boolean doIJoinQuest();
	
	ArrayList<AdventureCard> playCardsForQuestStage();
	
	int getBidForTest();
	
	ArrayList<AdventureCard> discardAfterWinningTest();
	
	ArrayList<AdventureCard> getCardsToDiscard();
	
	/**
	 * Called after a full round of a quest, tournament,
	 * event to reset all variables to their beginning values 
	 */
	void endTurn();
	
	
	
	
	
	
	

}
