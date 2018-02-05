package com.QuestCardGame.GameMain;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class HotspotBehaviourFactory{
  
	private Game game;
	private QuestUI gameUI;
	
	HotspotBehaviourFactory(Game g, QuestUI ui){
		game = g;
		gameUI = ui;
	}
	
	public final HotspotBehaviour playCard = new HotspotBehaviour(){
		public void onHit(Card c) {
			int active = game.activePlayer();
			game.getPlayer(active).playCard(c);			
			Group cardGroup = gameUI.findCardGroup(c);
			gameUI.getPlayerGroup(active).playCard(cardGroup);
			EventHandler<MouseEvent> drag = gameUI.findCardListener(c);
			cardGroup.removeEventHandler(MouseEvent.MOUSE_DRAGGED, drag);
		}
	};
	
	public Object getPlayToStage(final int s) {
		return new HotspotBehaviour() {
			public void onHit(Card c) {
				game.sponsorAddCardToStage(c, s);
				Group cardGroup = gameUI.findCardGroup(c);
				gameUI.getPlayerGroup(game.activePlayer()).removeCardFromHand(cardGroup);
				gameUI.getStageGroup(s).addCardGroup(cardGroup);
				EventHandler<MouseEvent> drag = gameUI.findCardListener(c);
				cardGroup.removeEventHandler(MouseEvent.MOUSE_DRAGGED, drag);
			}
		};
	}
}
