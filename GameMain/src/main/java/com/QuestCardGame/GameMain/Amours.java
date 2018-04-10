package com.QuestCardGame.GameMain;

public class Amours extends AdventureCard {
	private int battlePoint;
	private int bid;

	public Amours() {
		super("Amour", AdventureType.AMOURS);
		battlePoint = 10;
		bid = 1;
	}

	public int getBid(boolean bonus) {
		return bid;
	}

	public int getBattlePoint(boolean b) {
		return battlePoint;
	}

	public String getFrontImagePath() {
		return "./src/resources/Cards/Armour/" + cardName + ".png";
	}

	public String getUrlPath() {
		return "/Cards/Armour/" + cardName + ".png";
	}

}
