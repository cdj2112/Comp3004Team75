package com.QuestCardGame.GameMain;

public class AmourCard extends Card {

	private String name;
	private int battlePoints;

	AmourCard(int x) {
		super();
		if(x == 0) {
		name = "Amour1";
		battlePoints = 10;
		}else if (x == 1) {
			name = "Amour2";
		    battlePoints = 20;
		}else if (x == 2) {
			name = "Amour3";
		    battlePoints = 30;
		}
	}
	
	public int getBattlePoints(){
		return battlePoints;
	}
	
	public String getName(){
		return name;
	}
}
