package com.QuestCardGame.GameMain;

public abstract class StoryCard extends Card {

	StoryCard(String name){
		super(name);
	}
	
	public abstract int getShieldReward();
}
