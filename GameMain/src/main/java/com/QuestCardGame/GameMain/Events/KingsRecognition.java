package com.QuestCardGame.GameMain.Events;

import com.QuestCardGame.GameMain.EventEffect;
import com.QuestCardGame.GameMain.Game;

public class KingsRecognition extends EventEffect {
	public KingsRecognition(Game g) {
		super(g);
	}

	public boolean eventBehavior() {
		game.setExtraShield(2);
		return false;
	}
}
