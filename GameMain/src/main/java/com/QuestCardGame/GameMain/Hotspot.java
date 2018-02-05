package com.QuestCardGame.GameMain;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class Hotspot extends Rectangle {

	private HotspotBehaviour action;

	public boolean checkColision(double x, double y) {
		double h = getHeight();
		double w = getWidth();
		Point2D point = sceneToLocal(x, y);
		double pX = point.getX();
		double pY = point.getY();
		return pX <= w && pY <= h && pX >= 0 && pY >= 0;
	}

	public void setAction(HotspotBehaviour a) {
		action = a;
	}

	public void executeAction(Card c) {
		action.onHit(c);
	}
}
