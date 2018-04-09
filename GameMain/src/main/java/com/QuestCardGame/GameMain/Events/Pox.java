package com.QuestCardGame.GameMain.Events;

import com.QuestCardGame.GameMain.EventEffect;
import com.QuestCardGame.GameMain.Game;

public class Pox extends EventEffect{
  public Pox (Game g){
    super(g);
  }

  public void eventBehavior(){
    for (int i = 0; i < game.getNumPlayers(); i++){
      if (i != game.getCurrentActivePlayer()) game.getPlayer(i).addShields(-1);
    }
  }
}
