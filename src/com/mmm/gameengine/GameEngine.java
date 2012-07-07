package com.mmm.gameengine;

import java.util.Map;
import java.util.TreeMap;

/**
 * The GameEngine class is a simple implementation of a state manager. It facilitates
 * easy global access (with acceptable levels of security) to GameState's which are
 * loaded and available. Easy methods of initializing all states, cleaning up all 
 * states, as well as keeping track of current game state. With one call one can
 * easily switch to other states, which also takes care of calling the required
 * method leaving() from the current state, as well as switchedTo() on the NEW
 * current state.
 * <BR>
 * To use:
 * 
 * - Create a game engine with
 * <code>GameEngine ge = GameEngine.getInstance();</code>
 * 
 * Now you have a handle to the game engine. Go ahead and add your states.
 * 
 * - Call initialization on all states by
 * <code>ge.initAllStates()</code>
 * 
 * By default, the first state added was placed in control. To switch this,
 * at any time (and from any class file), use one of the two methods.
 * Either
 * 
 * <code>ge.switchToState(ID_HERE)</code>
 * 
 * or
 * 
 * <code>GameEngine.getInstance().switchToState(ID_HERE)</code>
 * 
 * Now everything is ready. Simply create a GameView (for which to see your
 * game drawn on) by
 * 
 * <code>GameView gv = new GameView(this)</code>
 * 
 * then set your content view in your activity...
 * 
 * <code>setContentView(gv);</code>
 * 
 * All done!
 * 
 * Last Edited by: Michael Morris (7.6.2012)
 * @author Michael Morris
 * @version 1.0.0
 *
 */
public final class GameEngine {

	/**
	 * The ONE GameEngine instance used for global
	 * access of game states. 
	 */
	private static GameEngine _instance = null;
	
	/**
	 * Retrieve the previously instantiated game instance. If
	 * none has been created go ahead and create a new instance.
	 * @return The current, or new, GameEngine instance.
	 */
	public static final GameEngine getInstance() {
		if (_instance == null)
			_instance = new GameEngine();
		
		return _instance;
	}
	
	/**
	 * The list of game states currently accessible by this GameEngine.
	 */
	private Map<Integer, GameState> game_states;
	
	/**
	 * The current game state.
	 */
	private GameState current_state = null;
	
	/**
	 * A private constructor to prevent instantiation without using getInstance().
	 */
	private GameEngine() { 
		game_states = new TreeMap<Integer, GameState>();
	}
	
	/**
	 * Retrieve the game state with the given ID, or null if not found.
	 * @param ID The unique integer ID of the state.
	 * @return The specified game state or null.
	 */
	public final GameState retrieveState(final int ID) {
		GameState rtn_val = null;
		if (game_states.containsKey(ID))
			rtn_val = game_states.get(ID);
		return rtn_val;
	}
	
	/**
	 * Add the given state to the list of accessible game states. Return
	 * the success value true if game was successfully added, false otherwise.
	 * @param state The GameState object to add.
	 * @return True if added successfully, false otherwise.
	 */
	public final boolean addState(final GameState state) {
		boolean ok = false;
		if (!game_states.containsKey(state.getStateId())) {
			game_states.put(state.getStateId(), state);
			ok = true;
			
			// If this is the first state being added, then automatically switch to it.
			if (game_states.size() == 1)
				current_state = state;
			
		}
		
		return ok;
	}
	
	/**
	 * Switch to the state specified by the ID. The state must have been
	 * added to the list of available states.
	 * @param ID The unique ID of the state to switch to.
	 * @return True if state could be switched to, false otherwise.
	 */
	public final boolean switchToState(final int ID) {
		
		boolean ok = false;
		if (game_states.containsKey(ID)) {
			
			// Leave the current state. If one exists.
			if (current_state != null)
				current_state.leaving();
			
			// Switch the actual state.
			current_state = game_states.get(ID);
			
			// Call the switchedTo() method on new current state.
			current_state.switchedTo();
			
			ok = true;
			
		}
		return ok;
	}
	
	/**
	 * Get a reference to the current game state.
	 * @return The current game state.
	 */
	public final GameState getCurrentState() {
		return current_state;
	}
	
	/**
	 * Call the init() method on all currently available states.
	 * @return True if all states successfully initialized. False otherwise.
	 */
	public final boolean initAllStates() {
		
		boolean ok = false;
		
		for (GameState state : game_states.values()) {
			ok = state.init() && ok;
		}
		
		return ok;
	}
	
	
	/**
	 * Call the cleanup() method on all currently available states.
	 * @return True if all states successfully cleaned up. False otherwise.
	 */
	public final boolean cleanupAllStates() {
		boolean ok = false;
		
		for (GameState state : game_states.values()) {
			ok = state.cleanup() && ok;
		}
		
		return ok;
	}
	
}
