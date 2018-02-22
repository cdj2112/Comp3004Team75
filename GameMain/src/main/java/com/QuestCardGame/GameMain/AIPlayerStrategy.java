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
	
	boolean doIJoinTournament(Game g);
	
	ArrayList<AdventureCard> playCardsForTournament(Game g);
	
	boolean doISponsorAQuest(Game g);
	
	Map<Integer, ArrayList<AdventureCard>> createQuest(Player current, QuestCard qc);
	
	boolean doIJoinQuest(Player current, int numStages);
	
	ArrayList<AdventureCard> playCardsForQuestStage(Game g);
	
	int getBidForTest(Game g);
	
	ArrayList<AdventureCard> discardAfterWinningTest(Player current);

	
	
	
	
	
	
	
	
	

}
