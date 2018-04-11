package com.QuestCardGame.GameMain;

public class QuestCard extends StoryCard {

	private int numStages;
	private String target;

	QuestCard(String n, int nStages, String t) {
		super(n);
		numStages = nStages;
		target = t;
	}

	public String getName() {
		return cardName;
	}

	public int getStages() {
		return numStages;
	}
	
	public String getTarget() {
		return target;
	}

	public String getFrontImagePath() {
		return "./src/resources/Cards/Quest/" + cardName + ".png";
	}

	public String getBackImagePath() {
		return "./src/resources/Cards/Backs/Story.png";
	}
	
	public int getShieldReward() {
		return numStages;
	}
	
	public String getUrlPath() {
		return "/Cards/Quest/" + cardName + ".png";
	}

}
