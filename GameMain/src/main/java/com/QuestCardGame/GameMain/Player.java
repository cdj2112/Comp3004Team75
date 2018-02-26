package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Player {
		
	private static final Logger logger = LogManager.getLogger(Player.class);
	private static final String[] rankNames = {"Squire", "Knight", "Champion Knight"};
	private static final int[] battlePoints = {5, 10, 20};
	private static final int[] shieldsNeeded = {5, 7, 10};
	
	private ArrayList<AdventureCard> hand;
	private ArrayList<AdventureCard> play;

	private int playerNumber;
	private int numShields;
	private int rank;
	static int nextPlayerNumber = 1;
	
	Player(){
		hand = new ArrayList<AdventureCard>();
		play = new ArrayList<AdventureCard>();
		playerNumber = nextPlayerNumber++;
		numShields = 0;
		rank = 0; //squire
		
		//disabled until log4j2.xml has been created
		//logger.info("A new player has been created.");
	}
	
	public void drawCard(AdventureCard c){
		hand.add(c);
	}
	
	public void playCard(AdventureCard c) {
		boolean removed = hand.remove(c);
		if (removed) {
			play.add(c);
		}
	}
	
	public void useCard(Card c) {
		hand.remove(c);
	}
	
	public ArrayList<AdventureCard> getHand() {
		return hand;
	}
	
	public ArrayList<AdventureCard> getPlay() {
		return play;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}
	
	public int getBattlePoints() {
		int totalBattlePoints = battlePoints[rank] + numShields;
		for(AdventureCard c : play) {
			totalBattlePoints += c.getBattlePoint(false); //no special ability
		}
		return totalBattlePoints;
	}
	
	public void addShields(int s) {
		numShields += s;

		if(numShields >= shieldsNeeded[rank]) {
			numShields -= shieldsNeeded[rank];
			rank++;
		}
	}
	
	public int getNumShields() {
		return numShields;
	}
	
	public String getRankName() {
		return rankNames[rank];
	}
	
	public String getRankImagePath() {
		return "./src/resources/Cards/Rank/"+getRankName()+".png";
	}
}
