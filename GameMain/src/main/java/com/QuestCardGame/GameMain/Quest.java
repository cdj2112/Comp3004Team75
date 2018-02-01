package com.QuestCardGame.GameMain;

public class Quest {

	private Stage[] stages;

	Quest(QuestCard qc) {
		stages = new Stage[qc.getStages()];
	}
	
	public boolean validateQuest() {
		//int previousBP = -1;
		for(Stage s: stages) {
			/*if(s.getBattlePoints()<=previousBP) {
				return false;
			}
			previousBP = s.getBattlePoints();*/
			if(s.getNumCards()==0) {
				return false;
			}
		}
		return true;
		
	}
}
