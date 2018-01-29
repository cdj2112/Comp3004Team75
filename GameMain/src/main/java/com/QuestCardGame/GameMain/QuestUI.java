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
	private HashMap<Integer, EventHandler<MouseEvent>> dragListener;
	private Card draggingCard;
	private ArrayList<Hotspot> hotspots;
	private HotspotBehaviourFactory behaviourFactory;

	QuestUI(Game g) throws FileNotFoundException {
		super();
		
		game = g;
		cardAssets = new HashMap<Integer, Group>();
		hotspots = new ArrayList<Hotspot>();
		dragListener = new HashMap<Integer, EventHandler<MouseEvent>>();
		behaviourFactory = new HotspotBehaviourFactory(game, this);
		
		Image pic = new Image(new FileInputStream("./src/resources/card back blue.png"));
		ImageView img = new ImageView(pic);
		img.setFitHeight(100);
		img.setFitWidth(100);

		EventHandler<MouseEvent> clicked = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				Card newCard = game.playerDrawAdventureCard(game.getPlayer());
				Group newCardGroup = makeNewCardGroup(newCard);
				getChildren().add(newCardGroup);
				cardAssets.put(newCard.getId(), newCardGroup);
				update();
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
		for(Hotspot h : hotspots) {
			boolean hit = h.checkColision(x, y);
			if(hit) {
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
		Text t = new Text(40, 40, "" + c.getId());
		t.setFont(new Font(20));
		g.getChildren().addAll(r, t);
		
		final Card eventCard = c;
		final Group eventGroup = g;
		EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				draggingCard = eventCard;
				double x = e.getSceneX();
				double y = e.getSceneY();
				eventGroup.setTranslateX(x-50);
				eventGroup.setTranslateY(y-50);
				update();
			}
		};
		EventHandler<MouseEvent> mouseUp = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(draggingCard != eventCard) {
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
			if(c == draggingCard) continue;
			Group g = findCardGroup(c);
			if(g == null) {
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
			if(c == draggingCard) continue;
			Group g = findCardGroup(c);
			if(g == null) {
				g = makeNewCardGroup(c);
				getChildren().add(g);
				cardAssets.put(c.getId(), g);
			}
			g.setTranslateY(500);
			g.setTranslateX(xOffset * 110.0);
			xOffset++;
		}

	}

	public Group findCardGroup(Card c) {
		return cardAssets.get(c.getId());
	}
	
	public EventHandler<MouseEvent> findCardListener(Card c){
		return dragListener.get(c.getId());
	}

}
