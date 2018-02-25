package com.QuestCardGame.GameMain;

import junit.framework.TestCase;

public class PlayerTest extends TestCase{
	
	
	public void testPlayerDrawCard() {
		AdventureCard c = new Foe("Test card",1);
		Player p = new Player();
		
		p.drawCard(c);
		assert(p.getHand().contains(c));
	}
	
	public void testPlayerPlayCard() {
		AdventureCard c = new Foe("Test card",1);
		Player p = new Player();
		
		p.drawCard(c);
		p.playCard(c);
		assert(!p.getHand().contains(c));
	}
	
	public void testPlayerHandSortDescendingByBattlePoints() {
		AdventureCard f1 = new Foe("TestFoe1", 6);
		AdventureCard w1 = new Weapon("Dagger", 1);

		Player p = new Player();
		p.drawCard(w1);
		p.drawCard(f1);
		
		assert p.getHand().indexOf(f1) == 1;
		assert p.getHand().indexOf(w1) == 0;
		
		p.getHand().sortDescendingByBattlePoints();
		
		assert p.getHand().indexOf(f1) == 0;
		assert p.getHand().indexOf(w1) == 1;
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
		
		Hand h = p.getHand().getBestPossibleHand();
		assert !h.contains(f1) && h.contains(w1) && h.contains(w3);
		
		p.drawCard(w2);
		h = p.getHand().getBestPossibleHand();
		assert !h.contains(f1) && h.contains(w1) && !h.contains(w2) && h.contains(w3);
		
	}
}
