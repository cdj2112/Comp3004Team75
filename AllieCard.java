package com.QuestCardGame.GameMain;

public class AllieCard extends Card {

	private String name;
	private int Strength;
    
	AllieCard(String n, int s) {
		super();
		name = n;
		Strength = s;
    }
	
    public int getStrength(){
	    return Strength;
}
    public String getName(){
		return name;
	}
}