package com.QuestCardGame.GameMain;

public class Amours extends AdventureCard{
  private int battlePoint;
  private int bid;

  public Amours(){
    super("Amours", AdventureType.AMOURS);
    battlePoint = 10;
    bid = 1;
  }

  public int getBid() {return bid;}
  public int getBattlePoint(Boolean b) {return battlePoint;}
}
