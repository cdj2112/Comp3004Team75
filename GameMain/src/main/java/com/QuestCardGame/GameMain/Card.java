package com.QuestCardGame.GameMain;

public class abstract Card {

	private static int nextId = 0;
	private int id;
	
	Card() {
		id = nextId++;
	}

	public int getId() {
		return id;
	}
}
