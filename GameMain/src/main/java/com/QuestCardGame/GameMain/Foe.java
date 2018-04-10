package com.QuestCardGame.GameMain;

public class Foe extends AdventureCard {

	private int battlePoints;
	private int specialBattlePoints;

	public Foe(String name, int bp) {
		super(name, AdventureType.FOE);
		battlePoints = specialBattlePoints = bp;
	}

	public Foe(String name, int bp1, int bp2) {
		super(name, AdventureType.FOE);

		// the greater number is signed to battlePoint2
		battlePoints = bp1;
		specialBattlePoints = bp2;
	}
	
	public int getBid(boolean bonus) {
		return 0;
	}

	public int getBattlePoint(boolean questTarget) {
		if (questTarget)
			return specialBattlePoints;
		else
			return battlePoints;
	}
	
	public int getBid(boolean b) {
		return 0;
	}

	public String getFrontImagePath() {
		return "./src/resources/Cards/Foe/" + cardName + ".png";
	}
	
	public String getUrlPath() {
		return "/Cards/Foe/" + cardName + ".png";
	}

}
