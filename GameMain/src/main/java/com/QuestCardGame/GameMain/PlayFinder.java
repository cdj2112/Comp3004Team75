package com.QuestCardGame.GameMain;

public class PlayFinder {
	
	private String target;
	private Game game;
	
	public PlayFinder(Game g, String t) {
		game = g;
		target = t;
	}
	
	public boolean find() {
		for(int i = 0; i<game.getNumPlayers(); i++) {
			Player p = game.getPlayer(i);
			for(AdventureCard c : p.getPlay()) {
				if(c.getName().equals(target)) {
					return true;
				}
			}
		}
		return false;
	}

}
