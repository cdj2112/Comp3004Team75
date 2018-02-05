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
import javafx.geometry.Point2D;

import com.QuestCardGame.GameMain.Game.GameStatus;

public class QuestUI extends Group {

	private Game game;
	private HotspotBehaviourFactory behaviourFactory;

	private HashMap<Integer, Group> cardAssets;
	private HashMap<Integer, EventHandler<MouseEvent>> dragListener;
	private Card draggingCard;
	private ArrayList<Hotspot> stageHotspots;
	private Hotspot playHotspot;
	private PlayerGroup[] playerGroups;

	QuestUI(Game g) throws FileNotFoundException {
		super();

		game = g;
		cardAssets = new HashMap<Integer, Group>();
		stageHotspots = new ArrayList<Hotspot>();
		dragListener = new HashMap<Integer, EventHandler<MouseEvent>>();
		behaviourFactory = new HotspotBehaviourFactory(game, this);

		playerGroups = new PlayerGroup[game.getNumPlayers()];
		for (int i = 0; i < game.getNumPlayers(); i++) {
			playerGroups[i] = new PlayerGroup();
			playerGroups[i].setTranslateX(0);
			playerGroups[i].setTranslateY(500);
			playerGroups[i].setVisible(i==0);
			getChildren().add(playerGroups[i]);
		}

		Image storyPic = new Image(new FileInputStream("./src/resources/Cards/Backs/Story.jpg"));
		ImageView storyDeck = new ImageView(storyPic);
		storyDeck.setFitHeight(100);
		storyDeck.setFitWidth(100);

		EventHandler<MouseEvent> drawStory = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (game.getGameStatus() != GameStatus.IDLE) {
					return;
				}
				game.playTurn();
				update();
			}
		};
		storyDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, drawStory);

		playHotspot = new Hotspot();
		playHotspot.setHeight(100);
		playHotspot.setWidth(600);
		playHotspot.setStroke(Color.RED);
		playHotspot.setFill(Color.TRANSPARENT);
		playHotspot.setAction(behaviourFactory.playCard);
		playerGroups[0].getChildren().add(playHotspot);

		getChildren().add(storyDeck);
		update();
	}

	private void dropCard(double x, double y) {
		for (Hotspot h : stageHotspots) {
			h.checkColision(draggingCard, x, y);
		}
		playHotspot.checkColision(draggingCard, x, y);
	}

	private Group makeNewCardGroup(Card c) {
		Group g = new Group();
		Rectangle r = new Rectangle();
		r.setHeight(100);
		r.setWidth(100);
		r.setFill(Color.RED);
		Text t = new Text(40, 40, "" + c.getName());
		t.setFont(new Font(20));
		g.getChildren().addAll(r, t);

		final Card eventCard = c;
		final Group eventGroup = g;
		EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				draggingCard = eventCard;
				double x = e.getX();
				double y = e.getY();
				Point2D p = eventGroup.localToParent(x, y);
				eventGroup.setTranslateX(p.getX() - 50);
				eventGroup.setTranslateY(p.getY() - 50);
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

	private void repositionCards() {
		Player p = game.getPlayer();
		ArrayList<Card> pHand = p.getHand();
		int xOffset = 0;
		for (Card c : pHand) {
			if (c == draggingCard)
				continue;
			Group g = findCardGroup(c);
			if (g == null) {
				g = makeNewCardGroup(c);
				getPlayerGroup().addCardToHand(g);
				cardAssets.put(c.getId(), g);
			}
			g.setTranslateX(xOffset * 110.0);
			g.setTranslateY(0);
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
				getPlayerGroup().playCard(g);
				cardAssets.put(c.getId(), g);
			}
			g.setTranslateX(xOffset * 110.0);
			g.setTranslateY(0);
			xOffset++;
		}
	}

	private void readGameStatus() {
		GameStatus GS = game.getGameStatus();
		int stages = game.activeStages();
		
		playHotspot.setActive(GS==GameStatus.PLAYING_QUEST);
		
		int i=0;
		for(Hotspot h: stageHotspots) {
			h.setActive(GS==GameStatus.BUILDING_QUEST && i < stages);
			i++;
		}
	}
	
	public void update() {
		repositionCards();
		readGameStatus();
	}

	public Group findCardGroup(Card c) {
		return cardAssets.get(c.getId());
	}

	public EventHandler<MouseEvent> findCardListener(Card c) {
		return dragListener.get(c.getId());
	}

	public PlayerGroup getPlayerGroup() {
		return playerGroups[0];
	}

}
