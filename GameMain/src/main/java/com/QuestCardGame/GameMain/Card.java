package com.QuestCardGame.GameMain;

import java.util.HashMap;

public abstract class Card {

	private static int nextId = 0;
	private static HashMap<Integer, Card> idMap = new HashMap<Integer, Card>();
	
	public static Card getCard(int i) {
		return idMap.get(i);
	}
	
	protected int id;
	protected String cardName;

	Card(String name) {
		id = nextId++;
		cardName = name;
		idMap.put(id, this);
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return cardName;
	}
	
	public abstract String getFrontImagePath();
	public abstract String getBackImagePath();
	public abstract String getUrlPath();
}
