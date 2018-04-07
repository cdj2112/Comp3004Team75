package com.QuestCardGame.GameMain;

public class TournamentCard extends Card {

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

	public String getFrontImagePath() {
		return "./src/resources/Cards/Tournament/" + cardName + ".png";
	}

	public String getBackImagePath() {
		return "./src/resources/Cards/Backs/Story.png";
	}
	
	public String getUrlPath() {
		return "/Cards/Tournament/"+cardName+".png";
	}

}