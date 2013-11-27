package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	ActionResolver actionResolver;

	public float screenWidth;
	public float screenHeight;

	private float touchX;
	private float touchY;
	int rows;
	int columns;

	private Orb[][] orbs;
	private Orb currentOrb;

	Vector2 position;
	Rectangle testRect;

	public MyGdxGame(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
		actionResolver.showToast("Start BallBuster", 5000);
	}

	public MyGdxGame() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		screenWidth = Gdx.graphics.getWidth(); // 1200
		screenHeight = Gdx.graphics.getHeight(); // 1824

		rows = 5;
		columns = 4;
		orbs = generateInitialField(rows, columns);

		testRect = new Rectangle();
		testRect.x = 5;
		testRect.y = 1700;
		testRect.width = Gdx.graphics.getWidth() - 10;
		testRect.height = 13;

		// camera = new OrthographicCamera(1, h/w);

		

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				orbs[row][column].draw(batch);
			}
		}

		batch.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0, 100, 100, 1));
		shapeRenderer.rect(100, 1600, 1000, 30);
		shapeRenderer.end();
	}

	private void drawOrbRect(Orb orb) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(1, 1, 1, 1));
		shapeRenderer.rect(orb.getBoundingRectangle().x, orb.getBoundingRectangle().y,
				orb.getBoundingRectangle().getWidth(), orb.getBoundingRectangle().getHeight());
		shapeRenderer.end();

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

	private void checkForMatches(Orb[][] orb) {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns - 2; column++) {
				OrbColor colorToMatch = orb[row][column].getOrbColor();
				checkRight(orb, row, column, colorToMatch);
			}
		}
		for (int row = 0; row < rows - 2; row++) {
			for (int column = 0; column < columns; column++) {
				OrbColor colorToMatch = orb[row][column].getOrbColor();
				checkDown(orb, row, column, colorToMatch);
			}
		}

	}

	private int deleteMatchesAndReset(Orb[][] orb) {
		int matches = 0;
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				if (orb[row][column].getTaken() == true) {
					matches++;
					generateRandomOrb(orb, orb[row][column].getLocation().x, orb[row][column].getLocation().y, row, column);
					// orb[row][column].resetToDelete();
				}
			}
		}
		return matches;
	}

	private void checkDown(Orb[][] orb, int row, int column, OrbColor colorToMatch) {
		// match two colors to the top
		if (orb[row + 1][column].getOrbColor() == colorToMatch) {
			// match three colors to the top
			if (orb[row + 2][column].getOrbColor() == colorToMatch) {
				orb[row][column].setToDelete();
				orb[row + 1][column].setToDelete();
				orb[row + 2][column].setToDelete();
			}
		}
	}

	private void checkRight(Orb[][] orb, int row, int column, OrbColor colorToMatch) {
		// match two to the right
		if (orb[row][column + 1].getOrbColor() == colorToMatch) {
			// match three to the right
			if (orb[row][column + 2].getOrbColor() == colorToMatch) {
				orb[row][column].setToDelete();
				orb[row][column + 1].setToDelete();
				orb[row][column + 2].setToDelete();
				/*
				 * // match four to the right if (orb[row][column +
				 * 3].getOrbColor() == colorToMatch) { orb[row][column +
				 * 3].setToDelete(); }
				 */
			}
		}

	}

	private Orb[][] generateInitialField(int numberOfRows, int numberOfColumns) {

		Orb[][] orb = new Orb[numberOfRows][numberOfColumns];
		float xPosition = 0;
		float yPosition = 0;
		int xLocation = (int) (screenWidth / numberOfColumns);

		for (int row = 0; row < numberOfRows; row++) {
			xPosition = 0;
			for (int column = 0; column < numberOfColumns; column++) {
				generateRandomOrb(orb, xPosition, yPosition, row, column);
				xPosition += xLocation;
			}
			yPosition += 295;
		}
		return orb;
	}

	private void generateRandomOrb(Orb[][] orb, float xPosition, float yPosition, int row, int column) {
		int randomNum = randInt(0, 3);
		if (randomNum == 0) {
			orb[row][column] = new Orb(new Texture(Gdx.files.internal("data/reddot.png")), OrbColor.RED, xPosition,
					yPosition);
		} else if (randomNum == 1) {
			orb[row][column] = new Orb(new Texture(Gdx.files.internal("data/bluedot.png")), OrbColor.BLUE, xPosition,
					yPosition);
		} else if (randomNum == 2) {
			orb[row][column] = new Orb(new Texture(Gdx.files.internal("data/greendot.png")), OrbColor.GREEN, xPosition,
					yPosition);
		} else if (randomNum == 3) {
			orb[row][column] = new Orb(new Texture(Gdx.files.internal("data/yellowdot.png")), OrbColor.YELLOW, xPosition,
					yPosition);
		}
		orb[row][column].setOrbRowColumn(row, column);
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		if (currentOrb != null) {
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
					if (currentOrb != orbs[row][column]
							&& currentOrb.getBoundingRectangle().overlaps(orbs[row][column].getBoundingRectangle())) {
						// System.out.println("touching: " +
						// orbs[row][column].getOrbColor());

						Vector2 temp = orbs[row][column].getLocation();
						orbs[row][column].setPosition(currentOrb.getLocation().x, currentOrb.getLocation().y);
						orbs[row][column].setLocation(currentOrb.getLocation().x, currentOrb.getLocation().y);
						orbs[row][column].setOrbRowColumn((int) currentOrb.getOrbRowColumn().x,
								(int) currentOrb.getOrbRowColumn().y);

						// swap
						orbs[(int) currentOrb.getOrbRowColumn().x][(int) currentOrb.getOrbRowColumn().y] = orbs[row][column];
						orbs[row][column] = currentOrb;

						currentOrb.setOrbRowColumn(row, column);
						currentOrb.setLocation(temp.x, temp.y);
					}
				}
			}

			touchX = screenX;
			touchY = screenY;
			currentOrb.setPosition(screenX - currentOrb.getWidth() / 2, screenHeight - screenY - currentOrb.getWidth() / 2);

		}

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchX = screenX;
		touchY = screenY;
		if (currentOrb == null)
			currentOrb = getOrb(touchX, touchY);
		// System.out.println("TouchX:" + touchX);
		// System.out.println("TouchY:" + touchY);
		return true;
	}

	private Orb getOrb(float touchX, float touchY) {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				if (isThisOrb(orbs[row][column], touchX, touchY))
					return orbs[row][column];
			}
		}
		return null;
	}

	private boolean isThisOrb(Orb sprite, float touchX, float touchY) {
		return touchX > (sprite.getX()) && touchX < (sprite.getX() + sprite.getWidth())
				&& touchY > (screenHeight - sprite.getY() - sprite.getWidth()) && touchY < (screenHeight - sprite.getY());
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (currentOrb != null)
			currentOrb.setPosition(currentOrb.getLocation().x, currentOrb.getLocation().y);
		currentOrb = null;
		checkForMatches(orbs);
		int orbsToDelete = deleteMatchesAndReset(orbs);
		if (orbsToDelete == 0) {
			actionResolver.showAlertBox("You SUCK", "You matched " + orbsToDelete + " orbs", "Try and suck AGAIN!!");
		} else {
			actionResolver.showAlertBox("GOOD JOB", "You matched " + orbsToDelete + " orbs", "AGAIN!!");
		}
		while (orbsToDelete != 0) {
			System.out.println("Deleted:" + orbsToDelete);
			checkForMatches(orbs);
			orbsToDelete = deleteMatchesAndReset(orbs);
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
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
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

}
