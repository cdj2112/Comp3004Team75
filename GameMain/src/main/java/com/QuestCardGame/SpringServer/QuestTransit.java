package com.QuestCardGame.SpringServer;

import java.io.Serializable;

import com.QuestCardGame.GameMain.Quest;
import com.QuestCardGame.GameMain.Stage;

public class QuestTransit implements Serializable {
	private StageTransit[] stages;
	private int currentStage;
	private int sponsor;
	
	public QuestTransit(Quest q) {
		Stage[] s = q.getStages();
		stages = new StageTransit[s.length];
		for(int i=0; i<s.length; i++) {
			stages[i] = new StageTransit(s[i]);
		}
		currentStage = q.getCurrentStageIndex();
	}
	
	public StageTransit[] getStages() {
		return stages;
	}
	
	public int getCurrentStage() {
		return currentStage;
	}
	
	public void setSponsor(int s) {
		sponsor = s;
	}
	
	public int getSponsor() {
		return sponsor;
	}
}
