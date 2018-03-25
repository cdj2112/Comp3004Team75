package com.QuestCardGame.SpringServer;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class GameCommands {

	
	@RequestMapping(value = "/gameStatus", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public GameTransit buildGameStatus() {
		if(!QuestSpringApplication.isGameStarted()) return null;
		return new GameTransit(QuestSpringApplication.getGame());
	}
}
