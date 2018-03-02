package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public abstract class AIPlayer extends Player implements AIPlayerStrategy{

	Game game;
	
	public ArrayList<AdventureCard> playTurn(){
		Game.GameStatus gameStatus = game.getGameStatus();
		
		switch (gameStatus) {
			case SPONSORING:
				this.doISponsorAQuest();
				return new ArrayList<AdventureCard>();
			case ACCEPTING_QUEST:
				return this.doIJoinQuest();
			case PRE_QUEST_DISCARD:
				return this.getCardsToDiscard();
			case BUILDING_QUEST:
				return this.createQuest();
			case PLAYING_QUEST:
				return this.playCardsForQuestStage();
//			case ACCEPTING_TOUR:
//				return this.doIJoinTournament();
//			case PLAYING_TOUR:
//				return this.playCardsForTournament();	
//			case PRE_TOUR_DISCARD: 
//				return this.getCardsToDiscard();
			case END_TURN_DISCARD:
				return this.getCardsToDiscard();
			case IDLE:
				return new ArrayList<AdventureCard>();
			default:
				return new ArrayList<AdventureCard>();
		}
	}
}
