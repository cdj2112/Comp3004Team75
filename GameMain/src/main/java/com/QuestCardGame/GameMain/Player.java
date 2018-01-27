package com.QuestCardGame.GameMain;

import java.util.ArrayList;


public class Player {
	private ArrayList<Card> hand;
	private ArrayList<Card> play;
	private int playerNumber;
	private int numShields;
	static int nextPlayerNumber = 1;
	
	Player(){
		hand = new ArrayList<Card>();
		play = new ArrayList<Card>();
		playerNumber = nextPlayerNumber++;
		numShields = 0;
	}
	
	public void drawCard(Card c){
		hand.add(c);
	}
	
	public void playCard(Card c) {
		boolean removed = hand.remove(c);
		if(removed) {
			play.add(c);
		}
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public ArrayList<Card> getPlay() {
		return play;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}
}
