package com.QuestCardGame.SpringServer;

import java.util.ArrayList;

public class PlayerListTransit {
	String[] userNames;
	int[] strategies;
	boolean rigged;

	public PlayerListTransit(ArrayList<String> uN, int[] aiS, boolean b) {
		int numPlayers = uN.size();
		userNames = new String[numPlayers];
		strategies = new int[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			userNames[i] = uN.get(i);
			strategies[i] = aiS[i];
		}
		rigged = b;
	}

	public String[] getUserNames() {
		return userNames;
	}

	public int[] getStrategies() {
		return strategies;
	}
	
	public boolean getRigged() {
		return rigged;
	}
}
