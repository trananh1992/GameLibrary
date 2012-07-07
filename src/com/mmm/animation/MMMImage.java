package com.mmm.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * The MMMImage class is a subclass of MMMAnimation. The behavior is
 * almost the exact same, however an MMMImage is designed to be
 * an animation with only one frame (therefore only one phase). Good for static images.
 * The phase creation and frame setup are taken care of for you while you
 * use MMMImage for static images.
 * 
 * Last Edited by: Michael Morris (7.6.2012)
 * @author Michael Morris
 * @version 1.2.0
 *
 */
public class MMMImage extends MMMAnimation {
	/**
	 * Create a new MMMImage object with the given resource, at position
	 * (0,0,0).
	 * @param c The context to load the resources.
	 * @param resource_id The resource identifier.
	 */
	public MMMImage(Context c, final int resource_id) {
		this(c, resource_id, 0f, 0f, 0);
	}
	
	/**
	 * Create a new MMMImage object with the given resource, and the
	 * specified x and y. The z (layer) location will be set to 0.
	 * @param c The context to load the resource.
	 * @param resource_id The resource identifier.
	 * @param x The desired x location.
	 * @param y The desired y location.
	 */
	public MMMImage(Context c, final int resource_id, final float x, final float y) {
		this(c, resource_id, x, y, 0);
	}
	
	/**
	 * Create a new MMMImage object with the given resource, and the
	 * specified x, y, and z (layer) location.
	 * @param c The context to load the resource.
	 * @param resource_id The resource identifier.
	 * @param x The desired x location.
	 * @param y The desired y location.
	 * @param layer The desired z (layer) location.
	 */
	public MMMImage(Context c, final int resource_id, final float x, final float y, final int layer) {
		
		super(x, y, layer);
		
		setSpriteSheet(c, resource_id, 1, 1);
		
		createPhase("phase", 0, 0, 1);
		
		startPhase("phase");
		
		scale = 1.0f;
		
	}
	
	/**
	 * Set the scale of the image.<BR>
	 * NOTE: Scale must be greater than 0.
	 * @param scale The floating point scale of the image.
	 */
	//public void setScale(final float scale) {
	//	if (scale > 0)
	//		this.scale = scale;
	//}
	
	/**
	 * Get the current scale of this image.
	 * @return The floating point scale of this image.
	 */
	//public float getScale() {
	//	return scale;
	//}
	
	@Override
	public Rect getBounds() {
		
		if (bmp == null)
			return null;
		
		Rect r = super.getBounds();
		r.right = (int)x + Math.round(bmp.getWidth() * scale);
		r.bottom = (int)y + Math.round(bmp.getHeight() * scale);
		
		return r;
		
	}
	
	@Override
	public void tick(final long dt) {
		// Do nothing.
	}
	
	/**
	 * Draw the image (with/without scale) to the given canvas.
	 * @param c The canvas to draw on.
	 */
	@Override
	public void draw(Canvas c) {
		
		// Rotation should happen to canvas matrix before any calls to darw.
		c.rotate(rotation_angle_degrees, x + (float)((bmp.getWidth() * scale) / 2.0), y + (float)((bmp.getHeight() * scale) / 2.0));
		
		final Paint p = getUsablePaint();
		
		// If the scale is anything but 1 (normal scale)
		// design source and destination rectangles for effective scaling.
		if (scale != 1.0f) {

			Rect src = new Rect();
			src.top = 0;
			src.left = 0;
			src.right = bmp.getWidth();
			src.bottom = bmp.getHeight();
			
			Rect dst = new Rect();
			dst.top = (int)y;
			dst.left = (int)x;
			dst.right = (int)x + (int)(bmp.getWidth() * scale);
			dst.bottom = (int)y + (int)(bmp.getHeight() * scale);
			
			//
			c.drawBitmap(bmp, src, dst, p);
		
		// If the scale is 1.0f there is no need for wasting the time
		// to create rectangles.
		} else {
			
			c.drawBitmap(bmp, x, y, p);
			
		}
		
		c.rotate(-rotation_angle_degrees, x + (float)((bmp.getWidth() * scale) / 2.0), y + (float)((bmp.getHeight() * scale) / 2.0));
		
	}
		
}
