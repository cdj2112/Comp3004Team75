package com.QuestCardGame.GameMain;

import junit.framework.TestCase;

public class PlayerTest extends TestCase{
	
	public void testPlayerNumber() {
		Player p1 = new Player();
		Player p2 = new Player();
		
		assertEquals(1, p1.getPlayerNumber());
		assertEquals(2, p2.getPlayerNumber());
	}
}
