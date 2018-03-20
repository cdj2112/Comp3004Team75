package com.QuestCardGame.SpringServer;

import java.util.HashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.QuestCardGame.GameMain.Game;

@SpringBootApplication
public class QuestSpringApplication {

	private static HashMap<String, Game> activeGames = new HashMap<String, Game>();

	public static String makeNewGame() {
		String code = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			char c = (char) (Math.floor(Math.random() * 25) + 64);
			sb.append(c);
		}
		code = sb.toString();
		activeGames.put(code, new Game(4, 0, false));
		return code;
	}

	public static void main(String[] args) {
		SpringApplication.run(QuestSpringApplication.class, args);
	}

}