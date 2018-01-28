package com.QuestCardGame.GameMain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

public class QuestUI extends Group {

	private Game game;
	private HashMap<Integer, Group> cardAssets;
	private Card draggingCard;

	QuestUI(Game g) throws FileNotFoundException {
		super();
		game = g;
		cardAssets = new HashMap<Integer, Group>();
		Image pic = new Image(new FileInputStream("./src/resources/card back blue.png"));
		ImageView img = new ImageView(pic);
		img.setFitHeight(100);
		img.setFitWidth(100);

		EventHandler<MouseEvent> clicked = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				Card newCard = game.playerDraw();
				Group newCardGroup = makeNewCardGroup(newCard);
				getChildren().add(newCardGroup);
				cardAssets.put(newCard.getValue(), newCardGroup);
				update();
			}
		};
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, clicked);

		getChildren().add(img);
		update();
	}
	
	private Group makeNewCardGroup(Card c) {
		Group g = new Group();
		Rectangle r = new Rectangle();
		r.setHeight(100);
		r.setWidth(100);
		r.setFill(Color.RED);
		Text t = new Text(40, 40, "" + c.getValue());
		t.setFont(new Font(20));
		g.getChildren().addAll(r, t);
		
		final Card eventCard = c;
		final Group eventGroup = g;
		EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				draggingCard = eventCard;
				double x = e.getSceneX();
				double y = e.getSceneY();
				eventGroup.setTranslateX(x);
				eventGroup.setTranslateY(y);
				update();
			}
		};
		EventHandler<MouseEvent> mouseUp = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				draggingCard = null;
				update();
			}
		};
		
		g.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragged);
		g.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseUp);
		
		return g;
	}

	public void update() {
		Player p = game.getPlayer();
		ArrayList<Card> pHand = p.getHand();
		int xOffset = 0;
		for (Card c : pHand) {
			if(c == draggingCard) continue;
			Group g = findCardGroup(c);
			if(g == null) {
				g = makeNewCardGroup(c);
				getChildren().add(g);
				cardAssets.put(c.getValue(), g);
			}
			g.setTranslateY(600);
			g.setTranslateX(xOffset * 110.0);
			xOffset++;
		}

	}

	public Group findCardGroup(Card c) {
		return cardAssets.get(c.getValue());
	}

}
