package com.me.mygdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MyGdxGame extends Game {
	private SplashScreen splash;
	private GameScreen game;
	private ActionResolver actionResolver;

	public MyGdxGame(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
	}

	public MyGdxGame() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() { 
		splash = new SplashScreen(this);
		setScreen(splash);
	}

	public Screen getGame(){
		game = new GameScreen(this, actionResolver);
		return game;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
