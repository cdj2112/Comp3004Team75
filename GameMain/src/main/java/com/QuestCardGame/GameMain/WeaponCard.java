package com.QuestCardGame.GameMain;

public class WeaponCard extends Card {

	private String name;
	private int battlePoints;

	WeaponCard(String n, int bp) {
		super();
		name = n;
		battlePoints = bp;
	}
	
	public int getBattlePoints(){
		return battlePoints;
	}
}
