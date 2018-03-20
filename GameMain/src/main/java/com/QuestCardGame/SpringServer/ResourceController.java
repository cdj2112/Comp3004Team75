package com.QuestCardGame.SpringServer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	 @RequestMapping(value = "/makeNewGame", method = RequestMethod.POST)
	 @ResponseBody
	 public String makeNewGame() {
		 return QuestSpringApplication.makeNewGame();
	 }
}
