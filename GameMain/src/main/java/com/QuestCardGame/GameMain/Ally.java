package com.QuestCardGame.GameMain;

public class Ally extends AdventureCard {

	//private int base;
	private int effectBouns;
	private String target;
	private int battlePoints;
	private int bids;
	private PlayFinder find = null;
	
	public Ally(String name, int n1, int n2, int n3, String s) {
		super(name, AdventureType.ALLY);
		battlePoints = n1;
		bids = n2;
		effectBouns = n3;
		target = s;
	}
	
	public void setFinder(PlayFinder pf) {
		find = pf;
	}

	/*public Ally(String name, int n, String target) {
		super(name, AdventureType.ALLY);
		base = n;
		//battlePoints = n;
	}*/
	
	public String getTarget() {
		return target;
	}
	
	public int getBid(boolean bonus) {
		if(find != null) bonus = find.find();
		int temp = bids;
		if(bonus& effectBouns < 5) temp = temp + effectBouns;	
		return temp;
	}
	
	public int getBattlePoint(boolean bonus) {
		if(find != null) bonus = find.find();
		int temp = battlePoints;
		if(bonus & effectBouns > 5) temp = temp + effectBouns;	
		return temp;
		
		/*int temp = 0;
		if (base >= 5)
			temp += base;
		if (effectBouns >= 5 && bouns)
			temp += effectBouns;
		return temp;*/
	}
	
	public String getFrontImagePath() {
		  return "./src/resources/Cards/Ally/"+cardName+".png";
	}
}
