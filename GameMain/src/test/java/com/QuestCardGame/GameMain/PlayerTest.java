package com.QuestCardGame.GameMain;

import junit.framework.TestCase;

public class PlayerTest extends TestCase{
	
	
	public void testPlayerDrawCard() {
		Card c = new QuestCard("Test card",1);
		Player p = new Player();
		
		p.drawCard(c);
		assert(p.getHand().contains(c));
	}
	
	public void testPlayerPlayCard() {
		Card c = new QuestCard("Test card",1);
		Player p = new Player();
		
		p.drawCard(c);
		p.playCard(c);
		assert(!p.getHand().contains(c));
	}
}
