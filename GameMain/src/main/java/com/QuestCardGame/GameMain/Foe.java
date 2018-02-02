package com.QuestCardGame.GameMain;

public class Foe extends AdventureCard{

  private int battlePoint1;
  private int battlePoint2;

  public Foe(String name, int bp){
    super(name, AdventureType.FOE);
    battlePoint1 = battlePoint2 = bp;
  }

  public Foe(String name, int bp1, int bp2){
    super(name, AdventureType.FOE);

    //the greater number is signed to battlePoint2
    if(bp1 > bp2){
      battlePoint2 = bp1;
      battlePoint1 = bp2;
    }else {
      battlePoint1 = bp1;
      battlePoint2 = bp2;
    }
  }

  public int getBattlePoint(String questTarget){
    if (questTarget == this.getName() || battlePoint1 == battlePoint2) return battlePoint2;
    else return battlePoint1;
  }

}
