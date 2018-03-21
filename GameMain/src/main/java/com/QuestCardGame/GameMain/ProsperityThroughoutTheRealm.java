package com.QuestCardGame.GameMain;

public class ProsperityThroughoutTheRealm extends EventEffect{
  public ProsperityThroughoutTheRealm(Game g){
    super(g);
  }
  public void eventBehavior(){
    for (int i = 0; i < game.getNumPlayers(); i++){
      game.playerDrawAdventureCard(game.getPlayer(i));
      game.playerDrawAdventureCard(game.getPlayer(i));
    }
  }
}
