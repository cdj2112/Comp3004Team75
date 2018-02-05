package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public class Game {

	public static enum GameStatus {
		IDLE, SPONSORING, BUILDING_QUEST, ACCEPTING_QUEST, PLAYING_QUEST
	};

	private GameStatus currentStatus;

	// Persistant Variables
	private Player[] players;
	private int numPlayers = 4;
	private Deck storyDeck;
	private Deck adventureDeck;
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
	}

	public void playTurn() {
		Card storyCard = getStoryCard();
		if (storyCard instanceof QuestCard) {
			Quest activeQuest = new Quest((QuestCard) storyCard);
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

	public boolean sponsorAddCardToStage(Card c, int s) {
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
			
			if(activePlayer == playerTurn)
				currentStatus = GameStatus.PLAYING_QUEST;
			
			return true;
		}
		return false;		
	}
	
	public boolean playerPlayCards(Player p, ArrayList<Card> cards) {		
		if(!isValidCardPlay(cards))
			return false;
		
		for(Card c: cards){
			p.playCard(c);
		}			
		return true;
	}

	public void playerDrawAdventureCard(Player p) {
		Card c = adventureDeck.drawCard();

		if (c != null)
			p.drawCard(c);
	}

	public Card getStoryCard() {
		Card c = storyDeck.drawCard();

		if (c != null)
			return c;
		else
			return null;
	}

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
		return activeQuest.getNextPlayer();
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
			adventureDeck.addCard(new WeaponCard("Dagger", 5));
		}
	}
	
	private int getNextActivePlayer() {
		return (activePlayer + 1) % numPlayers;
	}
	
	private boolean isValidCardPlay(ArrayList<Card> cards) {
		//check for same weapon type
		
		//check for more than 1 armour
		
		//for now assume all plays are valid
		return true;
	}

}
