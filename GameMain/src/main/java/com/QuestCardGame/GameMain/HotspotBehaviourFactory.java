package com.QuestCardGame.GameMain;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class HotspotBehaviourFactory {

	private Game game;
	private QuestUI gameUI;

	HotspotBehaviourFactory(Game g, QuestUI ui) {
		game = g;
		gameUI = ui;
	}

	public final HotspotBehaviour playCard = new HotspotBehaviour() {
		public void onHit(Card c) {
			int active = game.getCurrentActivePlayer();
			boolean play = game.playerPlayCard(game.getPlayer(active), (AdventureCard) c);
			if (play) {
				Group cardGroup = gameUI.findCardGroup(c);
				gameUI.getPlayerGroup(active).playCard(cardGroup);
			}
		}
	};
	
	public final HotspotBehaviour discardCard = new HotspotBehaviour() {
		public void onHit(Card c) {
			System.out.println("Discard");
			int active = game.getCurrentActivePlayer();
			game.playerDiscardAdventrueCard(game.getPlayer(active), (AdventureCard) c);
			Group cardGroup = gameUI.findCardGroup(c);
			Group g = (Group) cardGroup.getParent();
			if(g != null) g.getChildren().remove(cardGroup);
		}
	};

	public Object getPlayToStage(final int s) {
		return new HotspotBehaviour() {
			public void onHit(Card c) {
				boolean play = game.sponsorAddCardToStage((AdventureCard) c, s);
				if (play) {
					Group cardGroup = gameUI.findCardGroup(c);
					gameUI.getPlayerGroup(game.getCurrentActivePlayer()).removeCardFromHand(cardGroup);
					gameUI.getStageGroup(s).addCardGroup(cardGroup);
				}
			}
		};
	}
}
