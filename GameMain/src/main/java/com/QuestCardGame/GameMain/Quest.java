package com.QuestCardGame.GameMain;

public class Quest {

	private Stage[] stages;

	Quest(QuestCard qc) {
		stages = new Stage[qc.getStages()];
	}
}
