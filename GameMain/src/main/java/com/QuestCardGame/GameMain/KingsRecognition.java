package com.QuestCardGame.GameMain;

public class KingsRecognition extends EventEffect{
  public KingsRecognition(Game g){
    super(g)
  }

  public void eventBehavior(){
    game.setExtraShield(2);
  }
}
