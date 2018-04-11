package com.QuestCardGame.GameMain.Events;

import java.util.ArrayList;

import com.QuestCardGame.GameMain.AdventureCard;
import com.QuestCardGame.GameMain.AdventureCard.AdventureType;
import com.QuestCardGame.GameMain.EventEffect;
import com.QuestCardGame.GameMain.Game;
import com.QuestCardGame.GameMain.Hand;
import com.QuestCardGame.GameMain.Player;

public class KingsCallToArms extends EventEffect {
	public KingsCallToArms(Game g) {
		super(g);
	}

	public boolean eventBehavior() {
		ArrayList<Player> highestRanked = new ArrayList<Player>();
		int highestRank = -1;
		for (int i = 0; i < game.getNumPlayers(); i++) {
			if (game.getPlayer(i).getRank() < highestRank)
				continue;
			else if (game.getPlayer(i).getRank() == highestRank)
				highestRanked.add(game.getPlayer(i));
			else {
				highestRanked.clear();
				highestRanked.add(game.getPlayer(i));
				highestRank = game.getPlayer(i).getRank();
			}
		}

		for (Player p : highestRanked) {
			Hand h = p.getHand();
			int numWeapons = 0;
			int numFoes = 0;
			for (int i = 0; i < h.size(); i++) {
				AdventureCard ac = h.get(i);
				if (ac.getCardType() == AdventureType.WEAPON) {
					numWeapons++;
				} else if (ac.getCardType() == AdventureType.FOE) {
					numFoes++;
				}
			}

			if (numWeapons > 0) {
				game.setSpecialDiscard(game.getPlayerIndex(p), AdventureType.WEAPON, 1);
			} else {
				game.setSpecialDiscard(game.getPlayerIndex(p), AdventureType.FOE, Math.min(numFoes, 2));
			}
		}
		return true;
	}
}
