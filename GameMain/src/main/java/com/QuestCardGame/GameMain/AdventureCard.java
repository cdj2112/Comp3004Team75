package com.QuestCardGame.GameMain;

public class AdventureCard extends Card{
  private int battlePoint;
  private int battlePoint2;
  public enum AdventureType {WEAPON, FOE, ALLY, AMOUR,TEST};
  private AdventureType adventureType;

  public AdventureCard(String name){

      super(name);
      battlePoint = battlePoint2 = 0;
      adventureType = AdventureType.TEST;
  }
  public AdventureCard(String name, int bp, AdventureType at){
    super(name, illu);
    battlePoint = battlePoint2 = bp;
    adventureType = at;
  }

  public AdventureCard(String name, int bp, int bp2; AdventureType at){
    super(name, illu);
    battlePoint = bp;
    battlePoint2 = bp2;
    adventureType = at;
  }

  public AdventureType getCardType(){return adventureType;}
  public int getBP(){return battlePoint;}



}
