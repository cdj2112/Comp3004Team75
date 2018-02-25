package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Iterator; 

public class Tournaments {	
	private ArrayList<Player> players;
	private ListIterator<Player> iter;
	private Player currentPlayer;
	private ArrayList<AdventureCard> discardPile;
	private boolean isTournamentsOver;
	private int bp;
	private int bones;
	
	Tournaments(TournamentCard t) {
		discardPile = new ArrayList<AdventureCard>();
		players = new ArrayList<Player>();
		isTournamentsOver = false;
		bp = t.getBonesBP();
		bones = t.getBonesBP();
}
	
	public boolean addPlayer(Player p) {
		//p.addShields(-1);
		bp = bp + 1;
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
		//System.out.println(players.size());
		temp = new int[players.size()];
		for(int i=0; i<players.size(); i++ ) {
			temp[i] = players.get(i).getBattlePoints();
		}
		
		for(int i=0; i<temp.length;i++){
			if(temp[i]>max) max=temp[i];
			}
		
		for(int i=0; i<temp.length;i++){		
			if(temp[i]==max) count=count + 1;
			System.out.println(count);
			}
		
		int[] tie = new int[2];
		tie[0]=count;
		tie[1]=max;
	    return tie;
	}
	
	
	public void evaluatePlayer(ArrayList<Player> p) {
		int[] temp = new int[2];
		temp = tieBreaking(temp);
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
        	temp = tieBreaking(temp);
        	max = temp[1];
            count = temp[0];

            if(count == 1) {
        		for(int i=0; i<players.size(); i++ ) {
        			if(players.get(i).getBattlePoints() != max) players.remove(i);
        		}
        		    isTournamentsOver = true;
        		    awardQuestWinners(1);
        		    
                }
            if(count > 1) {
            	isTournamentsOver = true;
            	awardQuestWinners(2);
            }
        }		
	}


	public boolean isTournamentsOver() {
		return isTournamentsOver;
	}
	
	public ArrayList<AdventureCard> getDiscardPile(){
		return discardPile;
	}
		
	private void awardQuestWinners(int awardType) {
		int shieldsToAward1 = bp;
		int shieldsToAward2 = bp - bones ;
		if(awardType == 1) {
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
