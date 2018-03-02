package com.QuestCardGame.GameMain;

import junit.framework.Assert;
import junit.framework.TestCase;
import java.util.ArrayList;

public class TourTest extends TestCase {
	/*
	 * 1 players, not tie-breaking condition p1 win, and get 2 shileds(1bones)
	 */
	public void testEvaluatePlayer1() {
		ArrayList<Player> players;
		players = new ArrayList<Player>();
		TournamentCard c = new TournamentCard("pk", 1);
		Tournaments tour = new Tournaments(c);
		AdventureCard w1 = new Weapon("Dagger", 15);
		Player p1 = new Player();
		p1.drawCard(w1);
		tour.addPlayer(p1);
		tour.startTournaments();

		// p1 plays card
		Player nextPlayer = tour.getNextPlayer();
		assert nextPlayer == p1;
		nextPlayer.playCard(w1);

		// playing round is done
		nextPlayer = tour.getNextPlayer();
		assert nextPlayer == null;

		// p1 should win and get shield
		nextPlayer = tour.getNextPlayer();
		tour.evaluatePlayers(players);
		Assert.assertEquals(2, p1.getNumShields());
		nextPlayer = tour.getNextPlayer();
		assert nextPlayer == null;

		// eval round is done
		assert tour.isTournamentsOver() == true;
	}

	/*
	 * 2 players, not tie-breaking condition p1 win, and get 3 shileds(1bones),
	 * p2 get 0 shields
	 */
	public void testEvaluatePlayer2() {
		ArrayList<Player> players;
		players = new ArrayList<Player>();
		TournamentCard c = new TournamentCard("pk", 1);
		Tournaments tour = new Tournaments(c);
		AdventureCard w1 = new Weapon("Dagger", 15);
		AdventureCard w2 = new Weapon("Axe", 5);
		Player p1 = new Player();
		Player p2 = new Player();
		p1.drawCard(w1);
		p2.drawCard(w2);
		tour.addPlayer(p1);
		tour.addPlayer(p2);
		tour.startTournaments();

		// p1 plays card
		Player nextPlayer = tour.getNextPlayer();
		assert nextPlayer == p1;
		nextPlayer.playCard(w1);

		// p2 plays card
		nextPlayer = tour.getNextPlayer();
		assert nextPlayer == p2;
		nextPlayer.playCard(w2);

		// playing round is done
		nextPlayer = tour.getNextPlayer();
		assert nextPlayer == null;

		// p1 should win and get shield
		nextPlayer = tour.getNextPlayer();
		tour.evaluatePlayers(players);
		// System.out.println(p2.getNumShields());
		Assert.assertEquals(3, p1.getNumShields());
		Assert.assertEquals(0, p2.getNumShields());

		// eval round is done
		assert tour.isTournamentsOver() == true;
	}

	/*
	 * 2 players, tie-breaking condition p1 and p2 get 2 shileds(1bones)
	 */
	public void testEvaluatePlayer3() {
		ArrayList<Player> players;
		players = new ArrayList<Player>();
		TournamentCard c = new TournamentCard("pk", 1);
		Tournaments tour = new Tournaments(c);
		AdventureCard w1 = new Weapon("Dagger", 15);
		AdventureCard w2 = new Weapon("Axe", 15);
		Player p1 = new Player();
		Player p2 = new Player();
		p1.drawCard(w1);
		p2.drawCard(w2);
		tour.addPlayer(p1);
		tour.addPlayer(p2);
		tour.startTournaments();

		// p1 plays card
		Player nextPlayer = tour.getNextPlayer();
		assert nextPlayer == p1;
		nextPlayer.playCard(w1);

		// p2 plays card
		nextPlayer = tour.getNextPlayer();
		assert nextPlayer == p2;
		nextPlayer.playCard(w2);

		// playing round is done
		nextPlayer = tour.getNextPlayer();
		assert nextPlayer == null;

		// p1 should win and get shield
		nextPlayer = tour.getNextPlayer();
		tour.evaluatePlayers(players);

		Assert.assertEquals(2, p1.getNumShields());
		Assert.assertEquals(2, p2.getNumShields());

		// eval round is done
		assert tour.isTournamentsOver() == true;
	}

}