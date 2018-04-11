package com.QuestCardGame.GameMain;

public class Ally extends AdventureCard {

	//private int base;
	private int bonusForBid;
	private int bonusForBP;
	private String target;
	private int battlePoints;
	private int bids;
	private PlayFinder find = null;
	
	public Ally(String name, int battlePoints, int bids, int bFB, int bFBP, String target) {
		super(name, AdventureType.ALLY);
		this.battlePoints = battlePoints;
		this.bids = bids;
		this.bonusForBid = bFB;
		this.bonusForBP = bFBP;
		this.target = target;
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
		if(bonus) temp = bonusForBid;	
		return temp;
	}
	
	public int getBattlePoint(boolean bonus) {
		if(find != null) bonus = find.find();
		int temp = battlePoints;
		if(bonus) temp = bonusForBP;	
		return temp;
	}
	
	public String getFrontImagePath() {
		  return "./src/resources/Cards/Ally/"+cardName+".png";
	}

	public String getUrlPath() {
		return "/Cards/Ally/"+cardName+".png";
	}
}
