package com.QuestCardGame.SpringServer;

public class PlayerDecisionRequest {
	private boolean accept;
	private int player;
	private int bid;

	public void setAccept(boolean a) {
		accept = a;
	}

	public void setPlayer(int p) {
		player = p;
	}
	
	public void setBid(int b) {
		bid = b;
	}
	
	public boolean getAccept() {
		return accept;
	}
	
	public int getPlayer() {
		return player;
	}
	
	public int getBid() {
		return bid;
	}
}
