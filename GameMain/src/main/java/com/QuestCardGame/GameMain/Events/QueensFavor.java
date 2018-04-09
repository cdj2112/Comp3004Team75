package com.QuestCardGame.GameMain.Events;

import java.util.ArrayList;

import com.QuestCardGame.GameMain.EventEffect;
import com.QuestCardGame.GameMain.Game;
import com.QuestCardGame.GameMain.Player;

public class QueensFavor extends EventEffect{
  public QueensFavor(Game g){
    super(g);
  }

  public void eventBehavior(){
    ArrayList<Player> leastRankedPlayers = new ArrayList<Player>();
    leastRankedPlayers.add(game.getPlayer(0));
    int leastRank = game.getPlayer(0).getRank();
    for (int i = 1; i < game.getNumPlayers(); i++){
      if(game.getPlayer(i).getRank() > leastRank) continue;
      else if (game.getPlayer(i).getRank() == leastRank) leastRankedPlayers.add(game.getPlayer(i));
      else {
        leastRankedPlayers.clear();
        leastRankedPlayers.add(game.getPlayer(i));
        leastRank = game.getPlayer(i).getRank();
      }
    }
    for (Player p: leastRankedPlayers){
      game.playerDrawAdventureCard(p);
      game.playerDrawAdventureCard(p);
    }
  }
}
