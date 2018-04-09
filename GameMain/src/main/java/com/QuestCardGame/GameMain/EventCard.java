package com.QuestCardGame.GameMain;

public class EventCard extends StoryCard {
	
	private EventEffect effect;

	public EventCard(String n) {
		super(n);
	}

	public int getShieldReward() {
		return 0;
	}
	
	public void setEffect(EventEffect e) {
		effect = e;
	}

	public String getFrontImagePath() {
		return "./src/resources/Cards/Event/" + cardName + ".png";
	}

	public String getBackImagePath() {
		return "./src/resources/Cards/Backs/Story.png";
	}

	public String getUrlPath() {
		return "Cards/Event/" + cardName + ".png";
	}
}
