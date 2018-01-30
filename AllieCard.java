package com.QuestCardGame.GameMain;

public class AllieCard extends Card {

	private String name;
	private int Strength;
    
	AllieCard(int x) {
		super();
		if(x == 0) {
		name = "Merlin";
		Strength = 0;
		}else if (x == 1) {
			name = "Galahad";		
		    Strength = 10;
		}else if (x == 2) {
			name = "Lancelot";
		    Strength = 0;
		}else if (x == 3) {
			name = "Arthur";
			Strength = 20;
		}else if (x == 4) {
			name = "Tristan";
		    Strength = 25;
		}else if (x == 5) {
			name = "Pellinore";
		    Strength = 30;
		}else if (x == 6) {
			name = "Gawain";
		    Strength = 30;
		}else if (x == 7) {
			name = "Perecial";
		    Strength = 30;
		}else if (x == 8) {
			name = "Guinevere";
		    Strength = 30;
		}else if (x == 9) {
			name = "Iseult";
		    Strength = 30;
	}
    }
	
    public int getStrength(){
	    return Strength;
}
    public String getName(){
		return name;
	}
}