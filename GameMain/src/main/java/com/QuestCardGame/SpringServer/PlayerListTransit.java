package com.QuestCardGame.SpringServer;

import java.util.ArrayList;

public class PlayerListTransit {
	String[] userNames;
	int[] strategies;

	public PlayerListTransit(ArrayList<String> uN, int[] aiS) {
		int numPlayers = uN.size();
		userNames = new String[numPlayers];
		strategies = new int[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			userNames[i] = uN.get(i);
			strategies[i] = aiS[i];
		}
	}

	public String[] getUserNames() {
		return userNames;
	}

	public int[] getStrategies() {
		return strategies;
	}
}
