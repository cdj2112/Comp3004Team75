package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator; 

public class Tournaments {	
	private ArrayList<Player> players;
	private ListIterator<Player> iter;
	private Player currentPlayer;
	private ArrayList<AdventureCard> discardPile;
	private boolean isTournamentsOver;
	private int bp;
	
	Tournaments(TournamentCard t) {
		discardPile = new ArrayList<AdventureCard>();
		players = new ArrayList<Player>();
		isTournamentsOver = false;
		bp = t.getBonesBP();
}
	
	public boolean addPlayer(Player p) {
		p.addShields(-1);
		return players.add(p);
	}
	
	public boolean removePlayer(Player p) {
		return players.remove(p);
	}
	
	public void startTournaments() {
		iter = players.listIterator();
	}
	
	public Player getNextPlayer() {
		if(iter.hasNext()) {
			currentPlayer = iter.next();
			return currentPlayer;
		}
		else {
			currentPlayer = null;
			iter = players.listIterator(); //reset to beginning
			return null;
		}
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int[] tieBreaking(int[] temp) {		
		int max = 0;
		int count = 0;
		temp = new int[players.size()];
		for(int i=0; i<players.size(); i++ ) {
			temp[i] = players.get(i).getBattlePoints();
		}
		
		for(int i=0; i<temp.length;i++){
			if(temp[i]>max) max=temp[i];
			}
		
		for(int i=0; i<temp.length;i++){
			if(temp[i]==max) count=count++;
			}
		
	    int[] tieBreakings = new int[2];
	    tieBreakings[0]=count;
	    tieBreakings[1]=max;
	    return tieBreakings;
	}
	
	
	public void evaluatePlayer(Player p) {
		int[] temp = new int[2];
		tieBreaking(temp);
		int max = temp[1];
        int count = temp[0];
		
        if(count == 1) {
		for(int i=0; i<players.size(); i++ ) {
			if(players.get(i).getBattlePoints() != max) players.remove(i);
		}
		    isTournamentsOver = true;
		    awardQuestWinners(1);
        }
		
        if(count > 1) {
        	for(int i=0; i<players.size(); i++ ) {
    			if(players.get(i).getBattlePoints() != max) players.remove(i);
    			if(players.get(i).getBattlePoints() == max) { 
    				discardPile.clear();
    				removeCardsOfType(players.get(i), AdventureCard.AdventureType.WEAPON);
    			}
    		}
        	tieBreaking(temp);
        	max = temp[1];
            count = temp[0];
            if(count == 1) {
        		for(int i=0; i<players.size(); i++ ) {
        			if(players.get(i).getBattlePoints() != max) players.remove(i);
        		}
        		    isTournamentsOver = true;
        		    awardQuestWinners(1);
                }
            if(count > 1) awardQuestWinners(2);
        }		
	}


	public boolean isTournamentsOver() {
		return isTournamentsOver;
	}
	
	public ArrayList<AdventureCard> getDiscardPile(){
		return discardPile;
	}
		
	private void awardQuestWinners(int awardType) {
		int shieldsToAward1 = bp + awardType;
		int shieldsToAward2 = bp;
		if(awardType ==1) {
		for(Player p : players)
			p.addShields(shieldsToAward1);
		}else {
			for(Player p : players)
				p.addShields(shieldsToAward2);
		}
	}
	
	private void removeCardsOfType(Player p, AdventureCard.AdventureType t) {
		
		ArrayList<AdventureCard> playerHand = p.getPlay();
		
		for(Iterator<AdventureCard> it = playerHand.iterator(); it.hasNext();) {
			AdventureCard c = it.next();
			
			if(c.getCardType() == t) {
				discardPile.add(c);
				it.remove();
			}
		}
	}
}
