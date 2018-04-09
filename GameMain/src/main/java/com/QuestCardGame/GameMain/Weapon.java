package com.QuestCardGame.GameMain;

public class Weapon extends AdventureCard{ 
 
  private int battlePoint; 
 
  public Weapon(String name, int bp){ 
    super(name, AdventureType.WEAPON); 
    battlePoint = bp; 
  } 
 
  public int getBattlePoint(boolean b){return battlePoint;}
  public int getBid(boolean b) {return 0;}
  
  public String getFrontImagePath() {
	  return "./src/resources/Cards/Weapon/"+cardName+".png";
  }
 
} 
