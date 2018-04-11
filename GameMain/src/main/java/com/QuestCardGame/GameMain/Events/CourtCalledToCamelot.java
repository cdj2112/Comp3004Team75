package com.QuestCardGame.GameMain.Events;

import com.QuestCardGame.GameMain.EventEffect;
import com.QuestCardGame.GameMain.Game;

public class CourtCalledToCamelot extends EventEffect {
	public CourtCalledToCamelot(Game g) {
		super(g);
	}

	public boolean eventBehavior() {
		game.clearAllAllies();
		return false;
	}
}
