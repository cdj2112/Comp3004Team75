package com.QuestCardGame.GameMain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.*;

public class PlayerGroup extends Group {
	private Group hand;
	private Group play;
	private ImageView rank;
	private Text battlePoints;
	private Text cardsInHand;
	private ImageView[] shields;

	public PlayerGroup() throws FileNotFoundException {
		super();
		hand = new Group();
		play = new Group();
		rank = new ImageView();
		battlePoints = new Text();
		cardsInHand = new Text();

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

		getChildren().add(cardsInHand);
		cardsInHand.setFont(new Font(15));
		cardsInHand.setTranslateX(0);
		cardsInHand.setTranslateY(115);

		Image shield = new Image(new FileInputStream("./src/resources/shield.png"));
		shields = new ImageView[9];
		for (int i = 0; i < 9; i++) {
			shields[i] = new ImageView(shield);
			shields[i].setFitWidth(25);
			shields[i].setFitHeight(37.5);
			getChildren().add(shields[i]);
		}

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
		battlePoints.setText("Battle Points: " + bp);
	}

	public Text getBP() {
		return battlePoints;
	}

	public void setCardsInHand(int num, boolean visible) {
		cardsInHand.setText("Cards In Hand: " + num);
		cardsInHand.setVisible(visible);
	}

	public void arrangeShieldsGrid() {
		for (int i = 0; i < 9; i++) {
			ImageView s = shields[i];
			s.setFitWidth(25);
			s.setFitHeight(37.5);
			s.setTranslateX(1130 + (i % 3) * 25);
			s.setTranslateY(Math.floor(i / 3) * 38);
		}
	}

	public void arrangeShieldLine() {
		for (int i = 0; i < 9; i++) {
			ImageView s = shields[i];
			s.setFitWidth(15);
			s.setFitHeight(22.5);
			s.setTranslateX(i * 15);
			s.setTranslateY(80);
		}
	}

	public void setShields(int s) {
		for (int i = 0; i < 9; i++) {
			shields[i].setVisible(i<s);
		}
	}
}
