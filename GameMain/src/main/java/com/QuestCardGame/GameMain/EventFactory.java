package com.QuestCardGame.GameMain;

import java.util.HashMap;

public class EventFactory {

	private HashMap<String, EventEffect> effects;
	
	public EventFactory(Game g) {
		effects = new HashMap<String, EventEffect>();
		effects.put("Chivalrous Deed", new ChivalrousDeed(g));
		effects.put("Pox", new Pox(g));
		effects.put("Plague", new Plague(g));
		effects.put("King's Recognition", new KingsRecognition(g));
		effects.put("Queen's Favor", new QueensFavor(g));
		effects.put("Court Called To Camelot", new CourtCalledToCamelot(g));
		effects.put("King's Call To Arms", new CourtCalledToCamelot(g));
		effects.put("Prosperity Throughout The Realm", new ProsperityThroughoutTheRealm(g));
	}
	
	public void effectCard(EventCard c) {
		c.setEffect(effects.get(c.getName()));
	}
}
