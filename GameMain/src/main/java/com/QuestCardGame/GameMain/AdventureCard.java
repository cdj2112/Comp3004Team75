package com.QuestCardGame.GameMain;

public abstract class AdventureCard extends Card {
	// keep the order of WEAPON, ALLY, AMOURS for AI sorting/playing
	public enum AdventureType {
		WEAPON, FOE, ALLY, AMOURS, TEST
	};

	private AdventureType adventureType;

	public AdventureCard(String name, AdventureType at) {
		super(name);
		adventureType = at;
	}

	public AdventureType getCardType() {
		return adventureType;
	}

	public String getBackImagePath() {
		return "./src/resources/Cards/Backs/Adventure.png";
	}

	public String getBackUrlPath() {
		return "/Cards/Backs/Adventure.png";
	}

	public abstract int getBattlePoint(boolean b);
	public abstract int getBid(boolean bonus);

}
