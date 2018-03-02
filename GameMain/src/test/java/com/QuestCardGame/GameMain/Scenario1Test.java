package com.QuestCardGame.GameMain;

import junit.framework.TestCase;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Scenario1Test extends TestCase{
	public void testScenario1() {
  	HashMap<Integer, ArrayList<AdventureCard>> testDeck = new HashMap<Integer, ArrayList<AdventureCard>>();
		ArrayList<AdventureCard> p1Test = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> p3Test = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> p4Test = new ArrayList<AdventureCard>();
  	Foe saxons = new Foe("Saxons", 10,20);
  	p1Test.add(saxons);
  	Foe boar = new Foe("Boar", 5, 15);
  	p1Test.add(boar);
  	Weapon sword = new Weapon("Sword",10);
  	p1Test.add(sword);
		Weapon dagger = new Weapon("Dagger", 5);
		p1Test.add(dagger);
		Weapon horse = new Weapon("Horse", 10);
		p3Test.add(horse);
		Weapon excalibur = new Weapon("Excalibur", 30);
		p3Test.add(excalibur);
		Weapon battleAx = new Weapon("Battle-ax", 15);
		p4Test.add(battleAx);
		Weapon lance = new Weapon("Lance" , 20);
		p4Test.add(lance);

  	testDeck.put(0, p1Test);
		testDeck.put(2, p3Test);
		testDeck.put(3, p4Test);
  	QuestCard boarHunt = new QuestCard("Boar Hunt", 2, "Boar");
  	Game testGame = new Game(testDeck, boarHunt);

  	assert (testGame.getPlayer(0).getHand().contains((AdventureCard)saxons));
  	assert (testGame.getPlayer(0).getHand().contains((AdventureCard)boar));
  	assert (testGame.getPlayer(0).getHand().contains((AdventureCard)sword));
  	assert (testGame.getPlayer(0).getHand().size() == 12);
  	assert (testGame.getPlayer(1).getHand().size() == 12);
  	assert (testGame.getPlayer(2).getHand().size() == 12);
  	assert (testGame.getPlayer(3).getHand().size() == 12);

  	testGame.playTurn();
  	assert (testGame.getActiveQuest().getQuestName().equals(boarHunt.getName()));



		testGame.acceptSponsor();
		testGame.sponsorAddCardToStage(saxons, 0);
		testGame.sponsorAddCardToStage(boar, 1);
		testGame.sponsorAddCardToStage(dagger,1);
		testGame.sponsorAddCardToStage(sword, 1);
		assert (testGame.getPlayer(0).getHand().size() == 8);
		if (!testGame.finalizeQuest()) return;
		testGame.acceptDeclineQuest(testGame.getPlayer(1), true);
		testGame.playerDiscardAdventrueCard(testGame.getPlayer(1), testGame.getPlayer(1).getHand().get(5));
		testGame.acceptDeclineQuest(testGame.getPlayer(2), true);
		testGame.playerDiscardAdventrueCard(testGame.getPlayer(2), testGame.getPlayer(2).getHand().get(5));

		testGame.acceptDeclineQuest(testGame.getPlayer(3), true);
		testGame.playerDiscardAdventrueCard(testGame.getPlayer(3), testGame.getPlayer(3).getHand().get(5));


		//System.out.println(testGame.getPlayer(1).getHand().size());
		//System.out.println(testGame.getPlayer(2).getHand().size());
		//System.out.println(testGame.getPlayer(3).getHand().size());
		assert (testGame.getPlayer(1).getHand().size() <= 12);
		assert (testGame.getPlayer(2).getHand().size() <= 12);
		assert (testGame.getPlayer(3).getHand().size() <= 12);

		assert (testGame.getActiveQuest().getCurrentStageBattlePoints() == 10);
		if (!testGame.playerPlayCard(testGame.getPlayer(2),horse)) return;
		if (!testGame.playerPlayCard(testGame.getPlayer(3),battleAx)) return;
		testGame.finalizePlay();//player 2 finalized
		testGame.finalizePlay();
		testGame.finalizePlay();
		testGame.evaluatePlayerEndOfStage(1);
		testGame.evaluatePlayerEndOfStage(2);
		testGame.evaluatePlayerEndOfStage(3);
		assert (testGame.getActiveQuest().getCurrentStageBattlePoints() == 30);
		//player 2 is eliminated
		assert (!testGame.getActiveQuest().getPlayers().contains(testGame.getPlayer(1)));
		assert (testGame.getActiveQuest().getPlayers().contains(testGame.getPlayer(2)));
		assert (testGame.getActiveQuest().getPlayers().contains(testGame.getPlayer(3)));
		assert (testGame.getPlayer(2).getHand().size() == 12);
		assert (testGame.getPlayer(3).getHand().size() == 12);

		//stage 2 play
		if (!testGame.playerPlayCard(testGame.getPlayer(2),excalibur)) return;
		if (!testGame.playerPlayCard(testGame.getPlayer(3),lance)) return;
		testGame.finalizePlay();
		testGame.finalizePlay();
		testGame.evaluatePlayerEndOfStage(2);
		testGame.evaluatePlayerEndOfStage(3);
		testGame.playerDiscardAdventrueCard(testGame.getPlayer(0), testGame.getPlayer(0).getHand().get(1));
		testGame.playerDiscardAdventrueCard(testGame.getPlayer(0), testGame.getPlayer(0).getHand().get(1));
		assert (testGame.getPlayer(2).getNumShields() == 2);

	}
}
