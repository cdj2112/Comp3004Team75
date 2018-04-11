package com.QuestCardGame.GameMain;

import junit.framework.TestCase;
import java.util.ArrayList;

public class QuestTest extends TestCase{
	
	
	public void testIsValidQuestIncreasingBattlePoints() {			
		QuestCard c = new QuestCard("OneStageQuest", 2, null);
		Quest quest = new Quest(c, 0);
		AdventureCard f1 = new Foe("TestFoe1", 1);
		AdventureCard w1 = new Weapon("Dagger", 1);
		AdventureCard f2 = new Foe("TestFoe2", 2);
		AdventureCard w2 = new Weapon("Dagger", 2);
		quest.addCardToStage(f1, 0);
		quest.addCardToStage(w1, 0);
		quest.addCardToStage(f2, 1);
		quest.addCardToStage(w2, 1);
		
		assert quest.validateQuest();
	}
	
	public void testIsValidQuestDecreasingBattlePoints() {			
		QuestCard c = new QuestCard("OneStageQuest", 2, null);
		Quest quest = new Quest(c, 0);
		AdventureCard f1 = new Foe("TestFoe1", 10);
		AdventureCard w1 = new Weapon("Dagger", 1);
		AdventureCard f2 = new Foe("TestFoe2", 2);
		AdventureCard w2 = new Weapon("Dagger", 2);
		quest.addCardToStage(f1, 0);
		quest.addCardToStage(w1, 0);
		quest.addCardToStage(f2, 1);
		quest.addCardToStage(w2, 1);
		
		assert !quest.validateQuest();
	}
	
	/**
	 * Two stage quest
	 * P1 wins gets 2 shields (i.e. number of stages)
	 * P2 is eliminated first round
	 */
	public void testEvaluateStage() {
		QuestCard c = new QuestCard("OneStageQuest", 2, null);
		Quest quest = new Quest(c, 0);
		AdventureCard f1 = new Foe("TestFoe1", 6);
		AdventureCard w1 = new Weapon("Dagger", 1);
		AdventureCard f2 = new Foe("TestFoe2", 10);
		AdventureCard w2 = new Weapon("Dagger", 2);
		AdventureCard w3 = new Weapon("Axe", 10);
		quest.addCardToStage(f1, 0);
		quest.addCardToStage(w1, 0);
		quest.addCardToStage(f2, 1);
		quest.addCardToStage(w2, 1);
		Player p1 = new Player();
		Player p2 = new Player();
		p1.drawCard(w2);
		p1.drawCard(w3);
		p2.drawCard(w1);
		quest.addPlayer(p1);
		quest.addPlayer(p2);
		quest.startQuest();
		
		//p1 plays card for stage 1
		Player nextPlayer = quest.getNextPlayer();		
		assert nextPlayer == p1;		
		nextPlayer.playCard(w2);
		
		//p2 plays card for stage 1
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == p2;
		nextPlayer.playCard(w1);
		
		//playing round is done
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == null;
		
		//p1 should win stage 1
		nextPlayer = quest.getNextPlayer();
		boolean result = quest.evaluatePlayer(nextPlayer);
		assert result;
		
		//p2 should lose stage 1
		nextPlayer = quest.getNextPlayer();
		result = quest.evaluatePlayer(nextPlayer);
		assert !result;
		
		//eval round is done
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == null;

		//p1 plays stage 2
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == p1;
		nextPlayer.playCard(w3);
		
		//playing round is done
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == null;
		
		//p1 wins stage
		nextPlayer = quest.getNextPlayer();
		result = quest.evaluatePlayer(nextPlayer);
		assert result;
		
		//p1 is award 2 shields
		assert quest.isQuestOver();
		assert p1.getNumShields() == 2;
	}

}
