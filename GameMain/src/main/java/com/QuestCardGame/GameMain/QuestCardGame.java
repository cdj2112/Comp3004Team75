package com.QuestCardGame.GameMain;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.text.*;

public class QuestCardGame extends Application {

	private Stage mainStage;
	private Scene menuStage;
	private final ComboBox playerNumber = new ComboBox();
	private final ComboBox aiNumber = new ComboBox();
	private CheckBox rigged;

	public void startGame(int numPlay, int numAi) throws FileNotFoundException{

		mainStage.setMaximized(true);
		mainStage.show();
		mainStage.setResizable(false);
		System.out.println();
		int[] aiStrat = {0,0,0,0}; //all human
		Game game = new Game(numPlay, aiStrat, rigged.isSelected());
		Group root = new QuestUI(game, mainStage.getHeight(), mainStage.getWidth());
		Scene scene = new Scene(root, mainStage.getWidth(), mainStage.getHeight());
		mainStage.setScene(scene);
		mainStage.setTitle("Quest");

	}

	public void start(Stage stage) throws Exception {
        mainStage = stage;
		Group root = new Group();
		menuStage = new Scene(root, 500, 500);

		Text playerText = new Text();
		playerText.setFont(new Font(20));
		playerText.setText("# of Players");
		playerText.setTranslateX(50);
		playerText.setTranslateY(270);
		root.getChildren().add(playerText);

		playerNumber.getItems().addAll(2, 3, 4);
		playerNumber.setValue(4);
		playerNumber.setTranslateX(170);
		playerNumber.setTranslateY(250);
		root.getChildren().add(playerNumber);

		Text aiText = new Text();
		aiText.setFont(new Font(20));
		aiText.setText("# of AI Players");
		aiText.setTranslateX(250);
		aiText.setTranslateY(270);
		root.getChildren().add(aiText);

		aiNumber.getItems().addAll(0, 1, 2, 3);
		aiNumber.setValue(0);
		aiNumber.setTranslateX(400);
		aiNumber.setTranslateY(250);
		root.getChildren().add(aiNumber);
		
		rigged = new CheckBox("Rigged");
		rigged.setTranslateX(220);
		rigged.setTranslateY(300);
		root.getChildren().add(rigged);

		Button startGame = new Button();
		startGame.setText("Start Game");
		startGame.setTranslateX(210);
		startGame.setTranslateY(400);
		startGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				int numPlayers = (Integer) playerNumber.getValue();
				int aiPlayers = (Integer) aiNumber.getValue();
				if (numPlayers > aiPlayers) {
					try {
						startGame(numPlayers, aiPlayers);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		root.getChildren().add(startGame);

		stage.setScene(menuStage);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
