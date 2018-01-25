package com.QuestCardGame.GameMain;

import java.util.Stack;

public class Deck {

	private Stack<Card> cards;

	Deck(int size) {

		cards = new Stack<Card>();

		for (int i = 0; i < size; i++) {
			cards.push(new Card(i));
		}
	}
}
