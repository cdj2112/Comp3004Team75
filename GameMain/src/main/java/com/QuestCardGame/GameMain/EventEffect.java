package com.QuestCardGame.GameMain;

public abstract class EventEffect {
  protected Game game;
  public EventEffect(Game g){
    game = g;
  }
  public abstract boolean eventBehavior();
}
