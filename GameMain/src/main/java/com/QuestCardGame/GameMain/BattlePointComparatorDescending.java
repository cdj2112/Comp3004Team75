package com.QuestCardGame.GameMain;

import java.util.Comparator;

public class BattlePointComparatorDescending implements Comparator<AdventureCard> {
		public int compare(AdventureCard arg0, AdventureCard arg1) {
			return Integer.compare(arg1.getBattlePoint(false), arg0.getBattlePoint(false));
		}
}
