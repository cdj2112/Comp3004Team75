package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand extends ArrayList<AdventureCard>{

	public void sortDescendingByBattlePoints() {
		Collections.sort(this, new Comparator<AdventureCard>() {
			public int compare(AdventureCard a, AdventureCard b) {
				return Integer.compare(b.getBattlePoint(false), a.getBattlePoint(false));
			}
		});
	}
}
