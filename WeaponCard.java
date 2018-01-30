package com.QuestCardGame.GameMain;

public class WeaponCard extends Card {

	private String name;
	private int battlePoints;

	WeaponCard(int x) {
		super();
		if(x == 0) {
		name = "Excalibur";
		battlePoints = 10;
		}else if (x == 1) {
			name = "Lance";
		    battlePoints = 20;
		}else if (x == 2) {
			name = "Battex-ax";
		    battlePoints = 30;
		}else if (x == 3) {
			name = "sword";
		    battlePoints = 40;
		}else if (x == 4) {
			name = "horse";
		    battlePoints = 40;
		}else if (x == 5) {
			name = "Dagger";
		    battlePoints = 60;
		}
	}
	
	public int getBattlePoints(){
		return battlePoints;
	}
	
	public String getName(){
		return name;
	}
}
