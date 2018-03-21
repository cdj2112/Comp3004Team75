package com.QuestCardGame.GameMain;

import java.util.ArrayList;

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

    for (int i = 1; i < game.getNumPlayers(); i++){
      if(game.getPlayer(i).getNumShields() > leastShield) continue;
      else if (game.getPlayer(i).getNumShields() == leastShield) leastShieldPlayers.add(game.getPlayer(i));
      else {
        leastShieldPlayers.clear();
        leastShieldPlayers.add(game.getPlayer(i));
        leastShield = game.getPlayer(i).getNumShields();
      }
    }

    for (Player p : leastRankedPlayers){
      if (leastShieldPlayers.contains(p)) p.addShields(3);
    }
  }
}
