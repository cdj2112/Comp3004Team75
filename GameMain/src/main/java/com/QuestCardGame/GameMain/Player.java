package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Player {

	private static final Logger logger = LogManager.getLogger(Player.class);

	//private static final Logger logger = LogManager.getLogger(Player.class);
	private static final String[] rankNames = {"Squire", "Knight", "Champion Knight","Knight of The Round Table"};
	private static final int[] battlePoints = {5, 10, 20, 20};
	private static final int[] shieldsNeeded = {5, 7, 10, 100};
	
	private Hand hand;
	private ArrayList<AdventureCard> play;

	private int playerNumber;
	private int numShields;
	private int rank;
	static int nextPlayerNumber = 1;

	Player(){
		hand = new Hand();
		play = new Hand();
		playerNumber = nextPlayerNumber++;
		numShields = 0;
		rank = 0; //squire
		//disabled until log4j2.xml has been created
		logger.info("Player " + playerNumber + ": created");
	}

	public void drawCard(AdventureCard c){
		hand.add(c);
		logger.info("Player " + playerNumber + ": DREW [" + c.getName() + "]");
	}

	public void playCard(AdventureCard c) {
		boolean removed = hand.remove(c);
		if (removed) {
			play.add(c);
			logger.info("Player " + playerNumber + ": PLAYED [" + c.getName() + "]");
		}
	}

	public void useCard(Card c) {
		hand.remove(c);
		//logger.info("Player " + playerNumber + ": USED [" + c.getName() + "]");
	}

	public Hand getHand() {
		return hand;
	}

	public ArrayList<AdventureCard> getPlay() {
		return play;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public int getBattlePoints() {
		int totalBattlePoints = battlePoints[rank];
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
		if (numShields < 0) numShields = 0;
		logger.info("Player "+getPlayerNumber()+": Gains "+s+" shields");
		while(numShields >= shieldsNeeded[rank]) {
			numShields -= shieldsNeeded[rank];
			rank++;
			logger.info("Player "+getPlayerNumber()+": Promoted to "+rankNames[rank]+" with "+numShields+" shields");
		}
	}

	public int getRank(){
		return rank;
	}

	public int getNumShields() {
		return numShields;
	}

	public int getShieldsNeeded() {
		return shieldsNeeded[rank];
	}
	
	public String getRankName() {
		return rankNames[rank];
	}

	public String getRankImagePath() {
		return "./src/resources/Cards/Rank/"+getRankName()+".png";
	}

	public boolean isAIPlayer() {
		return false;
	}
}
