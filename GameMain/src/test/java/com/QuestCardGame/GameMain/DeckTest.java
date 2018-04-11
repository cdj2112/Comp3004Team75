package com.QuestCardGame.GameMain;

import junit.framework.TestCase;

public class DeckTest extends TestCase {

	public void testDrawCard() {
		Deck d = new Deck();
		Card c1 = new QuestCard("test", 3, null);
		d.addCard(c1);

		assertEquals(1, d.getNumCards());

		Card c2 = d.drawCard();

		assertEquals(0, d.getNumCards());
		assertEquals(c1, c2);
	}

	public void testEmptyDeck() {
		Deck d = new Deck();
		Card c = d.drawCard();

		assertEquals(c, null);
	}

}
