package com.QuestCardGame.SpringServer;

import java.io.Serializable;
import java.util.ArrayList;
import com.QuestCardGame.GameMain.AdventureCard;
import com.QuestCardGame.GameMain.Hand;
import com.QuestCardGame.GameMain.Player;

public class PlayerTransit implements Serializable {
	private CardTransit[] hand;
	private CardTransit[] play;
	private int battlePoints;
	private int shields;
	private String rank;

	public PlayerTransit(Player p, String storyName) {
		Hand h = p.getHand();
		hand = new CardTransit[h.size()];
		for (int i = 0; i < h.size(); i++) {
			hand[i] = new CardTransit(h.get(i));
		}

		ArrayList<AdventureCard> pl = p.getPlay();
		play = new CardTransit[pl.size()];
		for (int i = 0; i < pl.size(); i++) {
			play[i] = new CardTransit(pl.get(i));
		}

		battlePoints = p.getBattlePoints(storyName);
		shields = p.getNumShields();
		rank = p.getRankUrlPath();
	}

	public CardTransit[] getHand() {
		return hand;
	}

	public CardTransit[] getPlay() {
		return play;
	}

	public int getBattlePoints() {
		return battlePoints;
	}

	public int getShields() {
		return shields;
	}

	public String getRank() {
		return rank;
	}
}
