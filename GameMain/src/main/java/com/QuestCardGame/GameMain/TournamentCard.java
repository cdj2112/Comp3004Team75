package com.QuestCardGame.GameMain;

public class TournamentCard extends Card{
	
	private int bonesBP;
	
	TournamentCard(String n, int BP){
		super(n);
		bonesBP = BP;
	}
	
	public String getName() {
		return cardName;
	}
	
	public int getBonesBP() {
		return bonesBP;
	}

}
