package com.QuestCardGame.GameMain;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CardGroup extends Group{
	
	private boolean faceUp = true;
	private ImageView face;
	private ImageView back;
	
    public CardGroup(Card c) {
		super();
		Image frontImg = AssetStore.getImage(c.getFrontImagePath());
		Image backImg = AssetStore.getImage(c.getBackImagePath());
		
		face = new ImageView(frontImg);
		back = new ImageView(backImg);
		face.setFitHeight(100);
		face.setFitWidth(100);
		back.setFitHeight(100);
		back.setFitWidth(100);
		
		face.setVisible(true);
		back.setVisible(false);
		
		getChildren().add(face);
		getChildren().add(back);
	}
}
