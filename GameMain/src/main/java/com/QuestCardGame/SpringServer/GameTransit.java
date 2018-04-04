package com.QuestCardGame.SpringServer;

import java.io.Serializable;
import com.QuestCardGame.GameMain.Game;
import com.QuestCardGame.GameMain.Game.GameStatus;

public class GameTransit implements Serializable{

	private GameStatus currentStatus;
	private PlayerTransit[] playerStatus;
	private CardTransit storyCard = null;
	private QuestTransit currentQuest = null;
	private int activePlayer;
	private int[] toDiscard;

	public GameTransit(Game g) {
		currentStatus = g.getGameStatus();
		activePlayer = g.getCurrentActivePlayer();

		playerStatus = new PlayerTransit[g.getNumPlayers()];
		for (int i = 0; i < g.getNumPlayers(); i++) {
			playerStatus[i] = new PlayerTransit(g.getPlayer(i));
		}
		
		if(g.getActiveStoryCard()!=null) {
			storyCard = new CardTransit(g.getActiveStoryCard());
		}
		
		if(g.getActiveQuest()!=null){
			currentQuest = new QuestTransit(g.getActiveQuest());
		}
		
		toDiscard = g.getAllDiscard();
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
	
	public int[] getToDiscard() {
		return toDiscard;
	}
}
