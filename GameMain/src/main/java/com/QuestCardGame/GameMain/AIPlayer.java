package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Set;

import com.QuestCardGame.GameMain.AdventureCard.AdventureType;

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
			case EVENT_EXECUTE:
				game.executeEvent();
				return new ArrayList<AdventureCard>();
			case EVENT_DISCARD:
				return this.getSpecialDiscardCards();
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
	
	public ArrayList<AdventureCard> getSpecialDiscardCards(){
		ArrayList<AdventureCard> cardsToDiscard = new ArrayList<AdventureCard>();
		HashMap<AdventureType, Integer> map = game.getSpecialDiscard()[game.getPlayerIndex(player)];
		
		Hand playerHand = player.getHand();
		playerHand.sortAscendingByBattlePoints();
		
		Set<AdventureType> types = map.keySet();
		for(AdventureType t : types) {
			int toDiscard = map.get(t);
			ListIterator<AdventureCard> iter = playerHand.listIterator();
			while(toDiscard > 0) {
				AdventureCard c = iter.next();
				if(c.getCardType() == t) {
					cardsToDiscard.add(c);
					toDiscard--;
				}
			}
		}
		
		
		for(AdventureCard c : cardsToDiscard) {
			game.playerDiscardAdventrueCard(this.player, c);
		}
		return cardsToDiscard;
	}
}
