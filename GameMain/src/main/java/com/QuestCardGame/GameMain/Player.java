package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Player {
		
	private static final Logger logger = LogManager.getLogger(Player.class);
	
	private ArrayList<Card> hand;
	private ArrayList<Card> play;
	private int playerNumber;
	private int numShields;
	private int rank;
	static int nextPlayerNumber = 1;
	
	Player(){
		hand = new ArrayList<Card>();
		play = new ArrayList<Card>();
		playerNumber = nextPlayerNumber++;
		numShields = 0;
		rank = 5; //squire
		
		//disabled until log4j2.xml has been created
		//logger.info("A new player has been created.");
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
	
	public void useCard(Card c) {
		hand.remove(c);
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
	
	public int getBattlePoints() {
		int totalBattlePoints = rank + numShields;
		for(Card c : play) {
			// get BP instead of id when adventure cards are added
			totalBattlePoints += c.getId(); 
		}
		return totalBattlePoints;
	}
	
	public void addShields(int s) {
		numShields += s;
		//TODO:
		//upgrade rank based on number of shields
	}
}
