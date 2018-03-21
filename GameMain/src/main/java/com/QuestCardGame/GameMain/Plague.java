package com.QuestCardGame.GameMain;

public class Plague extends EventEffect{
  public Plague(Game g){
    super(g);
  }

  public void eventBehavior(){
    game.getPlayer(game.getCurrentActivePlayer()).addShields(-2);
  }
}
