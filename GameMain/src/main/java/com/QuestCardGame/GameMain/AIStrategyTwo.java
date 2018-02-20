package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;

public class AIStrategyTwo implements AIPlayerStrategy {

	public boolean doIJoinTournament(Player current, ArrayList<Player> allPlayers) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AdventureCard> playCardsForTournament(Player current) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean doISponsorAQuest(Player current, ArrayList<Player> allPlayers) {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<Integer, ArrayList<AdventureCard>> createQuest(Player current, QuestCard qc) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean doIJoinQuest(Player current, int numStages) {
		ArrayList<AdventureCard> hand = current.getHand();
		int foesToDiscard = getNumFoesToDiscard(hand);

		//TODO 	check can increase BP by 10 points each stage, otherwise
		//		don't join quest
		
		return  foesToDiscard >= 2;
	}

	public ArrayList<AdventureCard> playCardsForQuestStage(Player current, int currentStage, int totalStages) {
		ArrayList<AdventureCard> play = new ArrayList<AdventureCard>();
		if(currentStage == totalStages) {
			//TODO play strongest possible hand
		}
		else {
			//TODO play +10 using amour, ally, weapon in that order
		}
		return play;
	}

	public int getBidForTest(int currentBid, int currentRound, Player current) {
		int numToBid = 0;
		
		if(currentRound == 1) {
			numToBid = getNumFoesToDiscard(current.getHand());
		}
		else if(currentRound == 2) {
			//TODO add # duplicates to bid
			//numToBid += getNumFoesToDiscard(current.getHand()) + getNumDuplicates(current.getHand());
		}
		
		return numToBid;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest(Player current) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getNumFoesToDiscard(ArrayList<AdventureCard> hand) {
		int numFoesToDiscard = 0;
		for(AdventureCard c: hand) {
			if(c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) < 25)
				numFoesToDiscard++;
		}
		return numFoesToDiscard;
	}

}
