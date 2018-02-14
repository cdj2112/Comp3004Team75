package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {

	public static enum GameStatus {
		IDLE, SPONSORING, BUILDING_QUEST, ACCEPTING_QUEST, PLAYING_QUEST, EVAL_QUEST
	};

	private GameStatus currentStatus;

	// Persistant Variables
	private Player[] players;
	private int numPlayers = 4;
	private Deck storyDeck;
	private Deck adventureDeck;
	private Deck discardPile;
	private int playerTurn;

	// Turn Variables
	private int activePlayer;
	// Quests
	private Player sponsor;
	private Quest activeQuest;

	Game() {
		players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			players[i] = new Player();
		}
		currentStatus = GameStatus.IDLE;
		activePlayer = 0;
		initStoryDeck();
		initAdventureDeck();
		playTurn();
		discardPile = new Deck();
	}

	public void playTurn() {
		Card storyCard = getStoryCard();
		if (storyCard instanceof QuestCard) {
			activeQuest = new Quest((QuestCard) storyCard);
			currentStatus = GameStatus.SPONSORING;
		}
	}

	public void endTurn() {
		currentStatus = GameStatus.IDLE;
		sponsor = null;
		activeQuest = null;
		playerTurn++;
		activePlayer = playerTurn;
	}

	public void acceptSponsor() {
		if (currentStatus == GameStatus.SPONSORING) {
			sponsor = players[activePlayer];
			currentStatus = GameStatus.BUILDING_QUEST;
		}
	}

	public void declineSponsor() {
		if (currentStatus == GameStatus.SPONSORING) {
			activePlayer = (activePlayer + 1) % numPlayers;
			if (activePlayer == playerTurn) {
				endTurn();
			}
		}
	}

	public boolean sponsorAddCardToStage(AdventureCard c, int s) {
		if (currentStatus == GameStatus.BUILDING_QUEST) {
			sponsor.useCard(c);
			activeQuest.addCardToStage(c, s);
			return true;
		}
		return false;
	}
	
	public boolean acceptQuest(Player p) {
		if(currentStatus == GameStatus.ACCEPTING_QUEST) {
			activeQuest.addPlayer(players[activePlayer]);
			activePlayer = getNextActivePlayer();
			
			if(activePlayer == playerTurn) {
				currentStatus = GameStatus.PLAYING_QUEST;
				activeQuest.startQuest();
			}
			
			return true;
		}
		return false;		
	}
	
	public boolean playerPlayCard(Player p, AdventureCard c) {
		if(!isValidCardPlay(p, c))
			return false;
		
		p.playCard(c);
		
		return true;
	}
	
	public AdventureCard playerDrawAdventureCard(Player p) {

		AdventureCard c = (AdventureCard)adventureDeck.drawCard();

		if (c != null)
			p.drawCard(c);
		
		return c;
	}

	public Card getStoryCard() {
		Card c = storyDeck.drawCard();

		if (c != null)
			return c;
		else
			return null;

	}

	/**
	 * don't use. will be removed.
	 * @return
	 */
	public Player getPlayer() {
		return players[0];
	}
	
	/**
	 * Return the next player to play cards if there is one
	 * Returns null if the round is over
	 * 
	 * This is separate from the game's active player because not 
	 * all players may be in a quest
	 */
	public Player getNextActiveQuestPlayer() {
		Player p = activeQuest.getNextPlayer();
		
//		if(p == null)
//			currentStatus = GameStatus.BETWEEN_STAGES;
//		else
//			currentStatus = GameStatus.PLAYING_QUEST;
		
		return p;
	}
	
	public int getCurrentActivePlayer() {
		return activePlayer;
	}
	
	/**
	 * To get the current active quest player for the UI
	 * @return index of the activeQuestPlayer if it exists
	 *		   -1 otherwise
	 */
	public int getCurrentActiveQuestPlayer() {
		Player p = activeQuest.getCurrentPlayer();
		for(int i = 0; i < numPlayers; i++) {
			if(players[i] == p)
				return i;
		}
		return -1;
	}
	
	public ArrayList<AdventureCard> evaluateEndOfStage() {
		ArrayList<AdventureCard> discard = activeQuest.eliminateStageLosers();
		for(AdventureCard c : discard)
			discardPile.addCard(c);
		
		if(activeQuest.isQuestOver()) {
			currentStatus = GameStatus.IDLE;
			//TODO: perhaps the game should award winners?
			//		game should remove any cards left except allies?
			//		game should award adventure cards
		}
		
		return discard;
	}

	private void initStoryDeck() {
		storyDeck = new Deck();

		storyDeck.addCard(new QuestCard("Journey Through the Enchanted Forest", 3));
		storyDeck.addCard(new QuestCard("Vanquish King Arthur's Enemies", 3));
		storyDeck.addCard(new QuestCard("Repel the Saxon Raiders", 2));
	}

	private void initAdventureDeck() {
		adventureDeck = new Deck();

		for (int i = 0; i < 6; i++) {
			adventureDeck.addCard(new Weapon("Dagger", 5));
		}
	}
	
	private int getNextActivePlayer() {
		return (activePlayer + 1) % numPlayers;
	}
	
	private boolean isValidCardPlay(Player p, AdventureCard c) {
		for(AdventureCard pc : p.getPlay()) {
			if(pc.cardName.equals(c.getName()))
				return false;
		}
		
		return true;
	}
	

}
