package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;

public class Tournaments {
	private ArrayList<Player> players;
	private ListIterator<Player> iter;
	private Player currentPlayer;
	private ArrayList<AdventureCard> discardPile;
	private boolean isTournamentsOver;
	private int bp;
	private int bonus;

	Tournaments(TournamentCard t) {
		discardPile = new ArrayList<AdventureCard>();
		players = new ArrayList<Player>();
		isTournamentsOver = false;
		bp = t.getBonesShiled();
		bonus = t.getBonesShiled();
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
		int maxInfo[];
		maxInfo = new int[players.size()];
		// find maxBP in players
		for (int i = 0; i < players.size(); i++) {
			maxInfo[i] = players.get(i).getBattlePoints();
			if (maxInfo[i] > maxBP) {
				maxBP = maxInfo[i];
			}
		}
		// delete players who's BP is less than maxBP
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getBattlePoints() != maxBP)
				players.remove(i);
			discardPile.clear();
		}
	}

	public void evaluatePlayers(ArrayList<Player> p) {
		tieBreaking();
		// normal condition， only one has the maxBP
		if (players.size() == 1) {
			isTournamentsOver = true;
			awardQuestWinners(1);
		}
		// tie-breaking condition
		if (players.size() > 1) {
			for (int i = 0; i < players.size(); i++) {
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
}