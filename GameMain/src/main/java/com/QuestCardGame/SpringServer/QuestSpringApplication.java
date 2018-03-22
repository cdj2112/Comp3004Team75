package com.QuestCardGame.SpringServer;

import java.util.ArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.QuestCardGame.GameMain.Game;

@SpringBootApplication
public class QuestSpringApplication {

	private static ArrayList<String> userNames = new ArrayList<String>();
	private static boolean[] isAIPlayer = new boolean[4];
	private static int numAI = 0;

	private static Game game;

	public static int addPlayer(String name) {
		if (userNames.size() < 4) {
			userNames.add(name);
			return userNames.size() - 1;
		} else {
			return -1;
		}
	}

	public static void addAIPlayer() {
		if (userNames.size() < 4) {
			isAIPlayer[userNames.size()] = true;
			numAI++;
			userNames.add("CPU "+numAI);
		}
	}

	public static String[] getPlayerList() {
		String[] sA = new String[userNames.size()];
		return userNames.toArray(sA);
	}

	public static void startGame() {
		game = new Game(userNames.size(), numAI, false);
	}

	public static void main(String[] args) {
		SpringApplication.run(QuestSpringApplication.class, args);
	}

}