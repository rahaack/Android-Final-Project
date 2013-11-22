package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	Vector2 position;

	InputProcessor IP;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();

		texture = new Texture(Gdx.files.internal("data/ball.png"));
		position = new Vector2(0, 0);

		IP = new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				// TODO Auto-generated method stub
				return false;
			}

			float x;
			float y;

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {

				if (x < (position.x + 100) && x > (position.x - 100) && y < (500 - position.y + 75)&& y > (500 - position.y - 75)) {
					x = screenX; 
					y = screenY;
					position.x = screenX - 50;
					position.y = 500 - screenY - 50;
				}

				return true;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				x = screenX;
				y = screenY;
				System.out.println(y);
				System.out.println(position.y);
				return true;
			}

			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		Gdx.input.setInputProcessor(IP);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(texture, position.x, position.y);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
