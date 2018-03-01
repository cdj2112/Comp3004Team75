package com.QuestCardGame.GameMain;

public class TournamentCard extends Card {

	private int bonesShiled;

	TournamentCard(String n, int b) {
		super(n);
		bonesShiled = b;
	}

	public String getName() {
		return cardName;
	}

	public int getBonesShiled() {
		return bonesShiled;
	}

	public String getFrontImagePath() {
		return "./src/resources/Cards/Tournament/" + cardName + ".png";
	}

	public String getBackImagePath() {
		return "./src/resources/Cards/Backs/Story.png";
	}

}