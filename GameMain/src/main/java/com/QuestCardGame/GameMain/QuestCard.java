package com.QuestCardGame.GameMain;

public class QuestCard extends Card {

	private int numStages;

	QuestCard(String n, int nStages) {
		super(n);
		numStages = nStages;
	}

	public String getName() {
		return cardName;
	}

	public int getStages() {
		return numStages;
	}

	public String getFrontImagePath() {
		return "./src/resources/Cards/Quest/" + cardName + ".png";
	}

	public String getBackImagePath() {
		return "./src/resources/Cards/Backs/Story.png";
	}
}
