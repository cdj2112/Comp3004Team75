package com.QuestCardGame.GameMain;

import junit.framework.TestCase;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Scenario1Test extends TestCase{
  Map<Integer, ArrayList<AdventureCard>> testDeck = new HashMap<Integer, ArrayList<AdventureCard>>();
  ArrayList<AdventureCard> p1Test = new ArrayList<AdventureCard>();
  Foe saxons = new Foe("Saxons", 10,20);
  p1Test.add(saxons);
  Foe boar = new Foe("Boar", 5, 15);
  p1Test.add(boar);
  Weapon sword = new Weapon("Sword",10);
  p1Test.add(sword);
  testDeck.put(0, p1Test);
  Game testGame = new Game(testDeck);
  assert (testGame.getPlayer(0).getHand().contions(saxons));
  assert (testGame.getPlayer(0).getHand().contions(boar));
  assert (testGame.getPlayer(0).getHand().contions(sword));
}
