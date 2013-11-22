package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "BALL Buster";
		cfg.useGL20 = true;
		cfg.width = 500;
		cfg.height = 500;
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
