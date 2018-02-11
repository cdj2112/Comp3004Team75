package com.QuestCardGame.GameMain;

import junit.framework.TestCase;
import java.util.ArrayList;

public class QuestTest extends TestCase{
	
	
	public void testIsValidQuestIncreasingBattlePoints() {			
		QuestCard c = new QuestCard("OneStageQuest", 2);
		Quest quest = new Quest(c);
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
		QuestCard c = new QuestCard("OneStageQuest", 2);
		Quest quest = new Quest(c);
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
	public void testEliminateStageLosers() {
		QuestCard c = new QuestCard("OneStageQuest", 2);
		Quest quest = new Quest(c);
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
		
		Player nextPlayer = quest.getNextPlayer();		
		assert nextPlayer == p1;		
		nextPlayer.playCard(w2);
		
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == p2;
		nextPlayer.playCard(w1);
		
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == null;
		
		ArrayList<AdventureCard> discard = quest.eliminateStageLosers();
		assert discard.contains(w1);
		assert discard.contains(w2);
		
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == p1;
		
		nextPlayer.playCard(w3);
		
		nextPlayer = quest.getNextPlayer();
		assert nextPlayer == null;
		
		discard = quest.eliminateStageLosers();
		assert discard.contains(w3);
		
		assert p1.getNumShields() == 2;
	}

}
