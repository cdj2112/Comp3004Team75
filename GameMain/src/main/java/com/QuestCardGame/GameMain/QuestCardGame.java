package com.QuestCardGame.GameMain;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class QuestCardGame extends Application {
	public void start(Stage stage) throws Exception {
		stage.setMaximized(true);
		stage.show();
		stage.setResizable(false);
		Game game = new Game();
		Group root = new QuestUI(game, stage.getHeight(), stage.getWidth());
		Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
		stage.setScene(scene);
		stage.setTitle("Quest");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
