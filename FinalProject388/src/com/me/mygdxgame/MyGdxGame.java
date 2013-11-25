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
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture red;
	private Texture blue;
	private Texture green;
	private Texture yellow;

	public float screenWidth;
	public float screenHeight;

	private float touchX;
	private float touchY;

	private Sprite currentSprite;
	private Sprite redSprite;
	private Sprite blueSprite;
	Vector2 position;

	InputProcessor IP;
	GestureListener GL;

	@Override
	public void create() {
		screenWidth = Gdx.graphics.getWidth(); // 1200
		screenHeight = Gdx.graphics.getHeight(); // 1824

		// camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();

		red = new Texture(Gdx.files.internal("data/reddot.png"));
		blue = new Texture(Gdx.files.internal("data/bluedot.png"));
		green = new Texture(Gdx.files.internal("data/greendot.png"));
		yellow = new Texture(Gdx.files.internal("data/yellowdot.png"));

		redSprite = new Sprite(red);
		redSprite.setPosition(50, 50);
		blueSprite = new Sprite(blue);
		blueSprite.setPosition(screenWidth / 2, screenHeight / 2);

		position = new Vector2(0, 0);

		World world = new World(new Vector2(0, -10), true);

		IP = new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {

				currentSprite = getSprite(touchX, touchY);

				if (currentSprite != null) {
					touchX = screenX;
					touchY = screenY;
					currentSprite.setPosition(screenX - currentSprite.getWidth() / 2, screenHeight - screenY
							- currentSprite.getWidth() / 2);
					currentSprite = null;
				}

				return true;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				touchX = screenX;
				touchY = screenY;

				System.out.println("TouchX:" + touchX);
				System.out.println("TouchY:" + touchY);
				System.out.println("getX:" + redSprite.getX());
				System.out.println("getY:" + (screenHeight - redSprite.getY()));
				return true;
			}

			private Sprite getSprite(float touchX, float touchY) {
				if (checkSprite(redSprite, touchX, touchY)) {
					return redSprite;
				} else if (checkSprite(blueSprite, touchX, touchY)) {
					return blueSprite;
				}
				return null;
			}

			private boolean checkSprite(Sprite sprite, float touchX, float touchY) {
				return touchX > (sprite.getX()) && touchX < (sprite.getX() + sprite.getWidth())
						&& touchY > (screenHeight - sprite.getY() - sprite.getWidth())
						&& touchY < (screenHeight - sprite.getY());
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
		red.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// batch.setProjectionMatrix(camera.combined);
		batch.begin();
		redSprite.draw(batch);
		blueSprite.draw(batch);
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
