package com.QuestCardGame.SpringServer;

import java.io.Serializable;
import java.util.ArrayList;

import com.QuestCardGame.GameMain.AdventureCard;
import com.QuestCardGame.GameMain.Tournaments;

public class TournamentTransit implements Serializable{
	private CardTransit[] stash;
	
	public TournamentTransit(Tournaments t) {
		ArrayList<AdventureCard> c = t.getStash();
		stash = new CardTransit[c.size()];
		for(int i = 0; i<c.size(); i++) {
			stash[i] = new CardTransit(c.get(i));
		}
	}
	
	public CardTransit[] getStash() {
		return stash;
	}
}
