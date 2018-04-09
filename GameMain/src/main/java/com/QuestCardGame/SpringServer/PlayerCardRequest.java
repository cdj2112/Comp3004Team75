package com.QuestCardGame.SpringServer;

public class PlayerCardRequest {
	private int player;
	private int cardId;
	private int stageNum;

	public PlayerCardRequest() {

	}

	public void setPlayer(int p) {
		player = p;
	}

	public void setCardId(int id) {
		cardId = id;
	}

	public void setStageNum(int s) {
		stageNum = s;
	}
	
	public int getPlayer() {
		return player;
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public int getStageNum() {
		return stageNum;
	}
}
