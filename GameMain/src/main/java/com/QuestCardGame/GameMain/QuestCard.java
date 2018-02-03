package com.QuestCardGame.GameMain;

public class QuestCard extends Card{
	
	private int numStages;
	
	QuestCard(String n, int nStages){
		super(n);
		numStages = nStages;
	}
	
	public String getName() {
		return cardName;
	}
	
	public int getStages() {
		return numStages;
	}

}
