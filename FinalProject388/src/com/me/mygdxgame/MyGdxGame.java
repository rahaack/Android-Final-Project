package com.me.mygdxgame;

import java.util.Random;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
	private int numberOfOrbsMatched;

	private Orb[][] orbs;
	private Orb currentOrb;

	Vector2 position;
	Rectangle testRect;

	Skin skin;
	Stage stage;
	Label score;
	Label match;

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

		numberOfOrbsMatched = 0;

		rows = 5;
		columns = 4;
		orbs = generateInitialField(rows, columns);
		checkForMatches(orbs);
		int numOfOrbs = getNumberOfMatches(orbs);
		while (numOfOrbs != 0) {
			checkForMatches(orbs);
			numOfOrbs = getNumberOfMatches(orbs);
			resetMatchedOrbs(orbs);
		}

		testRect = new Rectangle();
		testRect.x = 5;
		testRect.y = 1700;
		testRect.width = Gdx.graphics.getWidth() - 10;
		testRect.height = 13;

		// camera = new OrthographicCamera(1, h/w);
		InputMultiplexer inputMult = new InputMultiplexer();
		Gdx.input.setInputProcessor(this);

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage(1000, 1000, false);
		score = new Label("SCORE: ", skin);
		match = new Label("MATCHED: ", skin);
		stage.addActor(score);
		stage.addActor(match);
		score.setPosition(30, 900);
		match.setPosition(30, 830);
		score.setFontScale(3);
		match.setFontScale(3);
		// Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();

		batch.begin();

		// batch.setProjectionMatrix(camera.combined);

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				if (orbs[row][column].getTaken()) {
					orbs[row][column].getAnimatedSprite().draw(batch);
					if (orbs[row][column].getAnimatedSprite().isAnimationFinished()) {

						match.setText("Matched: " + getNumberOfMatches(orbs));
						numberOfOrbsMatched += getNumberOfMatches(orbs);
						resetMatchedOrbs(orbs);
						checkForMatches(orbs);
						score.setText("Score: " + numberOfOrbsMatched);
					}
				} else {
					orbs[row][column].draw(batch);
				}

			}
		}

		batch.end();

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

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (currentOrb != null)
			currentOrb.setPosition(currentOrb.getLocation().x, currentOrb.getLocation().y);
		currentOrb = null;

		checkForMatches(orbs);
		int orbsToDelete = getNumberOfMatches(orbs);
		if (orbsToDelete == 0) {
			// actionResolver.showAlertBox("You SUCK", "You matched " +
			// orbsToDelete + " orbs", "Try and suck AGAIN!!");
		} else if (orbsToDelete < 11) {
			// actionResolver.showAlertBox("GOOD JOB", "You matched " +
			// orbsToDelete + " orbs", "AGAIN!!");
		} else {
			// actionResolver.showAlertBox("YOU'RE AMAZING", "You matched " +
			// orbsToDelete + " orbs", "AGAIN!!");
		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		if (currentOrb != null) {
			// if it is not over
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
					if (currentOrb != orbs[row][column]
							&& currentOrb.getBoundingRectangle().overlaps(orbs[row][column].getBoundingRectangle())) {
						System.out.println("touching: " + orbs[row][column].getOrbColor());

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

	private int getNumberOfMatches(Orb[][] orb) {
		int matches = 0;
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				if (orb[row][column].getTaken() == true) {
					matches++;
				}
			}
		}
		return matches;
	}

	private void resetMatchedOrbs(Orb[][] orb) {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				if (orb[row][column].getTaken() == true) {
					generateRandomOrb(orb, orb[row][column].getLocation().x, orb[row][column].getLocation().y, row, column);
					orb[row][column].resetToDelete();
				}
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
