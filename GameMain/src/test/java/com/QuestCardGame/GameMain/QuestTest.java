package com.QuestCardGame.GameMain;

import junit.framework.TestCase;

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

}
