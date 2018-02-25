package com.QuestCardGame.GameMain;

import javafx.scene.Group;

public class PlayerGroup extends Group {
	private Group hand;
	private Group play;
	
	public PlayerGroup() {
		super();
		hand = new Group();
		play = new Group();
		
		getChildren().add(play);
		play.setTranslateX(0);
		play.setTranslateY(0);
		
		getChildren().add(hand);
		hand.setTranslateX(0);
		hand.setTranslateY(100);
		
	}
	
	public void addCardToHand(Group g) {
		hand.getChildren().add(g);
	}
	
	public void playCard(Group g) {
		hand.getChildren().remove(g);
		play.getChildren().add(g);
	}
	
	public void removeCardFromHand(Group g) {
		hand.getChildren().remove(g);
	}
	
	public Group getHand() {
		return hand;
	}
	
	public Group getPlay() {
		return play;
	}
}
