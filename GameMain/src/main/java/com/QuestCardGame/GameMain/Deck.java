package com.QuestCardGame.GameMain;

import java.util.Collections;
import java.util.Stack;

public class Deck {

	private Stack<Card> cards;

	Deck() {
		cards = new Stack<Card>();
	}

	public Card drawCard() {
		if (cards.size() > 0)
			return cards.pop();
	}
	
	public void addCard(Card c) {
		cards.push(c);
	}
	
	public void shuffleDeck() {
		Collections.shuffle(cards);
	}
}
