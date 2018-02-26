package com.QuestCardGame.GameMain;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.*;

public class PlayerGroup extends Group {
	private Group hand;
	private Group play;
	private ImageView rank;
	private Text battlePoints;

	public PlayerGroup() {
		super();
		hand = new Group();
		play = new Group();
		rank = new ImageView();
		battlePoints = new Text();

		getChildren().add(play);
		play.setTranslateX(0);
		play.setTranslateY(0);

		getChildren().add(hand);
		hand.setTranslateX(0);
		hand.setTranslateY(150);
		
		getChildren().add(rank);
		rank.setFitWidth(75);
		rank.setFitHeight(112.5);
		
		getChildren().add(battlePoints);
		battlePoints.setFont(new Font(20));
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

	public void setRankImage(String path) {
		Image img = AssetStore.getImage(path);
		rank.setImage(img);
	}
	
	public ImageView getRankImage() {
		return rank;
	}
	
	public void setBP(int bp) {
		battlePoints.setText("Battle Points: "+bp);
	}
	
	public Text getBP() {
		return battlePoints;
	}
}
