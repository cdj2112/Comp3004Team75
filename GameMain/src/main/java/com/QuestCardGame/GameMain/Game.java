package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

	public static enum GameStatus {
		IDLE, END_TURN_DISCARD, SPONSORING, BUILDING_QUEST, ACCEPTING_QUEST, PLAYING_QUEST, EVAL_QUEST_STAGE, PRE_QUEST_DISCARD,
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
	private int[] toDiscard;
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
		toDiscard = new int[numPlayers];
		initStoryDeck();
		initAdventureDeck();
		for (int p = 0; p < numPlayers; p++) {
			for (int i = 0; i < 12; i++) {
				playerDrawAdventureCard(players[p]);
			}
		}
	}

	Game(HashMap<Integer,ArrayList<AdventureCard>> testDeck, QuestCard testQuest) {
		players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			players[i] = new Player();
		}
		currentStatus = GameStatus.IDLE;
		activePlayer = 0;
		toDiscard = new int[numPlayers];
		initStoryDeck();
		storyDeck.addCard(testQuest);
		initAdventureDeck();
		for (int p = 0; p < numPlayers; p++) {
			if (testDeck.containsKey(p)){
				for(AdventureCard cc : testDeck.get(p)) {
					players[p].drawCard(cc);
				}
			}
			while (players[p].getHand().size() < 12) {
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
		boolean correctCards = true;
		int i = 0;
		for (Player p : players) {
			correctCards = correctCards && p.getHand().size() <= 12;
			toDiscard[i] = Math.max(p.getHand().size() - 12, 0);
			i++;
		}

		if (correctCards) {
			currentStatus = GameStatus.IDLE;
			sponsor = null;
			activeQuest = null;
			playerTurn++;
			activePlayer = playerTurn;
		} else {
			currentStatus = GameStatus.END_TURN_DISCARD;
			for (i = 0; i < numPlayers; i++) {
				int index = (playerTurn + i) % numPlayers;
				if (toDiscard[index] != 0) {
					activePlayer = index;
					break;
				}
			}
		}
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
				playerDrawAdventureCard(p);
			}

			if (p.getHand().size() <= 12) {
				activePlayer = getNextActivePlayer();
			} else {
				currentStatus = GameStatus.PRE_QUEST_DISCARD;
				toDiscard[activePlayer] = p.getHand().size() - 12;
			}

			if (activePlayer == sponsorIndex) {
				currentStatus = GameStatus.PLAYING_QUEST;
				activeQuest.startQuest();
				activeQuest.getNextPlayer();
			}

			return true;
		}
		return false;
	}

	public boolean finalizeQuest() {
		if (currentStatus == GameStatus.BUILDING_QUEST) {
			boolean valid = activeQuest.validateQuest();
			if (valid) {
				currentStatus = GameStatus.ACCEPTING_QUEST;
				activePlayer = getNextActivePlayer();
				return true;
			}else {
				for (AdventureCard c : activeQuest.returnToHand()){
					activePlayer.drawCard(c);
				}
				return false;
			}
		}
	}

	public boolean finalizePlay() {
		Player p = players[getCurrentActivePlayer()];
		if (currentStatus == GameStatus.PLAYING_QUEST && p.getHand().size() <= 12) {
			getNextActiveQuestPlayer();
			return true;
		} else if (p.getHand().size() > 12) {
			return false;
		}
		return true;
	}

	public boolean[] playerPlayCards(Player p, ArrayList<AdventureCard> cards) {
		boolean[] played = new boolean[cards.size()];
		int i = 0;
		for (AdventureCard c : cards) {
			if (!isValidCardPlay(p, c)) {
				played[i] = false;
			} else {
				p.playCard(c);
				played[i] = true;
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

	public void playerDiscardAdventrueCard(Player p, Card c) {
		p.useCard(c);
		adventureDeck.discard(c);
		if (currentStatus == GameStatus.PRE_QUEST_DISCARD || currentStatus == GameStatus.END_TURN_DISCARD) {
			int playerIndex = -1;
			for (int i = 0; i < numPlayers; i++) {
				if (players[i] == p)
					playerIndex = i;
			}
			checkHandSizes();
		}
	}

	private void checkHandSizes() {
		boolean done = true;
		for (int d = 0; d < numPlayers; d++) {
			done = done && players[d].getHand().size() <= 12;
			toDiscard[d] = Math.max(players[d].getHand().size() - 12, 0);
		}
		if (done && currentStatus == GameStatus.PRE_QUEST_DISCARD) {
			activePlayer = getNextActivePlayer();
			if (activePlayer == sponsorIndex) {
				currentStatus = GameStatus.PLAYING_QUEST;
				activeQuest.startQuest();
				activeQuest.getNextPlayer();
			} else {
				currentStatus = GameStatus.ACCEPTING_QUEST;
			}
		} else if (done && currentStatus == GameStatus.END_TURN_DISCARD) {
			endTurn();
		} else if (currentStatus == GameStatus.END_TURN_DISCARD) {
			for (int i = 0; i < numPlayers; i++) {
				int index = (playerTurn + i) % numPlayers;
				if (toDiscard[index] != 0) {
					activePlayer = index;
					break;
				}
			}
		}
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
			else if (currentStatus == GameStatus.EVAL_QUEST_STAGE && !activeQuest.isQuestOver()) {
				currentStatus = GameStatus.PLAYING_QUEST;
				ArrayList<Player> questPlayers = activeQuest.getPlayers();
				for (Player qP : questPlayers) {
					playerDrawAdventureCard(qP);
				}
			} else {
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
			int backToSponsor = activeQuest.getCardsUsed() + activeQuest.getNumStages();
			for (int i = 0; i < backToSponsor; i++) {
				playerDrawAdventureCard(sponsor);
			}
			endTurn();
		}

		return questDiscard;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	private void initStoryDeck() {
		storyDeck = new Deck();

		storyDeck.addCard(new QuestCard("Journey Through the Enchanted Forest", 3, "Evil Knight"));
		storyDeck.addCard(new QuestCard("Vanquish King Arthur's Enemies", 3, null));
		storyDeck.addCard(new QuestCard("Repel the Saxon Raiders", 2, "All Saxons"));
	}

	private void initAdventureDeck() {
		adventureDeck = new Deck();

		for (int i = 0; i < 50; i++) {
			adventureDeck.addCard(new Weapon("Dagger", 5));
		}

		for (int i = 0; i < 50; i++) {
			adventureDeck.addCard(new Foe("Bandit", 10));
		}

		adventureDeck.shuffleDeck();
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

	public int getPlayerDiscard(int i) {
		return toDiscard[i];
	}

	private boolean isValidCardPlay(Player p, AdventureCard c) {
		for (AdventureCard pc : p.getPlay()) {
			if (pc.cardName.equals(c.getName()))
				return false;
		}

		return true;
	}

}
