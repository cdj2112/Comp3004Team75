package com.QuestCardGame.GameMain;

import junit.framework.TestCase;

public class PlayerTest extends TestCase {

	public void testPlayerDrawCard() {
		AdventureCard c = new Foe("Test card",1);
		Player p = new Player();

		p.drawCard(c);
		assert (p.getHand().contains(c));
	}

	public void testPlayerPlayCard() {
		AdventureCard c = new Foe("Test card",1);
		Player p = new Player();

		p.drawCard(c);
		p.playCard(c);
		assert (!p.getHand().contains(c));
	}
	
	public void testPlayerHandSortDescendingByBattlePoints() {
		AdventureCard w1 = new Weapon("Dagger", 1);
		AdventureCard f1 = new Foe("TestFoe1", 6);
		AdventureCard w2 = new Weapon("Axe",2);
		AdventureCard a1 = new Amours();

		Player p = new Player();
		p.drawCard(w1);
		p.drawCard(f1);
		p.drawCard(w2);
		p.drawCard(a1);
		
		assert p.getHand().indexOf(w1) == 0;
		assert p.getHand().indexOf(w2) == 2;
		assert p.getHand().indexOf(f1) == 1;
		assert p.getHand().indexOf(a1) == 3;

		
		p.getHand().sortDescendingByBattlePoints();
		
		assert p.getHand().indexOf(f1) == 1;
		assert p.getHand().indexOf(w1) == 3;
		assert p.getHand().indexOf(w2) == 2;
		assert p.getHand().indexOf(a1) == 0;
		
		p.getHand().sortDescendingByCardTypeBattlePoints();
		
		assert p.getHand().indexOf(f1) == 1;
		assert p.getHand().indexOf(w1) == 3;
		assert p.getHand().indexOf(w2) == 2;
		assert p.getHand().indexOf(a1) == 0;

	}
	
	/**
	 * Shouldn't contain any foes
	 * Shouldn't contain any duplicate weapons
	 */
	public void testBestPossibleHand() {
		AdventureCard f1 = new Foe("TestFoe1", 6);
		AdventureCard w1 = new Weapon("Dagger", 1);
		AdventureCard w2 = new Weapon("Dagger", 1);
		AdventureCard w3 = new Weapon("BattleAxe", 10);

		Player p = new Player();
		p.drawCard(w1);
		p.drawCard(f1);
		p.drawCard(w3);
		
		Hand h = p.getHand().getBestPossibleHand(p.getPlay());
		assert !h.contains(f1) && h.contains(w1) && h.contains(w3);
		
		p.drawCard(w2);
		h = p.getHand().getBestPossibleHand(p.getPlay());
		assert !h.contains(f1) && h.contains(w1) && !h.contains(w2) && h.contains(w3);
		
	}
	
	public void testIsValidHandToJoinQuest() {
		AdventureCard f1 = new Foe("TestFoe1", 6);
		AdventureCard w1 = new Weapon("Dagger", 1);
		AdventureCard w2 = new Weapon("Dagger", 1);
		AdventureCard w3 = new Weapon("BattleAxe", 10);
		AdventureCard w4 = new Weapon("BattleAxe", 10);
		AdventureCard w5 = new Weapon("BiggerBattleAxe", 20);

		Player p = new Player();
		p.drawCard(w1);
		p.drawCard(w2);
		p.drawCard(w3);
		p.drawCard(f1);
		
		assert !p.getHand().hasIncreasingBattlePointsForStages(2, 10, p.getPlay());
		
		p.drawCard(w4);
		p.drawCard(w5);
		
		assert p.getHand().hasIncreasingBattlePointsForStages(2, 10, p.getPlay());

	}
}
