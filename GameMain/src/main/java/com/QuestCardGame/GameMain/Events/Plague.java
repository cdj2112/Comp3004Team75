package com.QuestCardGame.GameMain.Events;

import com.QuestCardGame.GameMain.EventEffect;
import com.QuestCardGame.GameMain.Game;

public class Plague extends EventEffect{
  public Plague(Game g){
    super(g);
  }

  public void eventBehavior(){
    game.getCurrentActivePlayerObj().addShields(-2);
  }
}
