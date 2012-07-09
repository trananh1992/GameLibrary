package com.mmm.animation;

import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * 
 * The MMMAnimation class specifies an object which encapsulates all loading
 * of sprites, tracking positions, and drawing functionality of a Bitmap
 * resource.
 * 
 * Last Edited by: Michael Morris (7.6.2012)
 * @author Michael Morris
 * @version 1.3.3
 *
 */
public class MMMAnimation {

	/** A map of Strings over MMMAnimationPhase objects which represent all the different phases of animation for this MMMAnimation. **/
	private Map<String, MMMAnimationPhase> phases;

	/** The current phase being viewed by this MMMAnimation. **/
	private MMMAnimationPhase current_phase = null;
	
	/** The x location of the MMMAnimation. **/
	protected float x;
	
	/** The y location of the MMMAnimation. **/
	protected float y;
	
	/** The z location, here used as a "layer", of the MMMAnimation. **/
	protected float zLayer;
	
	/** The floating point scale used when rendering the animation. **/
	protected float scale;
	
	/** The sprite sheet. **/
	protected Bitmap bmp = null;
	
	/** The color to tint the drawn animation. **/
	protected int tint_color;
	
	/** The flag which controls whether or not to use/not use the tint color. **/
	protected boolean use_tint;
	
	/** The degrees of rotation to use while drawing. **/
	protected int rotation_angle_degrees;
	
	/** The number of sprites in the x direction on the sprite sheet. **/
	private int num_sprites_x;
	
	/** The number of sprites in the y direction on the sprite sheet. **/
	private int num_sprites_y;
	
	/**
	 * Create a new MMMAnimation at the default location of (0, 0, 0).
	 */
	public MMMAnimation() {
		this(0.0f, 0.0f, 0);
	}
	
	/**
	 * Create a new MMMAnimation at the default layer (z value) of 0.0, and with
	 * x and y locations specified, respectively, by <code>the_x</code> and <code>
	 * the_y</code>.
	 * @param the_x The x location of the new MMMAnimation.
	 * @param the_y The y location of the new MMMAnimation.
	 */
	public MMMAnimation(final float the_x, final float the_y) {
		this(the_x, the_y, 0);
	}
	
	/**
	 * Create a new MMMAnimation with the x, y, and layer locations specified,
	 * respectively, by <code>the_x</code>, <code>the_y</code>, and <code>
	 * the_layer</code>.
	 * @param the_x
	 * @param the_y
	 * @param the_layer
	 */
	public MMMAnimation(final float the_x, final float the_y, final float the_layer) {
		x = the_x;
		y = the_y;
		zLayer = the_layer;
		scale = 1.0f;
		
		tint_color = 0;
		use_tint = false;
		
		phases = new TreeMap<String, MMMAnimationPhase>();
	}
	
	/**
	 * Create, return, and register a new MMMAnimationPhase for this MMMAnimation.
	 * @param the_id The String identifier of the MMMAnimationPhase.
	 * @param start_frame The starting frame number of the phase.
	 * @param end_frame The ending frame number of the phase.
	 * @param speed The number of milliseconds between each frame of the new phase.
	 * @return A reference to the newly created phase.
	 * @throws IllegalArgumentException if the identifier given is already registered, or start frame, end frame, or speed are out of their bounds.
	 */
	public MMMAnimationPhase createPhase(final String the_id, final int start_frame, final int end_frame, final long speed) {
		
		// First check to make sure the id is not already taken.
		if (phases.containsKey(the_id))
			throw new IllegalArgumentException("id \"" + the_id + "\" already taken");
		
		MMMAnimationPhase p = new MMMAnimationPhase(the_id, start_frame, end_frame, speed);
		
		phases.put(the_id, p);
		
		// If this is the first phase created (or the only one available), make it current.
		if (current_phase == null)
			current_phase = p;
		
		return p;
		
	}
	
	/**
	 * Set the mask color (tint) for this animation.
	 * @param color The Color to tint.
	 */
	public void setTintColor(final int color) {
		tint_color = color;
	}
	
	/**
	 * Retrieve the mast color (tint) for this animation.
	 * @return The color to tint.
	 */
	public int getTintColor() {
		return tint_color;
	}
	
	/** Signal to use the tint color provided to do rendering. **/
	public void useTint() {
		use_tint = true;
	}
	
	/** Signal to NOT use the tint color provided to do render. **/
	public void skipTint() {
		use_tint = false;
	}
	
	/** Set the rotation angle (in degrees) to rotate the image. **/
	public void setRotation(final int degrees) {
		rotation_angle_degrees = degrees;
	}
	
	/** Get the rotation angle (in degrees) currently being used on the image. **/
	public int getRotation() {
		return rotation_angle_degrees;
	}
	
	/**
	 * Get a bounding box of the current image.
	 * @return A Rect object, which is the bounding box of the animation/image.
	 */
	public Rect getBounds() {
		
		if (bmp == null)
			return null;
		
		Rect r = new Rect();
		
		r.top = (int)y;
		r.left = (int)x;
		r.right = (int)x + bmp.getWidth();
		r.bottom = (int)y + bmp.getHeight();
		
		return r;
		
	}
	
	/**
	 * Set the current animation phase to be the one specified by the String identifier.
	 * @param the_id The String identifier of the required phase.
	 */
	public void setCurrentPhase(final String the_id) {
		Log.i("StickmanPaintball", "MMMAnimation.setCurrentPhase() -- called!");
		current_phase = phases.get(the_id);
		
		if (current_phase == null)
			Log.i("StickmanPaintball", "MMMAnimation.setCurrentPhase() -- PHASE IS NOW NULL!");
		else {
			current_phase.reset();
			Log.i("StickmanPaintball", "MMMAnimation.setCurrentPhase() -- PHASE IS NOW \"" + current_phase.getID() + "\"");
		}
	}
	
	/**
	 * Retrieve the MMMAnimationPhase with the given String identifier, or null
	 * if not registered with this animation.
	 * @param the_id The String identifier.
	 * @return The MMMAnimationPhase reference, or null if not found.
	 */
	public MMMAnimationPhase getPhase(final String the_id) {
		MMMAnimationPhase p = phases.get(the_id);
		return p;
	}
	
	/**
	 * Remove the MMMAnimationPhase from register, with the given String identifier.
	 * @param the_id The String identifier.
	 */
	public void removePhase(final String the_id) {
		
		phases.remove(the_id);
		
		// If the current phase has this id, then set current phase null.
		if (current_phase.getID().equals(the_id))
			current_phase = null;
		
	}
	
	/**
	 * Retrieve the current phase String identifier, or null if no current phase.
	 * @return The current phase identifier.
	 */
	public String getCurrentPhaseID() {
		if (current_phase == null)
			return null;
		return current_phase.getID();
	}
	
	/**
	 * Start the animation phase, with the given String identifier, if it can be
	 * found.
	 * @param the_id The String identifier.
	 */
	public void startPhase(final String the_id) {
		MMMAnimationPhase p = phases.get(the_id);
		if (p != null)
			p.start();
	}
	
	/** Retrieve the pixel width of one sprite. **/
	public int getSpriteWidth() {
		return bmp.getWidth() / num_sprites_x;
	}
	
	/** Retrieve the pixel height of one sprite. **/
	public int getSpriteHeight() {
		return bmp.getHeight() / num_sprites_y;
	}
	
	/**
	 * Stop the animation phase, with the given String identifier, if it can be
	 * found.
	 * @param the_id The string identifier.
	 */
	public void stopPhase(final String the_id) {
		MMMAnimationPhase p = phases.get(the_id);
		if (p != null)
			p.stop();
	}
	
	/**
	 * Set the location of this MMMAnimation to the specified x and y
	 * values, within the current layer.
	 * @param the_x The new x location of this MMMAnimation.
	 * @param y_pos The new y location of this MMMAnimation.
	 */
	public void setLocation(final float the_x, final float y_pos) {
		setLocation(the_x, y_pos, zLayer);
	}
	
	/**
	 * Set the location of this MMMAnimation to the specified x, y,
	 * and layer values.
	 * @param the_x The new x location of this MMMAnimation.
	 * @param the_y The new y location of this MMMAnimation.
	 * @param the_layer The new layer for this MMMAnimation.
	 */
	public void setLocation(final float the_x, final float the_y, final float the_layer) {
		x = the_x;
		y = the_y;
		zLayer = the_layer;		
	}
	
	/**
	 * Translate the current location of this MMMAnimation by <code>the_dx</code>,
	 * <code>the_dy</code>, and keep the same layer.
	 * @param the_dx The delta x value.
	 * @param the_dy The delta y value.
	 */
	public void translate(final float the_dx, final float the_dy) {
		translate(the_dx, the_dy, 0);
	}
	
	/**
	 * Translate the current location of this MMMAnimation by <code>the_dx</code>,
	 * <code>the_dy</code>, and <code>the_layer_delta</code>.
	 * @param the_dx The delta x value.
	 * @param the_dy The delta y value.
	 * @param the_layer_delta The layer delta.
	 */
	public void translate(final float the_dx, final float the_dy, final float the_layer_delta) {
		x += the_dx;
		y += the_dy;
		zLayer += the_layer_delta;
	}
	
	/**
	 * Retrieve this MMMAnimation x location.
	 * @return The x location.
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Retrieve this MMMAnimation y location.
	 * @return The y location.
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Retrieve this MMMAnimations layer value, aka z value.
	 * @return The layer (z) location.
	 */
	public float getZ() {
		return getLayer(); 
	}
	
	/**
	 * Retrieve this MMMAnimation z value, aka layer.
	 * @return The z (layer) location.
	 */
	public float getLayer() {
		return zLayer;
	}
	
	/**
	 * Retrieve the scale currently being used to render
	 * this animation.
	 * @return The floating point scale of this object.
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * Specify the scale to be used to render this
	 * animation.
	 * @param scale The floating point scale of this object.
	 */
	public void setScale(final float scale) {
		if (scale > 0)
			this.scale = scale;
	}
	
	/**
	 * Signal the animation to update, given the specified number of milliseconds.
	 * @param dt The time delta. (Number of milliseconds to tick)
	 */
	public void tick(final long dt) {
	
		if (current_phase != null)
			current_phase.tick(dt);
		
	}
	
	/**
	 * Set the sprite sheet which corresponds to this animation. 
	 * @param c The Context in which the resource is located.
	 * @param resource_id The integer (ex - R.drawable.icon) id of the resource.
	 * @param sprites_x The number of sprites in the x direction.
	 * @param sprites_y The number of sprites in the y direction.
	 * @return True if the sprite sheet could be loaded. False otherwise.
	 */
	public boolean setSpriteSheet(final Context c, final int resource_id, final int sprites_x, final int sprites_y) {
	
		if (sprites_x < 1 || sprites_y < 1)
			throw new IllegalArgumentException("number of sprites on a sheet cannot be less than 1 in any direction");
		
		boolean ok = true;
		
		bmp = BitmapFactory.decodeResource(c.getResources(), resource_id);
		
		if (bmp == null) {
			ok = false;
			num_sprites_x = 0;
			num_sprites_y = 0;
		} else {
			num_sprites_x = sprites_x;
			num_sprites_y = sprites_y;
		}
		
		return ok;
		
	}
	
	public void draw(final Canvas c, final float the_x, final float the_y, final float the_z, final float the_scale) {
		
		// If the sprite sheet has not been loaded or no phases have been defined do not draw anything.
				if (bmp == null || phases.size() == 0 || current_phase == null) {
					Log.i("StickmanPaintball", "MMMAnimation.draw -- bmp:" + (bmp != null) + ", phases:" + phases.size() + ", current_phase:" + (current_phase != null));
					return;
				}
					
				
				// First get the correct, current, sprite frame number.
				int frame_num = current_phase.getFrameNumber();
				
				
				/*
				 * Frames are laid out in a sprite sheet from left to right, then top to bottom.
				 * Example:
				 *  ___ ___ ___ ___
				 * |_0_|_1_|_2_|_3_|
				 * |_4_|_5_|_6_|_7_|
				 * |_8_|_9_|10_|11_|
				 * 
				 */
				
				// Calculate width and height of sprites.
				final int sprite_width = bmp.getWidth() / num_sprites_x;
				final int sprite_height = bmp.getHeight() / num_sprites_y;
				
				// Calculate the actual location (sprite location) of required frame.
				final int sprite_row = frame_num / num_sprites_x;
				final int sprite_col = frame_num % num_sprites_x;
				
				// Setup a source rectangle to achieve a cutout of the required frame.
				Rect src = new Rect();
				
				src.top = sprite_height * sprite_row;
				src.left = sprite_width * sprite_col;
				src.bottom = src.top + sprite_height;
				src.right = src.left + sprite_width;
				
				// Setup a destination rectangle to transfer the frame to the correct location on screen.		
				Rect dst = new Rect();
				
				dst.top = (int)the_y;
				dst.left = (int)the_x;//- (int)(0.5f * (sprite_width * scale));
				dst.right = (int)the_x + Math.round(sprite_width * (the_scale * scale));
				dst.bottom = (int)the_y + Math.round(sprite_height * (scale * the_scale));
				
				Paint p = getUsablePaint();
				
				// Rotation should happen to canvas matrix before any calls to darw.
				c.rotate(rotation_angle_degrees, dst.left + (sprite_width * (scale * the_scale) * .5f), dst.top + (sprite_height * (scale * the_scale) * .5f));// + (float)((bmp.getWidth() * scale) / 2.0), y + (float)((bmp.getHeight() * scale) / 2.0));
				
				c.drawBitmap(bmp, src, dst, p);
				
				
				//if (rotation_angle_degrees != 0)
				c.rotate(-rotation_angle_degrees, dst.left + (sprite_width * (scale * the_scale) * .5f), dst.top + (sprite_height * (scale * the_scale) * .5f));
		
	}
	
	/**
	 * Draw the current frame of animation to the given canvas.
	 * @param c The canvas to draw on.
	 */
	public void draw(final Canvas c) {
		
		draw(c, x, y, zLayer, 1.0f);
		
		/*
		
		// If the sprite sheet has not been loaded or no phases have been defined do not draw anything.
		if (bmp == null || phases.size() == 0 || current_phase == null) {
			Log.i("StickmanPaintball", "MMMAnimation.draw -- bmp:" + (bmp != null) + ", phases:" + phases.size() + ", current_phase:" + (current_phase != null));
			return;
		}
			
		
		// First get the correct, current, sprite frame number.
		int frame_num = current_phase.getFrameNumber();
		
		*/
		/*
		 * Frames are laid out in a sprite sheet from left to right, then top to bottom.
		 * Example:
		 *  ___ ___ ___ ___
		 * |_0_|_1_|_2_|_3_|
		 * |_4_|_5_|_6_|_7_|
		 * |_8_|_9_|10_|11_|
		 * 
		 */
		/*
		// Calculate width and height of sprites.
		final int sprite_width = bmp.getWidth() / num_sprites_x;
		final int sprite_height = bmp.getHeight() / num_sprites_y;
		
		// Calculate the actual location (sprite location) of required frame.
		final int sprite_row = frame_num / num_sprites_x;
		final int sprite_col = frame_num % num_sprites_x;
		
		// Setup a source rectangle to achieve a cutout of the required frame.
		Rect src = new Rect();
		
		src.top = sprite_height * sprite_row;
		src.left = sprite_width * sprite_col;
		src.bottom = src.top + sprite_height;
		src.right = src.left + sprite_width;
		
		// Setup a destination rectangle to transfer the frame to the correct location on screen.		
		Rect dst = new Rect();
		
		dst.top = (int)y; //- (int)(0.5f * (sprite_height * scale)) ;
		dst.left = (int)x;//- (int)(0.5f * (sprite_width * scale));
		dst.right = (int)x + Math.round(sprite_width * scale);
		dst.bottom = (int)y + Math.round(sprite_height * scale);
		
		Paint p = getUsablePaint();
		
		// Rotation should happen to canvas matrix before any calls to darw.
		c.rotate(rotation_angle_degrees, dst.left + (sprite_width * scale * .5f), dst.top + (sprite_height * scale * .5f));// + (float)((bmp.getWidth() * scale) / 2.0), y + (float)((bmp.getHeight() * scale) / 2.0));
		
		c.drawBitmap(bmp, src, dst, p);
		
		
		//if (rotation_angle_degrees != 0)
		c.rotate(-rotation_angle_degrees, dst.left + (sprite_width * scale * .5f), dst.top + (sprite_height * scale * .5f));
		*/
	}
	
	/**
	 * Build a Paint object for use when drawing the animation to the 
	 * screen.
	 * @return A usable Paint object (for use with <code>drawBitmap(..) method).
	 */
	protected Paint getUsablePaint() {
		
		Paint p = null;
		
		if (use_tint) {
			
			p = new Paint();
			p.setColor(tint_color);
			p.setAntiAlias(true);
			p.setColorFilter(new LightingColorFilter(tint_color, 1));
			
		}
		
		return p;
		
	}
	
	/** Check whether the current phase is running. **/
	public boolean isRunning() {
		if (current_phase != null)
			return current_phase.isRunning();
		return false;
	}
	
	/** Stop the current phase. **/
	public void stopCurrentPhase() {
		if (current_phase != null)
			current_phase.stop();
	}
	
	/** Start the current phase. **/
	public void startCurrentPhase() {
		if (current_phase != null)
			current_phase.start();
	}
	
	/**
	 * The MMMAnimationPhase class describes a named animation, within
	 * the current MMMAnimation resource, which starts at <code>start_frame</code>
	 * and ends at <code>end_frame</code>. The speed at which this animation is
	 * played is determined by the number of milliseconds, <code>frame_speed</code>,
	 * specified.
	 * 
	 * @author Micahel Morris
	 * @version 7.3.2012
	 *
	 */
	protected class MMMAnimationPhase {
		
		/** The String identifier of this MMMAnimationPhase. **/
		private final String id;
		
		/** The starting frame number of this MMMAnimationPhase. **/
		private final int start_frame;
		
		/** The ending frame number of this MMMAnimationPhase. **/
		private final int end_frame;
		
		/** The number of milliseconds between each frame. **/
		private final long frame_speed;
		
		/** The current frame number. **/
		private int current_frame_num;
		
		/** The left over milliseconds from last frame. **/
		private int current_milli_count;
		
		/** Boolean flag to signal that this phase accepts frame ticks. **/
		private boolean running;
		
		public MMMAnimationPhase(final String the_id,
								 final int the_start_frame,
								 final int the_end_frame,
								 final long the_frame_speed) {
			if (the_id == null)
				throw new IllegalArgumentException("MMMAnimationPhase must have an identifier");
			if (the_start_frame < 0 || the_end_frame < 0)
				throw new IllegalArgumentException("start/end frame must be greater than 0");
			if (the_start_frame > the_end_frame)
				throw new IllegalArgumentException("start frame must be less than end frame");
			if (the_frame_speed < 1) {
				throw new IllegalArgumentException("frame speed must be greater than 1");
			}
			
			id = the_id;
			start_frame = the_start_frame;
			end_frame = the_end_frame;
			frame_speed = the_frame_speed;
			
			current_milli_count = 0;
			running = false;
			
		}
		
		/**
		 * Reset the millisecond count and frame number to starting location.
		 */
		public void reset() {
			current_milli_count = 0;
			current_frame_num = start_frame;
		}
		
		/**
		 * Issue an update signal to this animation phase with the specified
		 * time delta.
		 * @param dt The time delta. (Number of milliseconds to update)
		 */
		public void tick(final long dt) {
			
			if (!running)
				return;
			
			// Increment the millisecond count (including left over).
			current_milli_count += dt;
			
			// Increment frame number by correct amount.
			while (current_milli_count >= frame_speed) {
				current_frame_num++;
				if (current_frame_num > end_frame)
					current_frame_num = start_frame;
				current_milli_count -= frame_speed;
			}
				
		}
		
		/**
		 * Manually set the frame number to the one specified.
		 * @param frame The frame number.
		 */
		public void setFrame(final int frame) {
			if (frame >= start_frame && frame <= end_frame)
				current_frame_num = frame;
			else
				throw new IllegalArgumentException("cannot set frame to " + frame + " when limits are (inclusive) " + start_frame + "-" + end_frame);
		}
		
		/** Start this phase. **/
		public void start() {
			running = true;
		}
		
		/** Stop this phase. **/
		public void stop() {
			running = false;
		}
		
		/** Retrieve whether this phase is running. **/
		public boolean isRunning() {
			return running;
		}
		
		/** Retrieve the String identifier of this MMMAnimationPhase. **/
		public String getID() {
			return id;
		}
		
		/**
		 * Retrieve the current frame number.
		 * @return The frame number.
		 */
		public int getFrameNumber() {
			return current_frame_num;
		}
		
	}

	

}
