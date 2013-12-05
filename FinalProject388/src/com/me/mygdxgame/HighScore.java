package com.me.mygdxgame;

public class HighScore {

	private String initials;
	private int score;
	
	public HighScore(String initials, int score){
		this.initials = initials;
		this.score = score;
	}
	
	public int getScore(){
		return score;
	}
	
	public String getHighScoreString(){
		return initials + "  " + score;
	}
}
