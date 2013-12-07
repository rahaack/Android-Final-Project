package com.me.mygdxgame;

import com.badlogic.gdx.utils.TimeUtils;

public class Timer {
	private long start;
	private long secsToWait;
	private boolean running;

	public Timer(long secsToWait) {
		this.secsToWait = secsToWait;
		running = false;
	}

	public void start() {
		start = TimeUtils.millis() / 1000;
		running = true;
	}

	public boolean isRunning(){
		return running;
	}
	public long getTime() {
		return TimeUtils.millis() / 1000 - start;
	}

	public boolean hasCompleted() {
		running = false;
		return TimeUtils.millis() / 1000 - start >= secsToWait;

	}
}
