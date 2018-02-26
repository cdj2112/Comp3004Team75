package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Player {
		
	private static final Logger logger = LogManager.getLogger(Player.class);
	
	private Hand hand;
	private Hand play;
	private int playerNumber;
	private int numShields;
	private int rank;
	static int nextPlayerNumber = 1;
	
	Player(){
		hand = new Hand();
		play = new Hand();
		playerNumber = nextPlayerNumber++;
		numShields = 0;
		rank = 5; //squire
		
		//disabled until log4j2.xml has been created
		//logger.info("A new player has been created.");
	}
	
	public void drawCard(AdventureCard c){
		hand.add(c);
	}
	
	public void playCard(AdventureCard c) {
		boolean removed = hand.remove(c);
		if(removed) {
			play.add(c);
		}
	}
	
	public void useCard(Card c) {
		hand.remove(c);
	}
	
	public Hand getHand() {
		return hand;
	}
	
	public Hand getPlay() {
		return play;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}
	
	public int getBattlePoints() {
		int totalBattlePoints = rank + numShields;
		for(AdventureCard c : play) {
			totalBattlePoints += c.getBattlePoint(false); //no special ability
		}
		return totalBattlePoints;
	}
	
	public int getBattlePointsForHand(Hand h) {
		int total = 0;
		for(AdventureCard c: h) {
			total += c.getBattlePoint(false);
		}	
		return total;
	}
	
	public void addShields(int s) {
		numShields += s;
		//TODO:
		//upgrade rank based on number of shields
	}
	
	public int getNumShields() {
		return numShields;
	}
}
