package com.QuestCardGame.GameMain;

public class Card {

	private static int nextId = 0;
	private int id;
	private String name;
	
	Card() {
		id = nextId++;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
