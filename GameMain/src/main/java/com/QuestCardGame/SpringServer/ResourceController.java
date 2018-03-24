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
    public String home(@RequestParam(name="joined", required=false, defaultValue="false") boolean joined, @RequestParam(name="player", required=false, defaultValue="0") int player, Model model) {
		model.addAttribute("joined", joined);
		model.addAttribute("player", player);
        return "index";
    }
	
	@GetMapping("/gamePage")
	public String gamePage(@RequestParam(name="player", required=false, defaultValue="0") int player, Model model) {
		model.addAttribute("player", player);
		return "gamePage";
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
	 
	 @GetMapping("/playerList")
	 @ResponseBody
	 @MessageMapping("/updatePlayers")
	 @SendTo("/status/playerList")
	 public String[] sendPlayerList() {
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
