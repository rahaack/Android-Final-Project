package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Orb extends Sprite {

	private boolean toDelete;
	private OrbColor color;
	private Rectangle rect;
	private Vector2 orbRowColumn;
	private Vector2 orbLocation;
	private AnimatedSprite as;

	private static final int FRAME_COLS = 3;
	private static final int FRAME_ROWS = 3;
	Animation walkAnimation;

	public Orb(Texture texture, OrbColor oc, float xPosition, float yPosition) {
		super(texture);
		animate(xPosition,  yPosition);
		rect = new Rectangle(xPosition, yPosition, this.getWidth(), this.getHeight());
		setPosition(xPosition, yPosition);
		orbLocation = new Vector2(xPosition, yPosition);
		color = oc;
		toDelete = false;
	}
	
	private void animate(float xPosition, float yPosition) {
		Texture walkSheet = new Texture(Gdx.files.internal("data/dotani.png"));
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, 768 / FRAME_COLS, 768 / FRAME_ROWS);
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(0.08f, walkFrames);
		walkAnimation.setPlayMode(Animation.NORMAL);
		as = new AnimatedSprite(walkAnimation);
		as.setPlaying(false);
		as.setX(xPosition);
		as.setY(yPosition);
	}
	
	public AnimatedSprite getAnimatedSprite(){
		return as;
	}


	@Override
	public void setPosition(float xPosition, float yPosition) {
		super.setPosition(xPosition, yPosition);
		as.setX(xPosition);
		as.setY(yPosition);
		rect.setPosition(new Vector2(xPosition, yPosition));
	}
	
	public void resetToDelete(){
		toDelete = false;
	}
	
	public void setToDelete(){
		toDelete = true;
	}

	public boolean getTaken(){
		return toDelete;
	}
	
	public OrbColor getOrbColor() {
		return color;
	}

	public Rectangle getBounds() {
		return rect;
	}

	public void setOrbRowColumn(int row, int column) {
		orbRowColumn = new Vector2(row, column);
	}
	
	public Vector2 getOrbRowColumn() {
		return orbRowColumn;
	}

	public void setLocation(float xPosition, float yPosition) {
		orbLocation = new Vector2(xPosition, yPosition);
	}
	
	public Vector2 getLocation() {
		return orbLocation;
	}

}
