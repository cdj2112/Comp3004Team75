package com.QuestCardGame.GameMain;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class Hotspot extends Rectangle{
    public boolean checkColision(double x, double y) {
    	double h = getHeight();
    	double w = getWidth();
    	Point2D point = sceneToLocal(x,y);
    	return w >= point.getX() && h >= point.getY();
    }
}
