package com.QuestCardGame.SpringServer;

import java.io.Serializable;

import com.QuestCardGame.GameMain.Quest;
import com.QuestCardGame.GameMain.Stage;

public class QuestTransit implements Serializable {
	private StageTransit[] stages;
	
	public QuestTransit(Quest q) {
		Stage[] s = q.getStages();
		stages = new StageTransit[s.length];
		for(int i=0; i<s.length; i++) {
			stages[i] = new StageTransit(s[i]);
		}
	}
	
	public StageTransit[] getStages() {
		return stages;
	}
}
