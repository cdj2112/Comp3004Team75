package com.QuestCardGame.GameMain;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardGroup extends Group {

	private boolean faceUp = true;
	private ImageView face;
	private ImageView back;
	private boolean canHover;
	private boolean canDrag;

	public CardGroup(Card c) {
		super();
		Image frontImg = AssetStore.getImage(c.getFrontImagePath());
		Image backImg = AssetStore.getImage(c.getBackImagePath());

		face = new ImageView(frontImg);
		back = new ImageView(backImg);
		face.setFitHeight(150);
		face.setFitWidth(100);
		back.setFitHeight(150);
		back.setFitWidth(100);

		face.setVisible(true);
		back.setVisible(false);

		getChildren().add(face);
		getChildren().add(back);
	}

	public void setFaceUpDown(boolean faceUp) {
		face.setVisible(faceUp);
		back.setVisible(!faceUp);
	}

	public void setDragCard(boolean drag) {
		canDrag = drag;
	}

	public boolean getDragCard() {
		return canDrag;
	}

	public void setHoverCard(boolean hover) {
		canHover = hover;
	}

	public boolean getHoverCard() {
		return canHover;
	}
}
