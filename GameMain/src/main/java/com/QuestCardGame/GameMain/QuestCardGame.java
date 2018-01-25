package com.QuestCardGame.GameMain;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class QuestCardGame extends Application
{
	public void start(Stage stage) {
		Group root = new Group();
		Scene scene = new Scene(root, 700, 700);
		
		Game game = new Game();
		
		stage.setScene(scene);
		stage.setTitle("Click and Drag");
		stage.show();
	}
	
    public static void main( String[] args )
    {
    	launch(args);
    }
}
