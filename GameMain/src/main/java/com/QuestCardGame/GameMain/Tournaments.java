package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Tournaments {
	private static final Logger logger = LogManager.getLogger(Tournaments.class);
	
	private ArrayList<Player> players;
	private ListIterator<Player> iter;
	private Player currentPlayer;
	private ArrayList<AdventureCard> discardPile;
	private boolean isTournamentsOver;
	private int bp;
	private int bonus;
	
	private ArrayList<AdventureCard> tournamentStash;

	Tournaments(TournamentCard t) {
		discardPile = new ArrayList<AdventureCard>();
		players = new ArrayList<Player>();
		isTournamentsOver = false;
		bp = t.getBonesShiled();
		bonus = t.getBonesShiled();
		tournamentStash = new ArrayList<AdventureCard>();
		
		logger.info("Tournaments started");
	}

	public boolean addPlayer(Player p) {
		// p.addShields(-1);
		bp = bp + 1;
		return players.add(p);
	}

	public boolean removePlayer(Player p) {
		return players.remove(p);
		
	}

	public void startTournaments() {
		iter = players.listIterator();
	}

	public Player getNextPlayer() {
		if (iter.hasNext()) {
			currentPlayer = iter.next();
			return currentPlayer;
		} else {
			currentPlayer = null;
			iter = players.listIterator(); // reset to beginning
			return null;
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void tieBreaking() {
		int maxBP = 0;
		// find maxBP in players
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			logger.info("Player "+p.getPlayerNumber()+" plays tournament with "+p.getBattlePoints()+" battle points");
			if (p.getBattlePoints() > maxBP) {
				maxBP = p.getBattlePoints();
			}
		}
		// delete players who's BP is less than maxBP
		for (Iterator<Player> removeIter = players.iterator(); removeIter.hasNext();) {
			Player p = removeIter.next();
			if (p.getBattlePoints() != maxBP) {
				removeCardsOfType(p, AdventureCard.AdventureType.WEAPON);
				logger.info("Player "+p.getPlayerNumber()+" eliminated from tournament");
				removeIter.remove();
			}
		}
	}

	public void evaluatePlayers() {
		tieBreaking();
		// normal condition， only one has the maxBP
		if (players.size() == 1) {
			isTournamentsOver = true;
			awardQuestWinners(1);
		}
		// tie-breaking condition
		if (players.size() > 1) {
			for (int i = 0; i < players.size(); i++) {
				//discardPile.clear();
				removeCardsOfType(players.get(i), AdventureCard.AdventureType.WEAPON);
			}
			tieBreaking();
			if (players.size() == 1) {
				isTournamentsOver = true;
				awardQuestWinners(1);
			} else if (players.size() > 1) {
				isTournamentsOver = true;
				awardQuestWinners(2);
			}
		}
		
		for(int i=0; i<players.size(); i++) {
			removeCardsOfType(players.get(i), AdventureCard.AdventureType.WEAPON);
		}
	}

	public boolean isTournamentsOver() {
		return isTournamentsOver;
	}

	public ArrayList<AdventureCard> getDiscardPile() {
		return discardPile;
	}

	private void awardQuestWinners(int awardType) {
		int shieldsToAward1 = bp;
		int shieldsToAward2 = bp - bonus;
		if (awardType == 1) {
			for (Player p : players)
				p.addShields(shieldsToAward1);
		} else {
			for (Player p : players)
				p.addShields(shieldsToAward2);
		}
	}
	
	public void addToStash(AdventureCard c) {
		tournamentStash.add(c);
	}
	
	public ArrayList<AdventureCard> getStash(){
		return tournamentStash;
	}

	private void removeCardsOfType(Player p, AdventureCard.AdventureType t) {

		ArrayList<AdventureCard> playerHand = p.getPlay();

		for (Iterator<AdventureCard> it = playerHand.iterator(); it.hasNext();) {
			AdventureCard c = it.next();

			if (c.getCardType() == t) {
				discardPile.add(c);
				it.remove();
			}
		}
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public int getBonus() {
		return bonus;
	}
}