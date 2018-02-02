package com.QuestCardGame.GameMain;

public class AdventureCard extends Card{
  public enum AdventureType {WEAPON, FOE, ALLY, AMOUR,TEST};
  private AdventureType adventureType;

  public AdventureCard(String name, AdventureType at){
      super(name);
      adventureType = at;
  }
  
  public AdventureType getCardType(){return adventureType;}
}
