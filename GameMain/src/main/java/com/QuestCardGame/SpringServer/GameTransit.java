package com.QuestCardGame.SpringServer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import com.QuestCardGame.GameMain.AdventureCard.AdventureType;
import com.QuestCardGame.GameMain.Game;
import com.QuestCardGame.GameMain.Game.GameStatus;

public class GameTransit implements Serializable{

	private GameStatus currentStatus;
	private PlayerTransit[] playerStatus;
	private CardTransit storyCard = null;
	private QuestTransit currentQuest = null;
	private TournamentTransit currentTournament = null;
	private int activePlayer;
	private int[] toDiscard;
	private HashMap<String, Integer>[] specialDiscard;
	private boolean[] canPlay;
	private boolean aiPlaying;

	public GameTransit(Game g) {
		currentStatus = g.getGameStatus();
		activePlayer = g.getCurrentActivePlayer();
		aiPlaying = g.getCurrentActivePlayerObj().isAIPlayer();
		String storyName;
		if(g.getActiveStoryCard()!=null) {
			storyCard = new CardTransit(g.getActiveStoryCard());
			storyName = storyCard.getName();
		} else {
			storyName = null;
		}

		playerStatus = new PlayerTransit[g.getNumPlayers()];
		for (int i = 0; i < g.getNumPlayers(); i++) {
			playerStatus[i] = new PlayerTransit(g.getPlayer(i), storyName);
		}
		
		canPlay = new boolean[g.getNumPlayers()];
		
		if(g.getActiveQuest()!=null){
			currentQuest = new QuestTransit(g.getActiveQuest());
			currentQuest.setSponsor(g.getSponsorIndex());
			for (int i = 0; i < g.getNumPlayers(); i++) {
				canPlay[i] = g.getActiveQuest().hasPlayer(g.getPlayer(i));
			}
		} else if(g.getActiveTournament()!=null) {
			currentTournament = new TournamentTransit(g.getActiveTournament());
			for (int i = 0; i < g.getNumPlayers(); i++) {
				canPlay[i] = g.getActiveTournament().hasPlayer(g.getPlayer(i));
			}
		} else {
			for (int i = 0; i < g.getNumPlayers(); i++) {
				canPlay[i] = true;
			}
		}
		
		toDiscard = g.getAllDiscard();
		specialDiscard = new HashMap[g.getNumPlayers()];
		for(int i=0; i<g.getNumPlayers(); i++) {
			HashMap<AdventureType, Integer> map = g.getSpecialDiscard()[i];
			specialDiscard[i] = new HashMap<String, Integer>();
			Set<AdventureType> keys = map.keySet();
			for(AdventureType k : keys) {
				specialDiscard[i].put(k.toString(), map.get(k));
			}
		}
	}
	
	public GameStatus getCurrentStatus() {
		return currentStatus;
	}
	
	public PlayerTransit[] getPlayerStatus() {
		return playerStatus;
	}
	
	public CardTransit getStoryCard() {
		return storyCard;
	}
	
	public int getActivePlayer() {
		return activePlayer;
	}
	
	public QuestTransit getCurrentQuest() {
		return currentQuest;
	}
	
	public TournamentTransit getCurrentTournament() {
		return currentTournament;
	}
	
	public int[] getToDiscard() {
		return toDiscard;
	}
	
	public HashMap<String, Integer>[] getSpecialDiscard(){
		return specialDiscard;
	}
	
	public boolean[] getCanPlay() {
		return canPlay;
	}
	
	public boolean getAiPlaying() {
		return aiPlaying;
	}
}
