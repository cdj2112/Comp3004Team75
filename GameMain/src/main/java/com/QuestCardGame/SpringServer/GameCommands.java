package com.QuestCardGame.SpringServer;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.QuestCardGame.GameMain.AdventureCard;
import com.QuestCardGame.GameMain.Card;
import com.QuestCardGame.GameMain.Game;
import com.QuestCardGame.GameMain.Player;


@Controller
public class GameCommands {

	@MessageMapping("/drawStoryCard")
	@SendTo("/status/gameStatus")
	public GameTransit drawStoryCard() {
		Game game = QuestSpringApplication.getGame();
		game.playTurn();
		return buildGameStatus();
	}
	
	@RequestMapping(value = "/gameStatus", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public GameTransit buildGameStatus() {
		if(!QuestSpringApplication.isGameStarted()) return null;
		return new GameTransit(QuestSpringApplication.getGame());
	}
}
