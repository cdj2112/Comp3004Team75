package com.QuestCardGame.GameMain;

import java.util.ArrayList;

public class Player {
	private ArrayList<Card> hand;
	private ArrayList<Card> play;
	
	Player(){
		hand = new ArrayList<Card>();
		play = new ArrayList<Card>();
	}
	
	public void drawCard(Card c){
		hand.add(c);
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
}
