package com.QuestCardGame.GameMain;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class QuestCardGame extends Application
{
	public void start(Stage stage) throws Exception {
		Game game = new Game();
		//game.playerDrawAdventureCard(game.getPlayer());
		Group root = new QuestUI(game);
		Scene scene = new Scene(root, 700, 700);
		
		stage.setScene(scene);
		stage.setTitle("Click and Drag");
		stage.show();
	}
	
    public static void main( String[] args )
    {
    	launch(args);
    }
}
