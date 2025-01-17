package com.QuestCardGame.GameMain;

import java.util.*;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.SystemPropertiesPropertySource;

import com.QuestCardGame.GameMain.AdventureCard.AdventureType;

public class Game {

	private static final Logger logger = LogManager.getLogger(Game.class);

	public static enum GameStatus {
		IDLE, END_TURN_DISCARD, 
		SPONSORING, BUILDING_QUEST, ACCEPTING_QUEST, PLAYING_QUEST, TEST_BIDDING, BID_DISCARD, EVAL_QUEST_STAGE, PRE_QUEST_DISCARD,
		ENTERING_TOUR, PLAYING_TOUR, PRE_TOUR_DISCARD, EVAL_TOUR,
		EVENT_EXECUTE, EVENT_DISCARD
	};

	private GameStatus currentStatus;

	// Persistant Variables
	private Player[] players;
	private int numPlayers = 4;
	private Deck storyDeck;
	private Deck adventureDeck;
	private int playerTurn;
	private Card currentStoryCard;

	// Turn Variables
	private int activePlayer;
	private int[] toDiscard;
	private HashMap<AdventureType, Integer>[] specialDiscard;
	// Quests
	private Player sponsor;
	private int sponsorIndex;
	private Quest activeQuest;

	// tour
	private Tournaments activeTournaments;

	// Events
	private int extraShield = 0;

	public Game(int nP, int[] aiStrat, boolean rigged) {
		numPlayers = nP;
		players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			// temporary fix to keep this working with java fx UI.
			// will just need int[] behaviour instead of nP/nAIP
			players[i] = new Player(this, aiStrat[i]);
		}
		currentStatus = GameStatus.IDLE;
		activePlayer = 0;
		toDiscard = new int[numPlayers];
		
		specialDiscard = new HashMap[numPlayers];
		for(int i = 0; i<numPlayers; i++) {
			specialDiscard[i] = new HashMap<AdventureType, Integer>();
		}
		currentStoryCard = null;
		if (!rigged) {
			initStoryDeck();
			initAdventureDeck();
		} else {
			initRiggedStoryDeck();
			initRiggedAdventureDeck();
		}
		for (int p = 0; p < numPlayers; p++) {
			for (int i = 0; i < 12; i++) {
				playerDrawAdventureCard(players[p]);
			}
		}
	}

	public void playTurn() {
		Card storyCard = getStoryCard();
		currentStoryCard = storyCard;
		if (storyCard instanceof QuestCard) {
			activeQuest = new Quest((QuestCard) storyCard, extraShield);
			extraShield = 0;
			currentStatus = GameStatus.SPONSORING;
		}else if (storyCard instanceof EventCard){
			//EventFactory event = new EventFactory((EventCard)storyCard, this);
			currentStatus =  GameStatus.EVENT_EXECUTE;
			checkHandSizes();
		} else if (storyCard instanceof TournamentCard) {
			activeTournaments = new Tournaments((TournamentCard) storyCard);
			currentStatus = GameStatus.ENTERING_TOUR;
		}
	}

	public void endTurn() {
		boolean correctCards = true;
		int i = 0;
		for (Player p : players) {
			correctCards = correctCards && p.getHand().size() <= 12;
			toDiscard[i] = Math.max(0, p.getHand().size() - 12);
			i++;
		}

		sponsor = null;
		activeQuest = null;
		activeTournaments = null;
		if (currentStoryCard != null) {
			logger.info("Story Card " + currentStoryCard.getName() + ": Discarded");
		}
		storyDeck.discard(currentStoryCard);
		currentStoryCard = null;

		if (correctCards) {
			currentStatus = GameStatus.IDLE;
			playerTurn = (playerTurn + 1) % numPlayers;
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

	// tournament*************************************************************************
	public ArrayList<AdventureCard> acceptDeclineTour(Player p, boolean accept) {
		if (currentStatus == GameStatus.ENTERING_TOUR) {
			if (accept) {
				logger.info("Player " + players[activePlayer].getPlayerNumber() + ": Entered Tournaments");
				activeTournaments.addPlayer(players[activePlayer]);
				playerDrawAdventureCard(p);
			} else {
				// logger.info("Player " + players[activePlayer].getPlayerNumber() + ": Did Not
				// Entered Tournaments");
			}

			if (p.getHand().size() <= 12) {
				activePlayer = getNextActivePlayer();
				shouldStartTournament();
			} else {
				currentStatus = GameStatus.PRE_TOUR_DISCARD;
				toDiscard[activePlayer] = p.getHand().size() - 12;
			}

		}
		return null;
	}

	public boolean finalizePlayTour() {
		Player p = players[getCurrentActivePlayer()];

		if (currentStatus == GameStatus.PLAYING_TOUR && p.getHand().size() <= 12) {
			getNextActiveTourPlayer();
			logger.info("Player " + p.getPlayerNumber() + ": Finished Playing for Tournament");
			return true;
		} else if (p.getHand().size() > 12) {
			return false;
		}
		return true;
	}

	/*
	 * public ArrayList<AdventureCard> EvalTour() {
	 * activeTournaments.evaluatePlayers(activeTournaments.getPlayers());
	 * ArrayList<AdventureCard> TourDiscard = activeTournaments.getDiscardPile();
	 * for (AdventureCard c : TourDiscard) adventureDeck.discard(c);
	 * 
	 * return TourDiscard; }
	 */

	public boolean shouldStartTournament() {
		if (activePlayer == playerTurn) {
			if (activeTournaments.getPlayers().size() > 1) {
				logger.info("Begin Tournament");
				currentStatus = GameStatus.PLAYING_TOUR;
				activeTournaments.startTournaments();
				activeTournaments.getNextPlayer();
			} else if (activeTournaments.getPlayers().size() == 1) {
				logger.info("Player " + activeTournaments.getPlayers().get(0).getPlayerNumber()
						+ " is the only participant in tournament and wins by default");
				activeTournaments.getPlayers().get(0).addShields(1 + activeTournaments.getBonus());
				endTurn();
			} else {
				logger.info("Tournament Has No Participants");
				endTurn();
			}
			return true;
		}
		return false;
	}

	public ArrayList<AdventureCard> EvalTour() {
		activeTournaments.evaluatePlayers();

		ArrayList<AdventureCard> tourDiscard = activeTournaments.getDiscardPile();
		for (AdventureCard c : tourDiscard) {
			logger.info("Card " + c.getName() + ": Discarded");
			adventureDeck.discard(c);
		}

		if (activeTournaments.isTournamentsOver()) {
			endTurn();
		} else {
			currentStatus = GameStatus.PLAYING_TOUR;
		}

		return tourDiscard;
	}
	// ***************************************************

	public void acceptSponsor() {
		if (currentStatus == GameStatus.SPONSORING) {
			sponsor = players[activePlayer];
			sponsorIndex = activePlayer;
			currentStatus = GameStatus.BUILDING_QUEST;
			logger.info("Player " + sponsor.getPlayerNumber() + ": Sponsored Quest");
		}
	}

	public void declineSponsor() {
		if (currentStatus == GameStatus.SPONSORING) {
			logger.info("Player " + getCurrentActivePlayerObj().getPlayerNumber() + ": Declined Sponsored Quest");
			activePlayer = (activePlayer + 1) % numPlayers;
			if (activePlayer == playerTurn) {
				endTurn();
			}
		}
	}

	public boolean sponsorAddCardToStage(AdventureCard c, int s) {
		if (currentStatus == GameStatus.BUILDING_QUEST) {
			boolean played = activeQuest.addCardToStage(c, s);
			boolean bonus = activeQuest.getTarget() != null && c.getName().contains(activeQuest.getTarget());		
				
			if (played) {
				logger.info("Player " + sponsor.getPlayerNumber() + ": Played Card " + c.getName() + " to Stage " + s);
				sponsor.useCard(c);
			}
			if(played && bonus)
				logger.info("Card [" + c.getName() +"] will use bonus battle points of [" + c.getBattlePoint(bonus) + "]");
			return played;
		}
		return false;
	}

	public ArrayList<AdventureCard> acceptDeclineQuest(Player p, boolean accept) {
		if (currentStatus == GameStatus.ACCEPTING_QUEST) {
			if (accept) {
				logger.info("Player " + players[activePlayer].getPlayerNumber() + ": Entered Quest");
				activeQuest.addPlayer(players[activePlayer]);
				playerDrawAdventureCard(p);
			} else {
				logger.info("Player " + players[activePlayer].getPlayerNumber() + ": Did Not Entered Quest");
			}

			if (p.getHand().size() <= 12) {
				activePlayer = getNextActivePlayer();
			} else {
				currentStatus = GameStatus.PRE_QUEST_DISCARD;
				toDiscard[activePlayer] = p.getHand().size() - 12;
			}

			if (activePlayer == sponsorIndex) {
				if (activeQuest.getPlayers().size() > 0) {
					logger.info("Quest Begin");
					activeQuest.startQuest();
					activeQuest.getNextPlayer();
					currentStatus = activeQuest.isPlayingTest() ? GameStatus.TEST_BIDDING : GameStatus.PLAYING_QUEST;
					if(activeQuest.isPlayingTest()) activeQuest.startBidding();
				} else {
					logger.info("Quest Has No Participants");
					int backToSponsor = activeQuest.getCardsUsed() + activeQuest.getNumStages();
					for (int i = 0; i < backToSponsor; i++) {
						playerDrawAdventureCard(sponsor);
					}
					activeQuest.clearQuest();
					ArrayList<AdventureCard> questDiscard = activeQuest.getDiscardPile();
					for (AdventureCard c : questDiscard) {
						logger.info("Card " + c.getName() + ": Discarded");
						adventureDeck.discard(c);
					}
					endTurn();
					return questDiscard;
				}
			}
		}
		return null;
	}

	public void finalizeQuest() {
		if (currentStatus == GameStatus.BUILDING_QUEST) {
			boolean valid = activeQuest.validateQuest();
			if (valid) {
				logger.info("Player " + sponsor.getPlayerNumber() + ": Finshed Quest");
				currentStatus = GameStatus.ACCEPTING_QUEST;
				activePlayer = getNextActivePlayer();
			}
		}
	}

	public boolean finalizePlay() {
		Player p = players[getCurrentActivePlayer()];
		if (currentStatus == GameStatus.PLAYING_QUEST && p.getHand().size() <= 12) {
			getNextActiveQuestPlayer();
			logger.info("Player " + p.getPlayerNumber() + ": Finished Playing for Stage");
			return true;
		} else if (p.getHand().size() > 12) {
			return false;
		}
		return true;
	}

	public void placeBid(int bid) {
		Player p = getCurrentActivePlayerObj();
		String questName = currentStoryCard.getName();
		
		int maxBids = p.getBids(questName) + p.getHand().size();
		if (bid > maxBids)
			return;
		boolean accepted = activeQuest.makeBid(bid);
		if (accepted) {
			logger.info("Player " + p.getPlayerNumber() +": Bid "+bid+" cards");
			getNextActiveQuestPlayer();
		}
	}

	public void playerDropOut() {
		Player p = getCurrentActivePlayerObj();
		logger.info("Player " + p.getPlayerNumber() +": Droped out of Quest");
		activeQuest.playerDropout();
		getNextActiveQuestPlayer();
	}

	public boolean[] playerPlayCards(Player p, ArrayList<AdventureCard> cards) {
		boolean[] played = new boolean[cards.size()];
		int i = 0;
		for (AdventureCard c : cards) {
			if (!isValidCardPlay(p, c)) {
				played[i] = false;
			} else {
				p.playCard(c);
				if(currentStatus == GameStatus.END_TURN_DISCARD || currentStatus == GameStatus.PRE_QUEST_DISCARD || currentStatus == GameStatus.PRE_TOUR_DISCARD) {
					checkHandSizes();
				}
				if (activeTournaments != null)
					activeTournaments.addToStash(c);
				if(currentStatus == GameStatus.PLAYING_QUEST)
					activeQuest.addToStash(c);
				played[i] = true;
			}
		}
		return played;
	}

	public boolean playerPlayCard(Player p, AdventureCard c) {
		if (!isValidCardPlay(p, c))
			return false;

		if (activeTournaments != null)
			activeTournaments.addToStash(c);
		if(currentStatus == GameStatus.PLAYING_QUEST)
			activeQuest.addToStash(c);
		p.playCard(c);
		if(currentStatus == GameStatus.END_TURN_DISCARD || currentStatus == GameStatus.PRE_QUEST_DISCARD || currentStatus == GameStatus.PRE_TOUR_DISCARD) {
			checkHandSizes();
		}

		return true;
	}

	public AdventureCard playerDrawAdventureCard(Player p) {

		AdventureCard c = (AdventureCard) adventureDeck.drawCard();

		if (c != null)
			p.drawCard(c);

		return c;
	}

	public void playerDiscardAdventrueCard(Player p, Card c) {
		if (currentStatus == GameStatus.PRE_QUEST_DISCARD || currentStatus == GameStatus.END_TURN_DISCARD
				|| currentStatus == GameStatus.PRE_TOUR_DISCARD) {
			p.useCard(c);
			adventureDeck.discard(c);
			logger.info("Player " + p.getPlayerNumber() + ": DISCARD [" + c.getName() + "]");
			checkHandSizes();
		} else if (currentStatus == GameStatus.BID_DISCARD) {
			int playerIndex = -1;
			for (int i = 0; i < numPlayers; i++) {
				if (players[i] == p)
					playerIndex = i;
			}
			toDiscard[playerIndex] = Math.max(0, toDiscard[playerIndex] - 1);
			boolean done = true;
			for (int d : toDiscard) {
				done = done && d == 0;
			}
			if (done) {
				activeQuest.closeBidding();
				getNextActiveQuestPlayer();
			}
		} else if (currentStatus == GameStatus.EVENT_DISCARD) {
			int idx = getPlayerIndex(p);
			HashMap<AdventureType, Integer> discardMap = specialDiscard[idx];
			AdventureType at = ((AdventureCard)c).getCardType();
			if(discardMap.keySet().contains(at)) {
				p.useCard(c);
				adventureDeck.discard(c);
				logger.info("Player " + p.getPlayerNumber() + ": DISCARD [" + c.getName() + "]");
				int tD = discardMap.get(at);
				if(tD > 1) {
					discardMap.put(at, tD - 1);
				} else {
					discardMap.remove(at);
				}
				checkSpecialDiscard();
			}
		} else {
			p.useCard(c);
			adventureDeck.discard(c);
			logger.info("Player " + p.getPlayerNumber() + ": DISCARD [" + c.getName() + "]");
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
				activeQuest.startQuest();
				activeQuest.getNextPlayer();
				currentStatus = activeQuest.isPlayingTest() ? GameStatus.TEST_BIDDING : GameStatus.PLAYING_QUEST;
				if(activeQuest.isPlayingTest()) activeQuest.startBidding();
			} else {
				currentStatus = GameStatus.ACCEPTING_QUEST;
			}
		} else if (done && currentStatus == GameStatus.PRE_TOUR_DISCARD) {
			activePlayer = getNextActivePlayer();
			boolean start = shouldStartTournament();
			if (!start)
				currentStatus = GameStatus.ENTERING_TOUR;
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
		String questName = currentStoryCard.getName();

		// play has looped a full circle - change status accordingly
		if (currentStatus == GameStatus.TEST_BIDDING && activeQuest.getPlayers().size() == 1 && activeQuest.bidMade()) {
			currentStatus = GameStatus.BID_DISCARD;
			if (p == null)
				p = activeQuest.getNextPlayer();
			logger.info("Player "+p.getPlayerNumber()+": Won test");
			for (int i = 0; i < numPlayers; i++) {
				if (players[i] == p && activeQuest.getBids() > p.getBids(questName)) {
					toDiscard[i] = activeQuest.getBids() - p.getBids(questName);
				} else if (players[i] == p) {
					currentStatus = GameStatus.PLAYING_QUEST;
					activeQuest.closeBidding();
				}
			}
		} else if (p == null && !activeQuest.isQuestOver()) {
			if (currentStatus == GameStatus.PLAYING_QUEST) {
				currentStatus = GameStatus.EVAL_QUEST_STAGE;
				activeQuest.clearStash();
			} else if (currentStatus == GameStatus.EVAL_QUEST_STAGE && !activeQuest.isQuestOver()) {
				currentStatus = activeQuest.isPlayingTest() ? GameStatus.TEST_BIDDING : GameStatus.PLAYING_QUEST;
				ArrayList<Player> questPlayers = activeQuest.getPlayers();
				for (Player qP : questPlayers) {
					playerDrawAdventureCard(qP);
				}
			} else if (currentStatus == GameStatus.BID_DISCARD) {
				currentStatus = GameStatus.PLAYING_QUEST;
			}
			return getNextActiveQuestPlayer();
		} else if (activeQuest.isQuestOver()) {
			// TODO: perhaps the game should award winners? for now in quest
			// game should remove any cards left except allies?
			// game should award adventure cards
			// handle max cards, force discard
			int backToSponsor = activeQuest.getCardsUsed() + activeQuest.getNumStages();
			for (int i = 0; i < backToSponsor; i++) {
				playerDrawAdventureCard(sponsor);
			}
			endTurn();
			return null;
		}

		return p;
	}

	// tour*************
	public Player getNextActiveTourPlayer() {
		Player p = activeTournaments.getNextPlayer();
		if (p == null && !activeTournaments.isTournamentsOver()) {
			if (currentStatus == GameStatus.PLAYING_TOUR) {
				currentStatus = GameStatus.EVAL_TOUR;
			} else {
				endTurn();
				currentStatus = GameStatus.IDLE;
				return null;
			}
			return getNextActiveTourPlayer();
		}

		return p;
	}

	private int getCurrentActiveTourPlayer() {
		Player p = activeTournaments.getCurrentPlayer();
		for (int i = 0; i < numPlayers; i++) {
			if (players[i] == p)
				return i;
		}
		return -1;
	}

	// ***********************

	/**
	 * Gets the game's active player. If the game is playing, then this is the quest
	 * active player. Otherwise it's the game player.
	 * 
	 * @return int between 0-3 inclusive -1 if there is no active player, i.e. the
	 *         game has done a full circle
	 */
	public int getCurrentActivePlayer() {
		if (currentStatus == GameStatus.PLAYING_QUEST || currentStatus == GameStatus.EVAL_QUEST_STAGE
				|| currentStatus == GameStatus.TEST_BIDDING || currentStatus == GameStatus.BID_DISCARD) {
			return getCurrentActiveQuestPlayer();
		} else if (currentStatus == GameStatus.PLAYING_TOUR) {
			return getCurrentActiveTourPlayer();
		} else {
			return activePlayer;
		}
	}

	public Player getCurrentActivePlayerObj() {
		return players[getCurrentActivePlayer()];
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
		if (player < numPlayers && player >= 0) {
			String currentStoryName = currentStoryCard == null ? null : currentStoryCard.getName();
			return players[player].getBattlePoints(currentStoryName);
		}
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
		activeQuest.evaluatePlayer(players[player]);

		ArrayList<AdventureCard> questDiscard = activeQuest.getDiscardPile();
		for (AdventureCard c : questDiscard) {
			logger.info("Card " + c.getName() + ": Discarded");
			adventureDeck.discard(c);
		}

		getNextActiveQuestPlayer();

		/*
		 * if (activeQuest.isQuestOver()) { // TODO: perhaps the game should award
		 * winners? for now in quest // game should remove any cards left except allies?
		 * // game should award adventure cards // handle max cards, force discard int
		 * backToSponsor = activeQuest.getCardsUsed() + activeQuest.getNumStages(); for
		 * (int i = 0; i < backToSponsor; i++) { playerDrawAdventureCard(sponsor); }
		 * endTurn(); }
		 */

		return questDiscard;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	private void initStoryDeck() {
		storyDeck = new Deck();

		try {
			CardList.populateStoryCards(storyDeck, this);
		} catch (Exception e) {
			System.out.println(e);
		}
		logger.info("Story Deck Created");
		storyDeck.shuffleDeck();
	}

	private void initAdventureDeck() {
		adventureDeck = new Deck();

		try {
			CardList.populateAdventureCards(adventureDeck, this);
		} catch (Exception e) {
			System.out.println(e);
		}
		logger.info("Adventure Deck Created");
		adventureDeck.shuffleDeck();
	}

	private void initRiggedAdventureDeck() {
		adventureDeck = new Deck();
		logger.info("Rigged Adventure Deck Created");
		CardList.populateRiggedAdventureCards(adventureDeck, this);
	}

	private void initRiggedStoryDeck() {
		storyDeck = new Deck();
		try {
			logger.info("Rigged Story Deck Created");
			CardList.populateRiggedStoryCards(storyDeck, this);
		} catch (Exception e) {
			System.out.println(e);
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

	public int getSponsorIndex() {
		return getPlayerIndex(sponsor);
	}

	public int getPlayerIndex(Player p) {
		if (p == null) {
			return -1;
		}
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p)
				return i;
		}
		return -1;
	}

	public Tournaments getActiveTournament() {
		return activeTournaments;
	}

	public int activeStages() {
		if (activeQuest != null) {
			return activeQuest.getNumStages();
		} else {
			return 0;
		}
	}

	public ArrayList<AdventureCard> getTournamentStash() {
		if (activeTournaments != null) {
			return activeTournaments.getStash();
		}
		return null;
	}

	public int getPlayerDiscard(int i) {
		return toDiscard[i];
	}

	public int[] getAllDiscard() {
		return toDiscard;
	}

	private boolean isValidCardPlay(Player p, AdventureCard c) {
		for (AdventureCard pc : p.getPlay()) {
			if (pc.cardName.equals(c.getName()))
				return false;
		}

		boolean alliesOnly = !(currentStatus == GameStatus.PLAYING_QUEST || currentStatus == GameStatus.PLAYING_TOUR);
		
		if (c.getCardType() == AdventureType.FOE || c.getCardType() == AdventureType.TEST) {
			return false;
		} else if (alliesOnly && c.getCardType() != AdventureType.ALLY) {
			return false;
		}

		return true;
	}

	public Card getActiveStoryCard() {
		return currentStoryCard;
	}

//Event Functions
	public int getExtraShield() {return extraShield;}
	public void setExtraShield(int e) {
		logger.info("Next Quest is worth "+e+" extra shields");
		extraShield += e;
	}
	public void clearAllAllies(){
		logger.info("All allies in play are discarded");
		for (int i = 0; i < numPlayers; i++){
			for (AdventureCard c: players[i].getPlay()){
				if (c.getCardType() == AdventureCard.AdventureType.ALLY) 
					playerDiscardAdventrueCard(players[i],(AdventureCard) c);
			}
		}
	}
	public void setSpecialDiscard(int p, AdventureType type, int amount) {
		logger.info("Player "+p+": Must discard "+amount+" "+type+" card"+(amount==1?"":"s"));
		specialDiscard[p].put(type, amount);
	}
	public HashMap<AdventureType, Integer>[] getSpecialDiscard(){
		return specialDiscard;
	}
	public void executeEvent() {
		if(currentStatus == GameStatus.EVENT_EXECUTE) {
			logger.info("Event card "+currentStoryCard.getName()+": Executes");
			boolean discard = ((EventCard)currentStoryCard).execute();
			if(!discard) {
				endTurn();
			} else {
				currentStatus = GameStatus.EVENT_DISCARD;
				checkSpecialDiscard();
			}
		}
	}
	private void checkSpecialDiscard() {
		for(int i=0; i<numPlayers; i++) {
			Set<AdventureType> keys = specialDiscard[i].keySet();
			for(AdventureType k : keys) {
				if(specialDiscard[i].get(k)==0) specialDiscard[i].remove(k);
			}
			if(specialDiscard[i].keySet().size()>0) return;
		}
		endTurn();
	}
}
