package com.QuestCardGame.SpringServer;

import java.util.ArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.QuestCardGame.GameMain.Game;

@SpringBootApplication
public class QuestSpringApplication {

	private static ArrayList<String> userNames = new ArrayList<String>();

	public static int addPlayer(String name) {
		userNames.add(name);
		return userNames.size() - 1;
	}
	
	public static String[] getPlayerList() {
		String[] sA = new String[userNames.size()];
		return userNames.toArray(sA);
	}

	public static void main(String[] args) {
		SpringApplication.run(QuestSpringApplication.class, args);
	}

}