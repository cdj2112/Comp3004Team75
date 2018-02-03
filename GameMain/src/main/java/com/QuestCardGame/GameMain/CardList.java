package com.QuestCardGame.GameMain;
import java.util.Map;
import java.util.HashMap;

public class CardList{
  private Map<String, Integer> adventureList;
  private Map<String, Integer> storyList;
//Integer is stand for the amount of copies of a same card


  public CardList(){
    adventureList = new HashMap<String, Integer>();
    storyList = new HashMap<String, Integer>();
    generateList();
  }

  public Map<String, Integer> getAdventureList() {return adventureList;}
  public Map<String, Integer> getStoryList() {return storyList;}

  private void generateList(){
    //hardcode for now, may get from text file later
    /*Adventure Card Initialization*/
    //Allies
    adventureList.put("Sir Galahad", 1);
    adventureList.put("Sir Lancelot",1);
    adventureList.put("King Arthur",1);
    adventureList.put("Sir Tristan",1);
    adventureList.put("King Pellinore", 1);
    adventureList.put("Sir Gawain",1);
    adventureList.put("Sir Pervival",1);
    adventureList.put("Queen Guinevere",1);
    adventureList.put("Queen Iseult",1);
    adventureList.put("Merlin",1);
    //Weapons
    adventureList.put("Excalibur", 2);
    adventureList.put("Lance",6);
    adventureList.put("Battle-ax",8);
    adventureList.put("Sword",16);
    adventureList.put("Horse",11);
    adventureList.put("Dagger",6);
    //Amours
    adventureList.put("Amour",8);
    //Foes
    adventureList.put("Dragon",1);
    adventureList.put("Giant",2);
    adventureList.put("Mordred",4);
    adventureList.put("Green Knight",2);
    adventureList.put("Black Knight",3);
    adventureList.put("Evil Knight",6);
    adventureList.put("Saxon Knight",8);
    adventureList.put("Robber Knight",7);
    adventureList.put("Saxons", 5);
    adventureList.put("Boar",4);
    adventureList.put("Thieves",8);
    //Tests
    adventureList.put("Test of Valor",2);
    adventureList.put("Test of Temptation",2);
    adventureList.put("Test of Morgan Le Fey",2);
    adventureList.put("Test of the Questing Beast",2);
    /*Adventure Card Initialization finished*/

    /*Story Card Initialization*/
    //Quests
    storyList.put("Search for the Holy Grail",1);
    storyList.put("Test of the Green Knight", 1);
    storyList.put("Search for the Questing Beast", 1);
    /*Strry Card Initialization finished*/

  }
  public void updateList(){
    //not yet implenmented
  }
}
