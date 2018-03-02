package com.QuestCardGame.GameMain;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Hotspot extends Rectangle {

	private HotspotBehaviour action;
	private boolean active = true;
	
	public Hotspot() {
		super();
		setFill(Color.TRANSPARENT);
		setStroke(Color.RED);
	}
	
    public void checkColision(Card c, double x, double y) {
    	if (!active) return;
    	double h = getHeight();
    	double w = getWidth();
    	Point2D point = sceneToLocal(x,y);
    	double pX = point.getX();
    	double pY = point.getY();
    	Boolean hit = pX <= w && pY <= h && pX >= 0 && pY >= 0;
    	if(hit) {
    		executeAction(c);
    	}
    }
    
    public void setAction(HotspotBehaviour a) {
    	action = a;
    }
    
    public void setActive(Boolean b) {
    	active = b;
    	setStroke(b ? Color.RED : Color.TRANSPARENT);
    }
    
    private void executeAction(Card c) {
    	action.onHit(c);
    }
}
