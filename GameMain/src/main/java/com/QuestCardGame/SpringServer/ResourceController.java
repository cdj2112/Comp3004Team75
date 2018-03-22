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
    public String home() {
        return "index";
    }
	
	 @RequestMapping(value = "/addNewPlayer", method = RequestMethod.POST)
	 @ResponseBody
	 public int addNewPlayer(@RequestBody NewPlayerRequest npr) {
		 sendPlayerList();
		 return QuestSpringApplication.addPlayer(npr.getName());
	 }
	 
	 @MessageMapping("/updatePlayers")
	 @SendTo("/status/playerList")
	 public String[] sendPlayerList() {
		 return QuestSpringApplication.getPlayerList();
	 }
	 
}
