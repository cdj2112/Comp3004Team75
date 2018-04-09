package com.QuestCardGame.SpringServer;

import java.io.Serializable;

import com.QuestCardGame.GameMain.Card;

public class CardTransit implements Serializable {
	private int id;
	private String name;
	private String url;
	private String backUrl;

	public CardTransit(Card c) {
		id = c.getId();
		name = c.getName();
		url = c.getUrlPath();
		backUrl = c.getBackUrlPath();
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
	
	public String getBackUrl() {
		return backUrl;
	}
}
