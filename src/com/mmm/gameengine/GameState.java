package com.mmm.gameengine;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * 
 * The GameState interface describes a basic contract which subclasses
 * must provide to be used by the GameEngine class.
 * 
 * Last Edited by: Michael Morris (7.6.2012)
 * @author Michael Morris
 * @version 1.0.0 
 *
 */
public interface GameState {

	/**
	 * Retrieve this states unique integer state ID. Used
	 * for distinguishing this state from another without using
	 * references.
	 * @return The unique integer state ID stored with this state
	 * implementation.
	 */
	public int getStateId();
	
	/**
	 * Called upon the GameEngine's init phase.
	 * @return True if initiation was successful.
	 */
	public boolean init();
	
	/**
	 * Called when the GameEngine needs to close.
	 * @return True if cleanup was successful.
	 */
	public boolean cleanup();
	
	/**
	 * Called when the GameEngine switches control
	 * to this game state. Should be used to re-initialize
	 * graphics, re-setup state variables, etc.
	 */
	public void switchedTo();
	
	/**
	 * Called when the GameEngine switches control
	 * from this game state to another. This should be used
	 * to stop counters, unload unneccesary resources, etc.
	 */
	public void leaving();
	
	/**
	 * Called when the GameEngine signals an update tick.
	 * @param dt The milliseconds since last update.
	 */
	public void update(final long dt);
	
	/**
	 * Called when the GameEngine signals for this game
	 * state to draw.
	 */
	public void draw(Canvas canvas);
	
	public void handleTouchEvent(MotionEvent event);
	
}
