package com.QuestCardGame.GameMain;

public class AllieCard extends Card {

	private String name;
    
	AllieCard(String n) {
		super();
		name = n;
    }
	
    public String getName(){
		return name;
	}
}