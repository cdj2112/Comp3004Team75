package com.QuestCardGame.GameMain;

public class AmourCard extends Card {

	private String name;
	private int battlePoints;

	AmourCard(String n, int x) {
		super();
		name = n;
		battlePoints = x;
	}
	
	public int getBattlePoints(){
		return battlePoints;
	}
	
	public String getName(){
		return name;
	}
}
