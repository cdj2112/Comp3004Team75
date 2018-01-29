package com.QuestCardGame.GameMain;

public class QuestCard extends Card{
	
	private String name;
	private int numStages;
	
	QuestCard(String n, int nStages){
		name = n;
		numStages = nStages;
	}
	
	public String getName() {
		return name;
	}
	
	public int getStages() {
		return numStages;
	}

}
