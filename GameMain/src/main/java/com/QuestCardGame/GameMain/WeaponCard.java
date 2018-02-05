package com.QuestCardGame.GameMain;

public class WeaponCard extends Card {

	// private String name;
	private int battlePoints;

	WeaponCard(String n, int bp) {
		super(n);
		battlePoints = bp;
	}

	public int getBattlePoint() {
		return battlePoints;
	}

}
