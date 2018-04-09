package com.QuestCardGame.GameMain.Events;

import com.QuestCardGame.GameMain.EventEffect;
import com.QuestCardGame.GameMain.Game;

public class ProsperityThroughoutTheRealm extends EventEffect {
	public ProsperityThroughoutTheRealm(Game g) {
		super(g);
	}

	public boolean eventBehavior() {
		for (int i = 0; i < game.getNumPlayers(); i++) {
			game.playerDrawAdventureCard(game.getPlayer(i));
			game.playerDrawAdventureCard(game.getPlayer(i));
		}
		return false;
	}
}
