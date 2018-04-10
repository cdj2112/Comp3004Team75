package com.QuestCardGame.SpringServer;

import java.io.Serializable;
import java.util.ArrayList;

import com.QuestCardGame.GameMain.AdventureCard;
import com.QuestCardGame.GameMain.Quest;
import com.QuestCardGame.GameMain.Stage;

public class QuestTransit implements Serializable {
	private StageTransit[] stages;
	private int currentStage;
	private int sponsor;
	private int currentBid;
	private CardTransit[] stash;

	public QuestTransit(Quest q) {
		Stage[] s = q.getStages();
		stages = new StageTransit[s.length];
		for (int i = 0; i < s.length; i++) {
			stages[i] = new StageTransit(s[i], q.getTarget());
		}
		currentStage = q.getCurrentStageIndex();
		currentBid = q.getBids();
		ArrayList<AdventureCard> st = q.getStash();
		stash = new CardTransit[st.size()];
		for (int i = 0; i < st.size(); i++) {
			stash[i] = new CardTransit(st.get(i));
		}
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

	public int getCurrentBid() {
		return currentBid;
	}
	
	public CardTransit[] getStash() {
		return stash;
	}
}
