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
	protected int rank;
	static int nextPlayerNumber = 1;
	
	private AIPlayer playerBehaviour;

	// ONLY USED FOR TEST CASES WHERE AI AND GAME IS NOT NEEDED
	Player(){
		hand = new Hand();
		play = new ArrayList<AdventureCard>();
		playerNumber = nextPlayerNumber++;
		numShields = 0;
		rank = 0; //squire
	}
	
	Player(Game g, int behaviour){
		hand = new Hand();
		play = new ArrayList<AdventureCard>();
		playerNumber = nextPlayerNumber++;
		numShields = 0;
		rank = 0; //squire
		assignStrategy(g, behaviour);
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
	
	public int getBids() {
		int total = 0;
		for(AdventureCard c : play) {
			total += c.getFreeBids();
		}
		return total;
	}
	
	public void addShields(int s) {
		numShields += s;
		logger.info("Player "+getPlayerNumber()+": Gains "+s+" shields");
		while(numShields >= shieldsNeeded[rank]) {
			numShields -= shieldsNeeded[rank];
			rank++;
			logger.info("Player "+getPlayerNumber()+": Promoted to "+rankNames[rank]+" with "+numShields+" shields");
		}
	}

	public int getNumShields() {
		return numShields;
	}
	
	public int getShieldsNeeded() {
		return shieldsNeeded[rank];
	}
	
	public int getRank() {
		return rank;
	}
	
	public String getRankName() {
		return rankNames[rank];
	}
	
	public String getRankImagePath() {
		return "./src/resources/Cards/Rank/"+getRankName()+".png";
	}
	
	public String getRankUrlPath() {
		return "/Cards/Rank/"+getRankName()+".png";
	}
	
	public boolean isAIPlayer() {
		return playerBehaviour.isAIPlayer();
	}
	
	private void assignStrategy(Game g, int strategy) {
		if(strategy == 1)
			playerBehaviour = new AIStrategyOne(g, this);
		else if (strategy == 2)
			playerBehaviour = new AIStrategyTwo(g, this);
		else
			playerBehaviour = new EmptyAIStrategy();
	}
	
	public ArrayList<AdventureCard> playTurn() {
		return playerBehaviour.playTurn();
	}
}
