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
public class ResourceController {

	@GetMapping("/")
	public String home(@RequestParam(name = "joined", required = false, defaultValue = "false") boolean joined,
			@RequestParam(name = "player", required = false, defaultValue = "0") int player, Model model) {
		model.addAttribute("joined", joined);
		model.addAttribute("player", player);
		boolean gameStarted = QuestSpringApplication.isGameStarted();
		return gameStarted ? "gameStarted" : "index";
	}

	@GetMapping("/gamePage")
	public String gamePage(@RequestParam(name = "player", required = false, defaultValue = "0") int player,
			Model model) {
		model.addAttribute("player", player);
		boolean gameStarted = QuestSpringApplication.isGameStarted();
		return gameStarted ? "gamePage" : "gameNotStarted";
	}

	@RequestMapping(value = "/addNewPlayer", method = RequestMethod.POST)
	@ResponseBody
	public int addNewPlayer(@RequestBody NewPlayerRequest npr) {
		sendPlayerList();
		return QuestSpringApplication.addPlayer(npr.getName());
	}

	@RequestMapping(value = "/addAIPlayer", method = RequestMethod.POST)
	@ResponseBody
	public void addAIPlayer() {
		QuestSpringApplication.addAIPlayer();
	}

	@MessageMapping("/changeAIStrategy")
	@SendTo("status/playerList")
	public PlayerListTransit changeAIStrategy(AIChangeRequest acr) {
		QuestSpringApplication.changeAiStrategy(acr.getIndex(), acr.getNewStrategy());
		return sendPlayerList();
	}

	@MessageMapping("/changeRigged")
	@SendTo("status/playerList")
	public PlayerListTransit changeRigged(boolean b) {
		QuestSpringApplication.setRigged(b);
		return sendPlayerList();
	}

	@GetMapping("/playerList")
	@ResponseBody
	@MessageMapping("/updatePlayers")
	@SendTo("/status/playerList")
	public PlayerListTransit sendPlayerList() {
		return QuestSpringApplication.getPlayerList();
	}

	@MessageMapping("/startGame")
	@SendTo("/status/gameStart")
	public String startGame() {
		System.out.println(">>>Start Game");
		QuestSpringApplication.startGame();
		return "Start";
	}

}
