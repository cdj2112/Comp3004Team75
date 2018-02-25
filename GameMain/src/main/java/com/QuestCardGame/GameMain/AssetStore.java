package com.QuestCardGame.GameMain;

import java.io.FileInputStream;
import java.util.HashMap;
import javafx.scene.image.Image;

public class AssetStore {
	public static HashMap<Integer, CardGroup> cardGroups = new HashMap<Integer, CardGroup>();
	public static HashMap<String, Image> imageStore = new HashMap<String, Image>();

	private static CardGroup makeNewCardGroup(Card c) {
		CardGroup cg = new CardGroup(c);
		cardGroups.put(c.getId(), cg);
		return cg;
	}

	public static CardGroup getCardGroup(Card c) {
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
