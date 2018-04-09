package com.QuestCardGame.GameMain;

public class TournamentCard extends StoryCard {

	private int bonusShields;

	TournamentCard(String n, int b) {
		super(n);
		bonusShields = b;
	}

	public String getName() {
		return cardName;
	}

	public int getBonusShields() {
		return bonusShields;
	}
	
	public int getShieldReward() {
		return bonusShields + 1;
	}

	public String getFrontImagePath() {
		return "./src/resources/Cards/Tournament/" + cardName + ".png";
	}

	public String getBackImagePath() {
		return "./src/resources/Cards/Backs/Story.png";
	}
	
	public String getUrlPath() {
		return "/Cards/Tournament/"+cardName+".png";
	}
	
	public String getBackUrlPath() {
		return "/Cards/Backs/Story.png";
	}

}