package com.QuestCardGame.GameMain;

import java.util.Comparator;

public class AdventureCardComparator implements Comparator<AdventureCard> {

	public int compare(AdventureCard arg0, AdventureCard arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}

}
