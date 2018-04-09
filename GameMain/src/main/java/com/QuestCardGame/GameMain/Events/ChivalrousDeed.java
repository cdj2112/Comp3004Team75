package com.QuestCardGame.GameMain.Events;

import java.util.ArrayList;

import com.QuestCardGame.GameMain.EventEffect;
import com.QuestCardGame.GameMain.Game;
import com.QuestCardGame.GameMain.Player;

public class ChivalrousDeed extends EventEffect{
  public ChivalrousDeed(Game g){
    super(g);
  }
  public void eventBehavior() {
    //Players with both lowest rand and least amount of shields,
    //recieves 3 shields.
    ArrayList<Player> leastRankedPlayers = new ArrayList<Player>();
    ArrayList<Player> leastShieldPlayers = new ArrayList<Player>();
    int leastRank = game.getPlayer(0).getRank();
    leastRankedPlayers.add(game.getPlayer(0));
    int leastShield = game.getPlayer(0).getNumShields();
    leastShieldPlayers.add(game.getPlayer(0));

    for (int i = 1; i < game.getNumPlayers(); i++){
      if(game.getPlayer(i).getRank() > leastRank) continue;
      else if (game.getPlayer(i).getRank() == leastRank) leastRankedPlayers.add(game.getPlayer(i));
      else {
        leastRankedPlayers.clear();
        leastRankedPlayers.add(game.getPlayer(i));
        leastRank = game.getPlayer(i).getRank();
      }
    }

    for (int i = 1; i < leastRankedPlayers.size(); i++){
      if(leastRankedPlayers.get(i).getNumShields() > leastShield) continue;
      else if (leastRankedPlayers.get(i).getNumShields() == leastShield) leastShieldPlayers.add(leastRankedPlayers.get(i));
      else {
        leastShieldPlayers.clear();
        leastShieldPlayers.add(leastRankedPlayers.get(i));
        leastShield = leastRankedPlayers.get(i).getNumShields();
      }
    }

    for (Player p : leastShieldPlayers){
      p.addShields(3);
    }
  }
}
