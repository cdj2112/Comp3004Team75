package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public class Game {

	public static enum GameStatus {
		IDLE, SPONSORING, BUILDING_QUEST, ACCEPTING_QUEST, PLAYING_QUEST, EVAL_QUEST_STAGE
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
	private int sponsorIndex;
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
		for (int p = 0; p < numPlayers; p++) {
			for (int i = 0; i < 12; i++) {
				playerDrawAdventureCard(players[p]);
			}
		}
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
			sponsorIndex = activePlayer; 
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

	public boolean acceptDeclineQuest(Player p, boolean accept) {
		if (currentStatus == GameStatus.ACCEPTING_QUEST) {
			if (accept) {
				activeQuest.addPlayer(players[activePlayer]);
			}

			activePlayer = getNextActivePlayer();

			if (activePlayer == sponsorIndex) {
				currentStatus = GameStatus.PLAYING_QUEST;
				activeQuest.startQuest();
				activeQuest.getNextPlayer();
			}

			return true;
		}
		return false;
	}
	
	public void finalizeQuest() {
		if(currentStatus == GameStatus.BUILDING_QUEST) {
			boolean valid = activeQuest.validateQuest();
			if(valid) {
				currentStatus = GameStatus.ACCEPTING_QUEST;
				activePlayer = getNextActivePlayer();
			}
		}
	}
	
	public void finalizePlay() {
		if(currentStatus == GameStatus.PLAYING_QUEST) {
			getNextActiveQuestPlayer();
		}
	}

	public boolean[] playerPlayCards(Player p, ArrayList<AdventureCard> cards) {
		boolean [] played = new boolean[cards.size()];
		int i = 0;
		for (AdventureCard c : cards) {
			if (!isValidCardPlay(p, c)) {
				played[i]=false;
			} else {
			    p.playCard(c);
			    played[i]=true;
			}
		}
		return played;
	}

	public boolean playerPlayCard(Player p, AdventureCard c) {
		if (!isValidCardPlay(p, c))
			return false;

		p.playCard(c);

		return true;
	}

	public AdventureCard playerDrawAdventureCard(Player p) {

		AdventureCard c = (AdventureCard) adventureDeck.drawCard();

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

	public Player getPlayer(int p) {
		return players[p];
	}

	/**
	 * Return the next player to play cards if there is one The next player then
	 * becomes the current player and can be retrieved anytime using
	 * getCurrentActiveQuestPlayer() Returns null if the round is over
	 * 
	 * This is separate from the game's active player because not all players may be
	 * in a quest
	 */
	public Player getNextActiveQuestPlayer() {
		Player p = activeQuest.getNextPlayer();

		// play has looped a full circle - change status accordingly
		if (p == null) {
			if (currentStatus == GameStatus.PLAYING_QUEST)
				currentStatus = GameStatus.EVAL_QUEST_STAGE;
			else if (currentStatus == GameStatus.EVAL_QUEST_STAGE && !activeQuest.isQuestOver())
				currentStatus = GameStatus.PLAYING_QUEST;
			else {
				currentStatus = GameStatus.IDLE;
				return null;
			}
			return getNextActiveQuestPlayer();
		}
		
		return p;
	}

	/**
	 * Gets the game's active player. If the game is playing, then this is the quest
	 * active player. Otherwise it's the game player.
	 * 
	 * @return int between 0-3 inclusive -1 if there is no active player, i.e. the
	 *         game has done a full circle
	 */
	public int getCurrentActivePlayer() {
		if (currentStatus == GameStatus.PLAYING_QUEST || currentStatus == GameStatus.EVAL_QUEST_STAGE)
			return getCurrentActiveQuestPlayer();
		else
			return activePlayer;
	}

	/**
	 * To get the current active quest player.
	 * 
	 * @return index of the activeQuestPlayer if it exists -1 otherwise
	 */
	private int getCurrentActiveQuestPlayer() {
		Player p = activeQuest.getCurrentPlayer();
		for (int i = 0; i < numPlayers; i++) {
			if (players[i] == p)
				return i;
		}
		return -1;
	}

	/**
	 * Gets the battle points of the specified player
	 * 
	 * @param player:
	 *            0, 1, 2, 3
	 * @return returns battle points of the player if exists -1 otherwise
	 */
	public int getPlayerBattlePoints(int player) {
		if (player < numPlayers && player >= 0)
			return players[player].getBattlePoints();
		return -1;
	}

	/**
	 * Gets the battle points of the current stage
	 * 
	 * @return the battle points of the current stage
	 */
	public int getQuestCurrentStageBattlePoints() {
		return activeQuest.getCurrentStageBattlePoints();
	}

	/**
	 * Determines if a player advances onto the next stage of a quest
	 * 
	 * @param player
	 *            to evaluate
	 * @return true if player wins stage false otherwise
	 */
	public ArrayList<AdventureCard> evaluatePlayerEndOfStage(int player) {
		boolean result = activeQuest.evaluatePlayer(players[player]);

		getNextActiveQuestPlayer();

		ArrayList<AdventureCard> questDiscard = activeQuest.getDiscardPile();
		for (AdventureCard c : questDiscard)
			adventureDeck.discard(c);

		if (activeQuest.isQuestOver()) {
			// TODO: perhaps the game should award winners? for now in quest
			// game should remove any cards left except allies?
			// game should award adventure cards
			// handle max cards, force discard

			endTurn();
		}

		return questDiscard;
	}

	public int getNumPlayers() {
		return numPlayers;
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

	public GameStatus getGameStatus() {
		return currentStatus;
	}

	public Quest getActiveQuest() {
		return activeQuest;
	}

	public int activeStages() {
		if (activeQuest != null) {
			return activeQuest.getNumStages();
		} else {
			return 0;
		}
	}

	private boolean isValidCardPlay(Player p, AdventureCard c) {
		for (AdventureCard pc : p.getPlay()) {
			if (pc.cardName.equals(c.getName()))
				return false;
		}

		return true;
	}

}
