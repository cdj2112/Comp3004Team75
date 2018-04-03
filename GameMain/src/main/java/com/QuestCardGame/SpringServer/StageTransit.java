package com.QuestCardGame.SpringServer;

import java.io.Serializable;
import java.util.ArrayList;

import com.QuestCardGame.GameMain.AdventureCard;
import com.QuestCardGame.GameMain.Stage;

public class StageTransit implements Serializable {
	private CardTransit[] cards;
	private int battlePoints;
	
	public StageTransit(Stage s) {
		ArrayList<AdventureCard> c = s.getCards();
		cards = new CardTransit[c.size()];
		for(int i=0; i<cards.length; i++) {
			cards[i] = new CardTransit(c.get(i));
		}
		battlePoints = s.getBattlePoints();
	}
}
