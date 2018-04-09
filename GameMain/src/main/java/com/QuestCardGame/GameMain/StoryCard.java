package com.QuestCardGame.GameMain;

public abstract class StoryCard extends Card {

	StoryCard(String name){
		super(name);
	}
	
	public String getBackUrlPath() {
		return "/Cards/Backs/Story.png";
	}
	
	public abstract int getShieldReward();
}
