package com.QuestCardGame.GameMain;

import java.util.Collections;
import java.util.Stack;

public class Deck {

	private Stack<Card> cards;
	private Stack<Card> discard;

	Deck() {
		cards = new Stack<Card>();
		discard = new Stack<Card>();
	}

	public Card drawCard() {
		if (cards.size() > 0) {
			return cards.pop();
		}
		else {
			handleEmptyDeck();
			return cards.pop();
		}
	}

	public void addCard(Card c) {
		cards.push(c);
	}

	public void shuffleDeck() {
		Collections.shuffle(cards);
	}

	public int getNumCards() {
		return cards.size();
	}

	public void discard(Card c) {
		discard.push(c);
	}

	private void handleEmptyDeck() {
		if(cards.size() != 0)
			return;

		cards.addAll(discard);
		discard.clear();
		shuffleDeck();
	}
}
