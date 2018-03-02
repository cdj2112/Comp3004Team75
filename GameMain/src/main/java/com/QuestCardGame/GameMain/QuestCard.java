package com.QuestCardGame.GameMain;

public class QuestCard extends Card{

	private int numStages;
	private String foe;

	QuestCard(String n, int nStages, String f){
		super(n);
		numStages = nStages;
		foe = f;
	}

	public String getName() {
		return cardName;
	}

	public int getStages() {
		return numStages;
	}

	public String getFoe(){
		return foe;
	}

}
