package com.mmm.gameengine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * The GameView class provides a SurfaceView subclass which should be
 * used with the GameEngine and GameThread classes. The GameView class
 * automatically calls GameEngine, to retrieve a reference, and
 * activates a separate game looping thread to automate the game
 * engine process. All state adding (to GameEngine) should be done
 * prior to creating the GameView or unexpected results may occur.
 * See GameEngine overview for more details.
 * 
 * Last Edited by: Michael Morris (7.6.2012)
 * @author Michael Morris
 * @version 1.0.0
 * 
 *
 */
public class GameView extends SurfaceView {

	private SurfaceHolder holder;
	
	private GameThread gameThread;
	
	public GameView(Context context) {
		super(context);
		
		Log.i("StickmanPaintball", "GameView constructor called!");
		
		// Setup game engine and thread.
		gameThread = new GameThread(this);
		
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {}

			// Upon surface creation, make sure we use the same runnable game thread, but create a new thread.
			public void surfaceCreated(SurfaceHolder holder) {
				Thread t = new Thread(gameThread);
				gameThread.setRunning(true);
				t.start();
			}

			// When the surface view has been destroyed, go ahead and kill the game thread.
			public void surfaceDestroyed(SurfaceHolder holder) {
				gameThread.setRunning(false);
			}
			
		});
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		GameState current = GameEngine.getInstance().getCurrentState();
		if (current != null)
			current.handleTouchEvent(event);
		
		return true;
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		GameState current = GameEngine.getInstance().getCurrentState();
		if (current != null) {
			current.draw(canvas);
		}
	}
	
	
}
