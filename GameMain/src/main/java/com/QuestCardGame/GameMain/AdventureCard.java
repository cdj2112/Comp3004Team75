package com.QuestCardGame.GameMain;

public abstract class AdventureCard extends Card{
  public enum AdventureType {WEAPON, FOE, ALLY, AMOURS,TEST};
  private AdventureType adventureType;

	public AdventureCard(String name, AdventureType at) {
		super(name);
		adventureType = at;
	}

  public AdventureType getCardType(){return adventureType;}
  
  public String getBackImagePath() {
	  return "./src/resources/Cards/Backs/Adventure.jpg";
  }
  
  public abstract int getBattlePoint(Boolean b);
  
}
