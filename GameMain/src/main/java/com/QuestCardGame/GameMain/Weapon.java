package com.QuestCardGame.GameMain;

public class Weapon extends AdventureCard{ 
 
  private int battlePoint; 
 
  public Weapon(String name, int bp){ 
    super(name, AdventureType.WEAPON); 
    battlePoint = bp; 
  } 
 
  public int getBattlePoint(Boolean b){return battlePoint;} 
 
} 
