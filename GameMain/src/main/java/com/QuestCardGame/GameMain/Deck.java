package com.QuestCardGame.GameMain;

import java.util.Collections;
import java.util.Stack;

public class Deck {

	private Stack<Card> cards;
	private int numCards;

	Deck() {
		cards = new Stack<Card>();
		numCards = 0;
	}

	public Card drawCard() {
		if (cards.size() > 0) {
			numCards--;
			return cards.pop();
		}
		else
			return null;
	}
	
	public void addCard(Card c) {
		cards.push(c);
		numCards++;
	}

	public void shuffleDeck() {
		Collections.shuffle(cards);
	}
	
	public int getNumCards() {
		return numCards;
	}
}
