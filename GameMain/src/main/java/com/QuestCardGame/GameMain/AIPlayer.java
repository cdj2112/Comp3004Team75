package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public abstract class AIPlayer implements AIPlayerStrategy{
	
	Game game;
	Player player;
	
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
			case TEST_BIDDING:
				this.getBidForTest();
				return new ArrayList<AdventureCard>();
			case BID_DISCARD:
				return this.discardAfterWinningTest();
			case EVAL_QUEST_STAGE:
				return this.evalQuestStage();
			case ENTERING_TOUR:
				this.doIJoinTournament();
				return new ArrayList<AdventureCard>();
			case PLAYING_TOUR:
				return this.playCardsForTournament();	
			case PRE_TOUR_DISCARD: 
				return this.getCardsToDiscard();
			case EVAL_TOUR:
				return this.evalTour();
			case END_TURN_DISCARD:
				return this.getCardsToDiscard();
			case IDLE:
				game.playTurn();
				return new ArrayList<AdventureCard>();
			default:
				return new ArrayList<AdventureCard>();
		}
	}
	
	/**
	 * For now just get rid of the lowest BP cards
	 */
	public ArrayList<AdventureCard> getCardsToDiscard(){
		Hand playerHand = player.getHand();
		int numCardsToDiscard = playerHand.size() - 12;
		ArrayList<AdventureCard> cardsToDiscard = new ArrayList<AdventureCard>();
		
		if(numCardsToDiscard <= 0)
			return null;
		
		playerHand.sortAscendingByBattlePoints();

		for(AdventureCard c: playerHand) {
			if(numCardsToDiscard == 0)
				break;
			cardsToDiscard.add(c);
			numCardsToDiscard--;
		}
		
		for(AdventureCard c : cardsToDiscard) {
			game.playerDiscardAdventrueCard(this.player, c);
		}
		return cardsToDiscard;
	}
}
