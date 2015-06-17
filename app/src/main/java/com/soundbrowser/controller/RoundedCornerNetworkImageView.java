package com.soundbrowser.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.soundbrowser.R;

public class RoundedCornerNetworkImageView extends NetworkImageView {

	/** The m radius. */
	private int mRadius;
 
	/** The is circular. */
	private boolean isCircular;
 
	/** The drawable. */
	private StreamDrawable mDrawable;
 
	/** The m margin. */
	private int mMargin;
 
	/** The is shadowed. */
	private boolean isShadowed;
	
	private Paint mCirclePaint;
	
	private boolean drawCircle;
	
	private int mCircleColor;
 
 
	/**
	 * Instantiates a new rounded corner network image view.<br/>
	 * Initializes <code>RoundedCornerNetworkImageView</code> with default radius of 4px, margin 0px and with no shadowing. 
	 *
	 * @param context the context
	 */
	public RoundedCornerNetworkImageView(Context context) {
		super(context);
		mRadius = 4;
		mMargin = 0;
		isCircular = false;
		isShadowed = false;
		drawCircle = false;
		mCircleColor = Color.WHITE;
		setWillNotDraw(false);
		mCirclePaint = new Paint();
		mCirclePaint.setStrokeWidth(4f);
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Style.STROKE);
		if(isCircular)
			isShadowed = false;
	}
 
	/**
	 * Instantiates a new rounded corner network image view.
	 * Initializes <code>RoundedCornerNetworkImageView</code> with radius provided in <code>radius</code> attribute in <code>XML</code> or default 4px,<br/> margin with <code>radius</code> attribute in <code>XML</code> default 0px,<br/>
	 * if <code>isShadowing</code> attribute is true shadows will be drawn on image <br/>and if <code>isCircular</code> attribute is true radius will be ignored and a circular image will be drawn.
	 *
	 * @param context the context
	 * @param attribs the AttributeSet
	 */
	public RoundedCornerNetworkImageView(Context context,AttributeSet attribs) {
		super(context, attribs);
		TypedArray a=getContext().obtainStyledAttributes(
				attribs,
				R.styleable.RoundedCornerNetworkImageView);
		mRadius = a.getInt(
				R.styleable.RoundedCornerNetworkImageView_radius,4);
		mMargin = a.getInt(
				R.styleable.RoundedCornerNetworkImageView_margin,0);
		isShadowed = a.getBoolean(
				R.styleable.RoundedCornerNetworkImageView_isShadowPresent,false);
		
		isCircular = a.getBoolean(R.styleable.RoundedCornerNetworkImageView_isCircular, false);
		
		drawCircle = a.getBoolean(R.styleable.RoundedCornerNetworkImageView_drawCircle, false);
		
		mCircleColor = a.getColor(R.styleable.RoundedCornerNetworkImageView_circleColor, Color.WHITE);
		
		int src_resource = attribs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
		if(src_resource != 0)
			setImageResource(src_resource);
		a.recycle();
		if(isCircular)
			isShadowed = false;
		setWillNotDraw(false);
		mCirclePaint = new Paint();
		mCirclePaint.setStrokeWidth(4f);
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Style.STROKE);
	}
 
	/**
	 * Instantiates a new rounded corner network image view.
	 * Initializes <code>RoundedCornerNetworkImageView</code> with radius provided in <code>radius</code> attribute in <code>XML</code> or default 4px,<br/> margin with <code>radius</code> attribute in <code>XML</code> default 0px,<br/>
	 * if <code>isShadowing</code> attribute is true shadows will be drawn on image, <br/>if <code>isCircular</code> attribute is true radius will be ignored and a circular image will be drawn.<br/>
	 * and with defStyle provided.
	 * @param context the context
	 * @param attribs the AttributeSet
	 * @param defStyle the defStyle
	 */
	public RoundedCornerNetworkImageView(Context context,AttributeSet attribs,int defStyle) {
		super(context, attribs, defStyle);
		TypedArray a=getContext().obtainStyledAttributes(
				attribs,
				R.styleable.RoundedCornerNetworkImageView);
		mRadius = a.getInt(
				R.styleable.RoundedCornerNetworkImageView_radius,4);
		mMargin = a.getInt(
				R.styleable.RoundedCornerNetworkImageView_margin,0);
		isCircular = a.getBoolean(R.styleable.RoundedCornerNetworkImageView_isCircular, false);
		isShadowed = a.getBoolean(
				R.styleable.RoundedCornerNetworkImageView_isShadowPresent,false);
 
		drawCircle = a.getBoolean(R.styleable.RoundedCornerNetworkImageView_drawCircle, false);
 
		mCircleColor = a.getColor(R.styleable.RoundedCornerNetworkImageView_circleColor, Color.WHITE);
 
 
		int src_resource = attribs.getAttributeResourceValue(
			"http://schemas.android.com/apk/res/android", "src",
//			R.drawable.user_thumbnail2
			R.drawable.ic_launcher
		);
		if(src_resource != 0)
			setImageResource(src_resource);
		a.recycle();
		setWillNotDraw(false);
		mCirclePaint = new Paint();
		mCirclePaint.setStrokeWidth(4f);
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Style.STROKE);
		if(isCircular)
			isShadowed = false;
	}
 
	/**
	 * Sets the radius.
	 *
	 * @param radius the new radius
	 */
	public void setRadius(int radius){
		mRadius = radius;
	}
 
	/**
	 * Sets the isCircular.
	 *
	 * @param isCircular true for circular ImageView
	 */
	public void setCircular(boolean isCircular){
		this.isCircular = isCircular;
	}
 
	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	public int getRadius() {
		return mRadius;
	}
 
	/**
	 * Checks if is circular.
	 *
	 * @return true, if is circular
	 */
	public boolean isCircular() {
		return isCircular;
	}
 
	/**
	 * Gets the margin.
	 *
	 * @return the m margin
	 */
	public int getmMargin() {
		return mMargin;
	}
 
	/**
	 * Sets the margin.
	 *
	 * @param margin the new margin
	 */
	public void setmMargin(int margin) {
		this.mMargin = margin;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isCircular && drawCircle)
			canvas.drawCircle((this.getWidth()*1.0f)/2, (this.getHeight()*1.0f)/2, (this.getHeight()*1.0f)/2-1.8f, mCirclePaint);
	}
 
	/* (non-Javadoc)
	 * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
	 */
	@Override
	public void setImageBitmap(Bitmap bm) {
//		Log.e("CAME IN SET IMAGE BITMAP");
		if (bm != null) {
//			Log.e("CAME IN BITMAP NOT NULL");
			if(isCircular){
				mRadius = (int) Math.max(this.getWidth()/2, this.getHeight()/2);
			}
			mDrawable = new StreamDrawable(bm, mRadius, mMargin,isShadowed,this.getScaleType());
		} else {
//			Log.e("CAME IN BITMAP NULL");
			mDrawable = null;
		}
 
		super.setImageDrawable(mDrawable);
	}
	
	@Override
	public void setImageResource(int resId) {
		setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
//		Log.e("CAME IN DRAWABLE");
		if(drawable instanceof BitmapDrawable){
//			Log.e("CAME IN BITMAP DRAWABLE");
			setImageBitmap(((BitmapDrawable)drawable).getBitmap());
		}else if(drawable instanceof StreamDrawable){
//			Log.e("CAME IN STREAM DRAWABLE");
			super.setImageDrawable(drawable);
		}else if(drawable instanceof ColorDrawable){
//			Log.e("CAME IN COLOR DRAWABLE");
			Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Config.ARGB_8888);
		    Canvas canvas = new Canvas(bitmap); 
		    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		    drawable.draw(canvas);
		    setImageBitmap(bitmap);
		}else{
//			Log.e("CAME IN ELSE");
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
		    Canvas canvas = new Canvas(bitmap); 
		    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		    drawable.draw(canvas);
		    setImageBitmap(bitmap);
		}
	}
}
