package com.QuestCardGame.SpringServer;

public class AIChangeRequest {
	private int index;
	private int newStrategy;

	public AIChangeRequest() {

	}

	public AIChangeRequest(int i, int s) {
		index = i;
		newStrategy = s;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int i) {
		index = i;
	}

	public int getNewStrategy() {
		return newStrategy;
	}

	public void setNewStrategy(int s) {
		newStrategy = s;
	}
}
