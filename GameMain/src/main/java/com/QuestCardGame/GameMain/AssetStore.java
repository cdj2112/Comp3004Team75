package com.QuestCardGame.GameMain;

import java.io.FileInputStream;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class AssetStore {
	public HashMap<Integer, CardGroup> cardGroups = new HashMap<Integer, CardGroup>();
	private HashMap<Integer, EventHandler<MouseEvent>> dragListener = new HashMap<Integer, EventHandler<MouseEvent>>();

	public static HashMap<String, Image> imageStore = new HashMap<String, Image>();

	private QuestUI UI;

	public AssetStore(QuestUI qUI) {
		UI = qUI;
	}

	private CardGroup makeNewCardGroup(Card c) {
		CardGroup cg = new CardGroup(c);

		final Card eventCard = c;
		final CardGroup eventGroup = cg;
		EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (!eventGroup.getDragCard())
					return;
				UI.setDraggingCard(eventCard);
				double x = e.getX();
				double y = e.getY();
				Point2D p = eventGroup.localToParent(x, y);
				eventGroup.setTranslateX(p.getX() - 50);
				eventGroup.setTranslateY(p.getY() - 50);
				UI.update();
			}
		};
		EventHandler<MouseEvent> mouseEnter = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (!eventGroup.getHoverCard())
					return;
				UI.setHoverCard(eventCard);
				UI.update();
			}
		};
		EventHandler<MouseEvent> mouseLeave = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (!eventGroup.getHoverCard())
					return;
				UI.setHoverCard(null);
				UI.update();
			}
		};
		EventHandler<MouseEvent> mouseUp = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (UI.getDraggingCard() != eventCard) {
					return;
				}
				UI.dropCard(e.getSceneX(), e.getSceneY());
				System.out.println("Card Up");
				UI.setDraggingCard(null);
				UI.update();
			}
		};

		cg.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragged);
		cg.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseUp);
		cg.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnter);
		cg.addEventHandler(MouseEvent.MOUSE_EXITED, mouseLeave);
		dragListener.put(c.getId(), dragged);
		cardGroups.put(c.getId(), cg);
		return cg;
	}

	public EventHandler<MouseEvent> getCardListener(Card c) {
		return dragListener.get(c.getId());
	}

	public CardGroup getCardGroup(Card c) {
		CardGroup cg = cardGroups.get(c.getId());
		if (cg != null) {
			return cg;
		} else {
			return makeNewCardGroup(c);
		}
	}

	private static Image loadNewImage(String path) {
		Image img = null;
		try {
			img = new Image(new FileInputStream(path));
		} catch (Exception e) {
			System.out.println(e);
		}
		if (img != null) {
			imageStore.put(path, img);
		}

		return img;
	}

	public static Image getImage(String path) {
		Image img = imageStore.get(path);
		if (img != null) {
			return img;
		} else {
			return loadNewImage(path);
		}
	}
}
