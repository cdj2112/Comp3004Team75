package com.QuestCardGame.GameMain;

import junit.framework.TestCase;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Scenario1Test extends TestCase{
	public void testScenario1() {
  	HashMap<Integer, ArrayList<AdventureCard>> testDeck = new HashMap<Integer, ArrayList<AdventureCard>>();
  	ArrayList<AdventureCard> p1Test = new ArrayList<AdventureCard>();
  	Foe saxons = new Foe("Saxons", 10,20);
  	p1Test.add(saxons);
  	Foe boar = new Foe("Boar", 5, 15);
  	p1Test.add(boar);
  	Weapon sword = new Weapon("Sword",10);
  	p1Test.add(sword);
		Weapon dagger = new Weapon("Dagger", 5);
		p1Test.add(dagger);
  	testDeck.put(0, p1Test);
  	QuestCard boarHunt = new QuestCard("Boar Hunt", 2);
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

	}
}
