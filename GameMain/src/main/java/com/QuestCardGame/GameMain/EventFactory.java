package com.QuestCardGame.GameMain;

public class EventFactory{
  EventCard eventCard;
  EventEffect effect;
  public EventFactory(EventCard c, Game g){
    eventCard = c;
    String cardName= c.getName();
  //  if (cardName.toLowerCase().quals("chivalrous deed"))
//    switch (cardName.toLowerCase()()){
      if (cardName.toLowerCase().equals("chivalrous deed")) effect = new ChivalrousDeed(g);

      else if (cardName.toLowerCase().equals("pox")) effect = new Pox(g);

      else if (cardName.toLowerCase().equals("plague"))  effect = new Plague(g);

      else if (cardName.toLowerCase().equals("king's recognition"))  effect = new KingsRecognition(g);

      else if (cardName.toLowerCase().equals("queen's favor")) effect = new QueensFavor(g);

      else if (cardName.toLowerCase().equals("court called to camelot")) effect = new CourtCalledToCamelot(g);

      else if (cardName.toLowerCase().equals("king's call to arms")) effect = new KingsRecognition(g);

      else if (cardName.toLowerCase().equals("prosperity throughout the realm")) effect = new ProsperityThroughoutTheRealm(g);
    
    effect.eventBehavior();
  }
}
