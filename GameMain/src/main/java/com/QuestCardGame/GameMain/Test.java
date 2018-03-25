package com.QuestCardGame.GameMain;

public class Test extends AdventureCard {
	private int minBid;
	private String target;

	public Test(String name) {
		super(name, AdventureType.TEST);
		minBid = 0;
		target = null;
	}
	public Test(String name, int mb) {
		super(name, AdventureType.TEST);
		minBid = mb;
		target = null;
	}
	public Test(String name, String targetQuest) {
		super(name, AdventureType.TEST);
		target = targetQuest;
	}

  public int getMinBid(Boolean bouns){
    //only for the "Searching for the questing beast"
    if (target != null && bouns) return 4;
    return minBid;
  }
  
  public int getBattlePoint(Boolean b) {
	  return 0;
  }
  
  public String getFrontImagePath() {
	  return "./src/resources/Cards/Test/"+cardName+".png";
  }
  
  public String getUrlPath() {
	  return "/Cards/Test/"+cardName+".png";
  }
}
