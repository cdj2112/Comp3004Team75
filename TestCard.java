package com.QuestCardGame.GameMain;

public class TestCard extends Card {

	private String name;

	TestCard(int x) {
		super();
		if(x == 0) {
		name = "Valor";
		
		}else if (x == 1) {
			name = "Temptation";
		 
		}else if (x == 2) {
			name = "MorganLeFey";
		    
		}else if (x == 3) {
			name = "QuestionBeat";
		  
		}
		}
	
	public String getName(){
		return name;
	}
}
