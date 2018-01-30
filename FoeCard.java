package com.QuestCardGame.GameMain;

public class FoeCard extends Card {

	private String name;
	private int Strength;
    private int battlePoints;
    
    FoeCard(int x) {
		super();
		if(x == 0) {
		name = "Dragon";
		battlePoints = 10;
		Strength = 5;
		}else if (x == 1) {
			name = "Giant";
		    battlePoints = 20;
		    Strength = 10;
		}else if (x == 2) {
			name = "Mordred";
		    battlePoints = 0;
		    Strength = 0;
		}else if (x == 3) {
			name = "greenKnight";
			Strength = 20;
		}else if (x == 4) {
			name = "BlackKnight";
		    battlePoints = 40;
		    Strength = 25;
		}else if (x == 5) {
			name = "EvilKnight";
		    battlePoints = 60;
		    Strength = 30;
		}else if (x == 6) {
			name = "SaxonKnight";
		    battlePoints = 70;
		    Strength = 30;
		}else if (x == 7) {
			name = "RobberKnight";
		    battlePoints = 80;
		    Strength = 30;
		}else if (x == 8) {
			name = "Saxons";
		    battlePoints = 80;
		    Strength = 30;
		}else if (x == 9) {
			name = "Boar";
		    battlePoints = 80;
		    Strength = 30;
		}else if (x == 10) {
			name = "Thieves";
		    battlePoints = 80;
		    Strength = 30;
		}
	}
	
	public int getBattlePoints(){
		return battlePoints;
	}


    public int getStrength(){
	    return Strength;
}
    public String getName(){
		return name;
	}
}
