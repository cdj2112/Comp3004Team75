package com.QuestCardGame.GameMain;

public class FoeCard extends Card {

	private String name;
	private int Strength;
    private int battlePoints;
    
    FoeCard(String n, int s, int bp) {
		super();
		name =n;
		Strength =s;
		battlePoints =bp;
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
