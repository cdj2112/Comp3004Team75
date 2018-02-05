package com.QuestCardGame.GameMain;

public abstract class Card {

	private static int nextId = 0;
	protected int id;
	protected String cardName;

	Card(String name) {
		id = nextId++;
		cardName = name;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return cardName;
	}
	public abstract int getBattlePoint();
}
