package com.QuestCardGame.SpringServer;

public class NewPlayerRequest {

	private String name;

	public NewPlayerRequest() {

	}

	public NewPlayerRequest(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
