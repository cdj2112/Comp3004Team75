package com.QuestCardGame.GameMain;

import junit.framework.TestCase;
import java.util.ArrayList;

public class HandTest extends TestCase {

	AdventureCard w1 = new Weapon("Dagger", 1);
	AdventureCard w2 = new Weapon("Axe",2);
	AdventureCard w3 = new Weapon("Axe",2);
	AdventureCard w4 = new Weapon("BattleAxe", 10);
	AdventureCard f1 = new Foe("Foe", 6);
	AdventureCard f2 = new Foe("Foe", 12);
	AdventureCard a1 = new Amours();
	AdventureCard a2 = new Amours();
	AdventureCard ally = new Ally("Ally", 10, 1, "target");
	AdventureCard test = new Test("TestCard");

	Player p = new Player();
	
	public void testPlayerHandSortDescendingByBattlePoints() {
		p.getHand().clear();
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
	
	public void testIsValidHandToJoinQuest() {
		AdventureCard w4 = new Weapon("BattleAxe", 10);
		AdventureCard w5 = new Weapon("BiggerBattleAxe", 20);

		p.getHand().clear();
		p.drawCard(w1);
		p.drawCard(w2);
		p.drawCard(w3);
		p.drawCard(f1);
		
		assert !p.getHand().hasIncreasingBattlePointsForStages(2, 10, p.getPlay());
		
		p.drawCard(w4);
		p.drawCard(w5);
		
		assert p.getHand().hasIncreasingBattlePointsForStages(2, 10, p.getPlay());

	}
	
	public void testBestPossibleHand() {
		p.getHand().clear();
		p.drawCard(w1);
		p.drawCard(w2);
		p.drawCard(w3);
		p.drawCard(f1);
		p.drawCard(a1);
		p.drawCard(a2);
		
		//hand should contain only w1, w2, a1
		Hand h = p.getHand().getBestPossibleHand(new ArrayList<AdventureCard>());
		assert h.contains(w1);
		assert h.contains(w2);
		assert !h.contains(f1);
		assert h.contains(a1);
		assert !h.contains(a2);
		
		//this hand shouldn't contain amour
		p.playCard(a2);
		h = p.getHand().getBestPossibleHand(p.getPlay());
		assert h.contains(w1);
		assert h.contains(w2);
		assert !h.contains(f1);
		assert !h.contains(a1);
	}
	
	public void testHasIncreasingBattlePointsForStages() {
		p.getHand().clear();
		p.drawCard(w1);		
		p.drawCard(f1);
		p.drawCard(w2);
		
		assert !p.getHand().hasIncreasingBattlePointsForStages(1, 5, p.getPlay());
		
		p.drawCard(ally);
		assert p.getHand().hasIncreasingBattlePointsForStages(1, 5, p.getPlay());
	}
	
	public void testHasCardsToSponsorQuest() {
		p.getHand().clear();
		p.drawCard(w1);
		p.drawCard(w2);
		p.drawCard(w3);
		p.drawCard(f1);
		
		//doesn't have 2 foes, fail
		assert !p.getHand().hasCardsToSponsorQuest(2);
		
		p.drawCard(f2);		
		assert p.getHand().hasCardsToSponsorQuest(2);		
		
		//doesn't have 3 foes
		assert !p.getHand().hasCardsToSponsorQuest(3);	
		p.drawCard(test);	
		assert p.getHand().hasCardsToSponsorQuest(3);
	}
	
}
