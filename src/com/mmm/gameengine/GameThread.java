package com.mmm.gameengine;

import android.graphics.Canvas;
import android.util.Log;

/**
 * The GameThread class is intended to be used with GameEngine and GameView to
 * provide the general game loop. The game loop's draw phase calls the onDraw
 * of the GameView (thereby calling whatever the current game states draw method
 * is required.) The update phase is handled directly with the GameEngine, which
 * calls the current states update method.
 * 
 * Last Edited by: Michael Morris (7.6.2012)
 * @author Michael Morris
 * @version 1.0.0
 *
 */
public class GameThread implements Runnable {

	/** Desired frame rate. **/
	static final long FPS = 28;
	
	private GameView view;
	private boolean running = false;
	
	private GameEngine ge = null;
	
	public GameThread(GameView view) {
		this.view = view;
		ge = GameEngine.getInstance();
	}
	
	public void setRunning(boolean run) {
		running = run;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void run() {
		
		Log.i("StickmanPaintball", "GameThread.run() method called!");
		
		long ticksPS = 1000 / FPS;
		long startTime = System.currentTimeMillis();
		long sleepTime;
		
		while (running) {
			Canvas c = null;
			
			// Do work if there is a current state to work with.
			GameState current_state = ge.getCurrentState();
			if (current_state != null) {
			
				// Draw first
				try {
					c = view.getHolder().lockCanvas();
					synchronized (view.getHolder()) {
						view.onDraw(c);
					}
				} finally {
					if (c != null) {
						view.getHolder().unlockCanvasAndPost(c);
					}
				}
			}
			
			long elapsed = System.currentTimeMillis() - startTime;
			startTime = System.currentTimeMillis();
			
			current_state = ge.getCurrentState();
			
			if (current_state != null) {
				
				// Update second
				ge.getCurrentState().update(elapsed);
				
			}
			
			// Calculate the time to sleep, hopefully meeting our desired frames per second.
			sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					Thread.sleep(sleepTime);
				else
					Thread.sleep(10);
			} catch (Exception e) { }
		}
		
		Log.i("StickmanPaintball", "GameThread.run() method ending!");
		
	}


	
}
