package com.QuestCardGame.GameMain;

public class Ally extends AdventureCard {

	//private int base;
	private int effectBonusForBid;
	private int effectBonusForBP;
	private String target;
	private int battlePoints;
	private int bids;
	
	public Ally(String name, int battlePoints, int bids, int effectBonusForBid, int effectBonusForBP, String target) {
		super(name, AdventureType.ALLY);
		this.battlePoints = battlePoints;
		this.bids = bids;
		this.effectBonusForBid = effectBonusForBid;
		this.effectBonusForBP = effectBonusForBP;
		this.target = target;
	}
	
	public String getTarget() {
		return target;
	}

	public int getBid(boolean bonus) {
		int temp = bids;
		if(bonus) temp = temp + effectBonusForBid;	
		return temp;
	}
	public int getBattlePoint(boolean bonus) {
		int temp = battlePoints;
		if(bonus) temp = temp + effectBonusForBP;	
		return temp;
	}
	
	public String getFrontImagePath() {
		  return "./src/resources/Cards/Ally/"+cardName+".png";
	}

	public String getUrlPath() {
		return "/Cards/Ally/"+cardName+".png";
	}
}
