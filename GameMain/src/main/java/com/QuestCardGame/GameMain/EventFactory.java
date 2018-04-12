package com.QuestCardGame.GameMain;

import java.util.HashMap;

import com.QuestCardGame.GameMain.Events.ChivalrousDeed;
import com.QuestCardGame.GameMain.Events.CourtCalledToCamelot;
import com.QuestCardGame.GameMain.Events.KingsRecognition;
import com.QuestCardGame.GameMain.Events.Plague;
import com.QuestCardGame.GameMain.Events.Pox;
import com.QuestCardGame.GameMain.Events.ProsperityThroughoutTheRealm;
import com.QuestCardGame.GameMain.Events.QueensFavor;
import com.QuestCardGame.GameMain.Events.KingsCallToArms;

public class EventFactory {

	private HashMap<String, EventEffect> effects;
	
	public EventFactory(Game g) {
		effects = new HashMap<String, EventEffect>();
		effects.put("Chivalrous Deed", new ChivalrousDeed(g));
		effects.put("Pox", new Pox(g));
		effects.put("Plague", new Plague(g));
		effects.put("King's Recognition", new KingsRecognition(g));
		effects.put("Queen's Favor", new QueensFavor(g));
		effects.put("Court Called to Camelot", new CourtCalledToCamelot(g));
		effects.put("King's Call to Arms", new KingsCallToArms(g));
		effects.put("Prosperity Throughout the Realm", new ProsperityThroughoutTheRealm(g));
	}
	
	public void effectCard(EventCard c) {
		c.setEffect(effects.get(c.getName()));
	}
}
