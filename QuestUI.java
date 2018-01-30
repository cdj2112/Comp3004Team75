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
	private HashMap<Integer, Group> hand;

	QuestUI(Game g) throws FileNotFoundException {
		super();
		game = g;
		hand = new HashMap<Integer, Group>();
		Image pic = new Image(new FileInputStream("./src/resources/card back blue.png"));
		ImageView img = new ImageView(pic);
		img.setFitHeight(100);
		img.setFitWidth(100);

		EventHandler<MouseEvent> clicked = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				game.playerDrawAdventureCard(game.getPlayer());
				update();
			}
		};
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, clicked);

		getChildren().add(img);
		update();
	}

	public void update() {
		Player p = game.getPlayer();
		ArrayList<Card> pHand = p.getHand();
		int xOffset = 0;
		for (Card c : pHand) {
			Group g = findCardGroup(c);
			if (g == null) {
				g = new Group();
				Rectangle r = new Rectangle();
				r.setHeight(100);
				r.setWidth(100);
				r.setFill(Color.RED);
				Text t = new Text(20, 40, "" + c.getId() + c.getName());
				t.setFont(new Font(10));
				g.getChildren().addAll(r, t);
				g.setTranslateY(600);
				getChildren().add(g);
				hand.put(c.getId(), g);
			}
			g.setTranslateX(xOffset * 110.0);
			xOffset++;
		}

	}

	public Group findCardGroup(Card c) {
		return hand.get(c.getId());
	}

}
