package com.me.mygdxgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Orb extends Sprite {

	private boolean toDelete;
	private OrbColor color;
	private Rectangle rect;
	private Vector2 orbRowColumn;
	private Vector2 orbLocation;

	public Orb(Texture texture, OrbColor oc, float xPosition, float yPosition) {
		super(texture);
		rect = new Rectangle(xPosition, yPosition, this.getWidth(), this.getHeight());
		setPosition(xPosition, yPosition);
		orbLocation = new Vector2(xPosition, yPosition);
		color = oc;
		toDelete = false;
	}

	@Override
	public void setPosition(float xPosition, float yPosition) {
		super.setPosition(xPosition, yPosition);
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
