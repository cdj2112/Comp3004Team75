package com.QuestCardGame.SpringServer;

import java.util.ArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.QuestCardGame.GameMain.Game;

@SpringBootApplication
public class QuestSpringApplication {

	private static ArrayList<String> userNames = new ArrayList<String>();
	private static int[] aiStrategy = new int[4];
	private static int numAI = 0;
	private static boolean rigged = false;

	private static Game game = null;

	public static Game getGame() {
		return game;
	}

	public static boolean isGameStarted() {
		return game != null;
	}

	public static int addPlayer(String name) {
		if (userNames.size() < 4) {
			aiStrategy[userNames.size()] = 0;
			userNames.add(name);
			return userNames.size() - 1;
		} else {
			return -1;
		}
	}

	public static void addAIPlayer() {
		if (userNames.size() < 4) {
			aiStrategy[userNames.size()] = 1;
			numAI++;
			userNames.add("CP " + numAI);
		}
	}

	public static void setRigged(boolean b) {
		rigged = b;
	}

	public static void changeAiStrategy(int idx, int newStrat) {
		if (aiStrategy[idx] != 0)
			aiStrategy[idx] = newStrat;
	}

	public static PlayerListTransit getPlayerList() {
		return new PlayerListTransit(userNames, aiStrategy, rigged);
	}

	public static void startGame() {
		game = new Game(userNames.size(), aiStrategy, rigged);
	}

	public static void main(String[] args) {
		SpringApplication.run(QuestSpringApplication.class, args);
	}

}