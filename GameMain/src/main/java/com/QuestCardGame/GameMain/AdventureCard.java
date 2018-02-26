package com.QuestCardGame.GameMain;

public abstract class AdventureCard extends Card{
	//keep the order of WEAPON, ALLY, AMOURS for AI sorting/playing
  public enum AdventureType {WEAPON, FOE, ALLY, AMOURS,TEST};
  private AdventureType adventureType;

  public AdventureCard(String name, AdventureType at){
      super(name);
      adventureType = at;
  }

  public AdventureType getCardType(){return adventureType;}
  
  public abstract int getBattlePoint(Boolean b);
}
