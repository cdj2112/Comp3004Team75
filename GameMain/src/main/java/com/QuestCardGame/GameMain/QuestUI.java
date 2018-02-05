package com.QuestCardGame.GameMain;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
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
	private HashMap<Integer, EventHandler<MouseEvent>> dragListener;
	private Card draggingCard;
	private ArrayList<Hotspot> hotspots;
	private HotspotBehaviourFactory behaviourFactory;
	boolean round = false;

	QuestUI(Game g) throws FileNotFoundException {
		super();

		game = g;
		cardAssets = new HashMap<Integer, Group>();
		hotspots = new ArrayList<Hotspot>();
		dragListener = new HashMap<Integer, EventHandler<MouseEvent>>();
		behaviourFactory = new HotspotBehaviourFactory(game, this);

		Image pic = new Image(
				new FileInputStream("./src/resources/card back blue.png"));
		ImageView img = new ImageView(pic);
		img.setFitHeight(100);
		img.setFitWidth(100);

		// next round not yet implenmented
		Button button = new Button("end turn");
		EventHandler<MouseEvent> btn = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				round = true;
			}
		};
		button.setLayoutX(100);
		button.setLayoutY(10);
		getChildren().add(button);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, btn);

		EventHandler<MouseEvent> clicked = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				Card newCard = null;
				if(round == true) {
				newCard = game.playerDrawAdventureCard(game.getPlayer());
				}
				round = false;
				if (newCard != null) {
					Group newCardGroup = makeNewCardGroup(newCard);
					getChildren().add(newCardGroup);
					cardAssets.put(newCard.getId(), newCardGroup);
					update();
				}
			}
		};
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, clicked);

		Hotspot hitbox = new Hotspot();
		hitbox.setHeight(100);
		hitbox.setWidth(600);
		hitbox.setStroke(Color.RED);
		hitbox.setFill(Color.TRANSPARENT);
		hitbox.setTranslateY(500);
		hitbox.setAction(behaviourFactory.playCard);
		hotspots.add(hitbox);

		getChildren().add(img);
		getChildren().add(hitbox);
		update();
	}

	private void dropCard(double x, double y) {
		for (Hotspot h : hotspots) {
			boolean hit = h.checkColision(x, y);
			if (hit) {
				h.executeAction(draggingCard);
			}
		}
	}

	private Group makeNewCardGroup(Card c) {
		Group g = new Group();
		Rectangle r = new Rectangle();
		r.setHeight(100);
		r.setWidth(100);
		r.setFill(Color.RED);

		Text t = new Text(20, 40, "" + c.getId() + " " + c.getName());
		t.setFont(new Font(10));
		g.getChildren().addAll(r, t);

		final Card eventCard = c;
		final Group eventGroup = g;
		EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				draggingCard = eventCard;
				double x = e.getSceneX();
				double y = e.getSceneY();
				eventGroup.setTranslateX(x - 50);
				eventGroup.setTranslateY(y - 50);
				update();
			}
		};
		EventHandler<MouseEvent> mouseUp = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (draggingCard != eventCard) {
					return;
				}
				dropCard(e.getSceneX(), e.getSceneY());
				System.out.println("Card Up");
				draggingCard = null;
				update();
			}
		};

		g.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragged);
		dragListener.put(c.getId(), dragged);
		g.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseUp);

		return g;
	}

	public void update() {
		Player p = game.getPlayer();
		ArrayList<Card> pHand = p.getHand();
		int xOffset = 0;
		for (Card c : pHand) {
			if (c == draggingCard)
				continue;
			Group g = findCardGroup(c);
			if (g == null) {
				g = makeNewCardGroup(c);
				getChildren().add(g);
				cardAssets.put(c.getId(), g);
			}
			g.setTranslateY(600);
			g.setTranslateX(xOffset * 110.0);
			xOffset++;
		}

		ArrayList<Card> pPlay = p.getPlay();
		xOffset = 0;
		for (Card c : pPlay) {
			if (c == draggingCard)
				continue;
			Group g = findCardGroup(c);
			if (g == null) {
				g = makeNewCardGroup(c);
				getChildren().add(g);
				cardAssets.put(c.getId(), g);

			}
			g.setTranslateY(500);
			g.setTranslateX(xOffset * 110.0);
			xOffset++;
		}

		Rectangle r = new Rectangle();
		r.setHeight(50);
		r.setWidth(300);
		r.setFill(Color.WHITE);
		r.setX(5.0);
		r.setY(160.0);
		Text t = new Text();
		t.setCache(true);
		t.setX(10.0);
		t.setY(200.0);
		t.setFill(Color.BLACK);
		t.setText("Player 1: " + p.CardAmount() + " cards");
		t.setFont(new Font(24));

		Rectangle r1 = new Rectangle();
		r1.setHeight(60);
		r1.setWidth(300);
		r1.setFill(Color.WHITE);
		r1.setX(10.0);
		r1.setY(230.0);
		Text t1 = new Text();
		t1.setCache(true);
		t1.setX(10.0);
		t1.setY(260.0);
		t1.setFill(Color.BLACK);
		t1.setText("Player 1 BP: " + p.getBattlePoint() + ".");
		t1.setFont(new Font(24));

		getChildren().add(r);
		getChildren().add(t);
		getChildren().add(r1);
		getChildren().add(t1);
	}

	public Group findCardGroup(Card c) {
		return cardAssets.get(c.getId());
	}

	public EventHandler<MouseEvent> findCardListener(Card c) {
		return dragListener.get(c.getId());
	}

}
