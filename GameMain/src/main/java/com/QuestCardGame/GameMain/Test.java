package com.QuestCardGame.GameMain;

public class Test extends AdventureCard {
	private int minBid;
	private String target;
	private int bonusMinBid;

	public Test(String name, int mb, int bmb, String targetQuest) {
		super(name, AdventureType.TEST);
		target = targetQuest;
		minBid = mb;
		bonusMinBid = bmb;
	}

  public int getMinBid(String test){
    //only for the "Searching for the questing beast"
    if (target != null && target.equals(test)) return bonusMinBid;
    return minBid;
  }
  
  public int getBid(boolean bonus) {
	  return 0;
  }
  
  public int getBattlePoint(boolean b) {
	  return 0;
  }
  
  public String getFrontImagePath() {
	  return "./src/resources/Cards/Test/"+cardName+".png";
  }
  
  public String getUrlPath() {
	  return "/Cards/Test/"+cardName+".png";
  }
}
