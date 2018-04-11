package com.QuestCardGame.GameMain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EventEffect {
	protected static final Logger logger = LogManager.getLogger(EventCard.class);
	protected Game game;

	public EventEffect(Game g) {
		game = g;
	}

	public abstract boolean eventBehavior();
}
