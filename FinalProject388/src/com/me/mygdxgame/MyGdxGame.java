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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MyGdxGame implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private ActionResolver actionResolver;

	public float screenWidth;
	public float screenHeight;

	private float touchX;
	private float touchY;
	private int rows;
	private int columns;
	private int numberOfOrbsMatched;
	private int numberOfTurns;
	private int maxTurns;

	private Orb[][] orbs;
	private Orb currentOrb;

	Vector2 position;

	private Skin skin;
	private Stage stage;
	private Label score;
	private Label match;
	private Label turns;
	private TextButton newGame;

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

		maxTurns = 5;
		numberOfTurns = 0;
		numberOfOrbsMatched = 0;
		rows = 5;
		columns = 4;

		// Generate initial balls
		orbs = generateInitialField(rows, columns);
		checkForMatches(orbs);
		int numOfOrbs = getNumberOfMatches(orbs);
		while (numOfOrbs != 0) {
			checkForMatches(orbs);
			numOfOrbs = getNumberOfMatches(orbs);
			resetMatchedOrbs(orbs);
		}

		// camera = new OrthographicCamera(1, h/w);

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage(1000, 1000, false);
		score = new Label("Score: 0", skin);
		match = new Label("Matched: 0", skin);
		turns = new Label("Turns: 0/" + maxTurns, skin);
		newGame = new TextButton("New Game!", skin);
		stage.addActor(score);
		score.setPosition(35, 920);
		score.setFontScale(3);

		stage.addActor(match);
		match.setPosition(35, 850);
		match.setFontScale(3);

		stage.addActor(turns);
		turns.setPosition(350, 920);
		turns.setFontScale(3);

		stage.addActor(newGame);
		newGame.setBounds(750, 900, 200, 80);
		newGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				score.setText("Score: 0");
				match.setText("Matched: 0");
				turns.setText("Turns: 0/" + maxTurns);
				numberOfTurns = 0;
				numberOfOrbsMatched = 0;
				actionResolver.showToast("Start BallBuster", 5000);
			}
		});

		InputMultiplexer inputMult = new InputMultiplexer();
		inputMult.addProcessor(stage);
		inputMult.addProcessor(this);
		Gdx.input.setInputProcessor(inputMult);
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
					match.setText("Matched: " + getNumberOfMatches(orbs));
					if (orbs[row][column].getAnimatedSprite().isAnimationFinished()) {
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
		
		if (numberOfTurns == maxTurns && getNumberOfMatches(orbs) == 0) {
			actionResolver.showAlertBox("Game Over", score.getText() + " orbs", "Try Again");
			score.setText("Score: 0");
			match.setText("Matched: 0");
			turns.setText("Turns: 0/" + maxTurns);
			numberOfTurns = 0;
			numberOfOrbsMatched = 0;
		}

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

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (currentOrb != null)
			currentOrb.setPosition(currentOrb.getLocation().x, currentOrb.getLocation().y);
		currentOrb = null;

		numberOfTurns++;
		turns.setText("Turns: " + numberOfTurns +"/" + maxTurns);

		checkForMatches(orbs);
		int orbsToDelete = getNumberOfMatches(orbs);
		match.setText("Matched: " + orbsToDelete);

		return true;
	}

	Orb swapped = null;

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		boolean orbTouched = false;

		if (currentOrb != null) {
			if (swapped != null && currentOrb.getBoundingRectangle().overlaps(swapped.getBoundingRectangle())) {
				System.out.println("Swapped");
			} else {
				swapped = null;
				// if it is not over
				for (int row = 0; row < rows; row++) {
					for (int column = 0; column < columns; column++) {
						if (currentOrb != orbs[row][column]
								&& currentOrb.getBoundingRectangle().overlaps(orbs[row][column].getBoundingRectangle())
								&& orbTouched == false) {
							System.out.println("touching: " + orbs[row][column].getOrbColor());

							Vector2 temp = orbs[row][column].getLocation();

							orbs[row][column].setPosition(currentOrb.getLocation().x, currentOrb.getLocation().y);
							orbs[row][column].setLocation(currentOrb.getLocation().x, currentOrb.getLocation().y);
							orbs[row][column].setOrbRowColumn((int) currentOrb.getOrbRowColumn().x,
									(int) currentOrb.getOrbRowColumn().y);
							swapped = orbs[row][column];

							// swap
							orbs[(int) currentOrb.getOrbRowColumn().x][(int) currentOrb.getOrbRowColumn().y] = orbs[row][column];
							orbs[row][column] = currentOrb;

							currentOrb.setOrbRowColumn(row, column);
							currentOrb.setLocation(temp.x, temp.y);
							orbTouched = true;
						}
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
		float xPosition = 10;
		float yPosition = 10;
		int xLocation = (int) (screenWidth / numberOfColumns);

		for (int row = 0; row < numberOfRows; row++) {
			xPosition = 10;
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
