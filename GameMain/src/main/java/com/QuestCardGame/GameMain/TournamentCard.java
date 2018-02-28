package com.QuestCardGame.GameMain;

public class TournamentCard extends Card {

	private int bonusBP;

	TournamentCard(String n, int b) {
		super(n);
		bonusBP = b;
	}

	public String getName() {
		return cardName;
	}

	public int getBonesBP() {
		return bonusBP;
	}

}