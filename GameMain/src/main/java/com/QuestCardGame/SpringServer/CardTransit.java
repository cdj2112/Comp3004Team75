package com.QuestCardGame.SpringServer;

import java.io.Serializable;

import com.QuestCardGame.GameMain.Card;

public class CardTransit implements Serializable {
	private int id;
	private String name;
	private String url;

	public CardTransit(Card c) {
		id = c.getId();
		name = c.getName();
		url = c.getUrlPath();
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
}
