package com.QuestCardGame.GameMain;

public class EventCard extends Card{
  public EventCard(String n){
    super(n);
  }
  public String getFrontImagePath() {
    return "./src/resources/Cards/Event/" + cardName + ".png";
  }
  public String getBackImagePath() {
    return "./src/resources/Cards/Backs/Story.png";
  }
}
