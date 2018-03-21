package com.QuestCardGame.GameMain;

public class CourtCalledToCamelot extends EventEffect{
  public CourtCalledToCamelot(Game g){
    super(g);
  }

  public eventBehavior(){
    game.clearAllAllies();
  }
}
