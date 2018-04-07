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

	@MessageMapping("/sponsorQuest")
	@SendTo("/status/gameStatus")
	public GameTransit acceptDeclineSponsor(PlayerDecisionRequest pdr) {
		Game game = QuestSpringApplication.getGame();
		if (pdr.getPlayer() == game.getCurrentActivePlayer() && pdr.getAccept()) {
			game.acceptSponsor();
		} else if (pdr.getPlayer() == game.getCurrentActivePlayer()) {
			game.declineSponsor();
		}
		return buildGameStatus();
	}

	@MessageMapping("/playToStage")
	@SendTo("/status/gameStatus")
	public GameTransit playCardToStage(PlayerCardRequest pcr) {
		Game game = QuestSpringApplication.getGame();
		Card c = Card.getCard(pcr.getCardId());
		game.sponsorAddCardToStage((AdventureCard) c, pcr.getStageNum());
		return buildGameStatus();
	}

	@MessageMapping("/finalizeQuest")
	@SendTo("/status/gameStatus")
	public GameTransit finalizeQuest() {
		Game game = QuestSpringApplication.getGame();
		game.finalizeQuest();
		return buildGameStatus();
	}

	@MessageMapping("/acceptQuest")
	@SendTo("/status/gameStatus")
	public GameTransit acceptDeclineQuest(PlayerDecisionRequest pdr) {
		Game game = QuestSpringApplication.getGame();
		Player p =game.getPlayer(pdr.getPlayer());
		game.acceptDeclineQuest(p, pdr.getAccept());
		return buildGameStatus();
	}

	@MessageMapping("/playCards")
	@SendTo("/status/gameStatus")
	public GameTransit playerPlayCard(PlayerCardRequest pcr) {
		Game game = QuestSpringApplication.getGame();
		Player p = game.getPlayer(pcr.getPlayer());
		Card c = Card.getCard(pcr.getCardId());
		game.playerPlayCard(p, (AdventureCard) c);
		return buildGameStatus();
	}
	
	@MessageMapping("/finalizePlay")
	@SendTo("/status/gameStatus")
	public GameTransit finalizePlay() {
		Game game = QuestSpringApplication.getGame();
		game.finalizePlay();
		return buildGameStatus();
	}
	
	@MessageMapping("/discardCard")
	@SendTo("/status/gameStatus")
	public GameTransit discardCard(PlayerCardRequest pcr) {
		Game game = QuestSpringApplication.getGame();
		Player p = game.getPlayer(pcr.getPlayer());
		Card c = Card.getCard(pcr.getCardId());
		game.playerDiscardAdventrueCard(p, c);
		return buildGameStatus();
	}
	
	@MessageMapping("/playStage")
	@SendTo("/status/gameStatus")
	public GameTransit evalQuestStage(PlayerDecisionRequest pdr) {
		Game game = QuestSpringApplication.getGame();
		game.evaluatePlayerEndOfStage(pdr.getPlayer());
		return buildGameStatus();
	}
	
	@MessageMapping("/acceptTournament")
	@SendTo("/status/gameStatus")
	public GameTransit acceptDeclineTournament(PlayerDecisionRequest pdr) {
		Game game = QuestSpringApplication.getGame();
		Player p = game.getPlayer(pdr.getPlayer());
		game.acceptDeclineTour(p, pdr.getAccept());
		return buildGameStatus();
	}
	
	@MessageMapping("/finalizeTournament")
	@SendTo("status/gameStatus")
	public GameTransit finalizeTournament() {
		Game game = QuestSpringApplication.getGame();
		game.finalizePlayTour();
		return buildGameStatus();
	}
	
	@MessageMapping("/playTournament")
	@SendTo("status/gameStatus")
	public GameTransit playTournament() {
		Game game = QuestSpringApplication.getGame();
		game.EvalTour();
		return buildGameStatus();
	}

	@RequestMapping(value = "/gameStatus", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public GameTransit buildGameStatus() {
		if (!QuestSpringApplication.isGameStarted())
			return null;
		return new GameTransit(QuestSpringApplication.getGame());
	}
}
