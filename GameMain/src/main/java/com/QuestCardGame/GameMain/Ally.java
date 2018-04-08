package com.QuestCardGame.GameMain;

public class Ally extends AdventureCard {

	private int base;
	private int effectBouns;
	private String target;

	public Ally(String name, int n1, int n2, String target) {
		super(name, AdventureType.ALLY);
		base = n1;
		effectBouns = n2;
	}

	public Ally(String name, int n, String target) {
		super(name, AdventureType.ALLY);
		base = n;
	}
	public String getTarget() {
		return target;
	}
	public int getBid(Boolean bouns) {
		int temp = 0;
		if (base < 5)
			temp += base;
		if (effectBouns < 5 && bouns)
			temp += effectBouns;
		return temp;
	}
	public int getBattlePoint(Boolean bouns) {
		int temp = 0;
		if (base >= 5)
			temp += base;
		if (effectBouns >= 5 && bouns)
			temp += effectBouns;
		return temp;
	}
	
	public String getFrontImagePath() {
		  return "./src/resources/Cards/Ally/"+cardName+".png";
	}

	public String getUrlPath() {
		return "/Cards/Ally/"+cardName+".png";
	}
}
