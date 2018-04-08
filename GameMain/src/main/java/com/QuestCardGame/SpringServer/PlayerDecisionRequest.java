package com.QuestCardGame.SpringServer;

public class PlayerDecisionRequest {
	private boolean accept;
	private int player;

	public void setAccept(boolean a) {
		accept = a;
	}

	public void setPlayer(int p) {
		player = p;
	}
	
	public boolean getAccept() {
		return accept;
	}
	
	public int getPlayer() {
		return player;
	}
}
