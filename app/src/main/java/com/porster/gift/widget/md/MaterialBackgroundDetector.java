/*
 * Copyright 2015 kifile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.porster.gift.widget.md;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;


/**
 * Android5.0点击动画
 * 基于ObjectAnimator开发
 * @author wuming
 * @version 2015-4-11 下午2:35:56
 */
public class MaterialBackgroundDetector {
    protected static final String TAG = "MaterialBackground";
    protected static final boolean DBG = false;

    private static final int DEFAULT_DURATION = 1200;
    private static final int DEFAULT_FAST_DURATION = 300;
    private static final int DEFAULT_TRANSPARENT_DURATION = 300;

    private static final int DEFAULT_ALPHA = 55;
    public static final int DEFAULT_COLOR = Color.parseColor("#bababa");
    protected boolean showFocusColor=true;
    protected boolean showRippleAnima=true;
    
    public boolean isShowRippleAnima() {
		return showRippleAnima;
	}

	public void setShowRippleAnima(boolean showRippleAnima) {
		this.showRippleAnima = showRippleAnima;
	}

	public void setShowFocusColor(boolean showFocusColor) {
		this.showFocusColor = showFocusColor;
	}
	/*package*/protected View mView;
    /*package*/ Callback mCallback;

    protected int mColor;
    protected Paint mCirclePaint;
    protected int mFocusColor;
    protected int mCircleColor;

    //The position of the initial circle center.
    private float mX;
    private float mY;
    //The position of curr circle center.
    protected float mCenterX;
    protected float mCenterY;
    protected float mViewRadius;
    //The cornerRadius of curr circle.
    protected float mRadius;
    //The size of view.
    protected int mWidth;
    protected int mHeight;
    protected int mMinPadding;

    private ObjectAnimator mAnimator;
    /*package*/protected boolean mIsAnimation;
    /*package*/protected boolean mIsCancelAnimation;
    /*package*/protected boolean mHasDrawMask;
    public boolean mIsPressed;

    private boolean mIsPerformClick;
    private boolean mIsPerformLongClick;

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mIsAnimation = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mHasDrawMask = false;
            mIsAnimation = false;
            mView.invalidate();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private Animator.AnimatorListener mCancelAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mIsCancelAnimation = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mHasDrawMask = false;
            mIsCancelAnimation = false;
            mView.invalidate();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public MaterialBackgroundDetector(Context context, View view, Callback callback, int color) {
        mView = view;
        mCallback = callback;
        setColor(color);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinPadding = configuration.getScaledEdgeSlop();
    }
    protected int cornerRadius;
    protected boolean hasSetCorners;
    protected boolean hasLast;
    /**
     * 设置圆角
     * @param	 radiuPx	像素
     */
    public void setRadius(int radiuPx){
    	hasSetCorners=true;
    	this.cornerRadius=radiuPx;
    }
    
    
    public void setColor(int color) {
        if (mColor != color) {
            if (DBG) {
                Log.d(TAG, "ColorChanged");
            }
            mColor = color;
            setAlpha(DEFAULT_ALPHA);
        }
    }

    private void resetPaint() {
        if (mCirclePaint == null) {
            mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        mCirclePaint.setColor(mCircleColor);
    }

    private int computeCircleColor(int color, int alpha) {
        return getColorAtAlpha(color, alpha);
    }

    private int computeFocusColor(int color, int alpha) {
        return getColorAtAlpha(color, alpha);
    }

    /**
     * Called when view size changed.
     *
     * @param width
     * @param height
     */
    public void onSizeChanged(int width, int height) {
        mWidth = width;
        mHeight = height;
        mViewRadius = (float) Math.sqrt(mWidth * mWidth / 4 + mHeight * mHeight / 4);
    }
    public boolean onTouchEvent(MotionEvent event, boolean result) {
    	if(!mView.isEnabled()){
    		return true;
    	}
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsPressed = true;
                if (!mIsAnimation) {
                    //Ensure there is only one shadow animation.
                    startShadowAnimation(event);
                }
                // Ensure the following motion event can be received.
                if (!result) {
                    result = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = event.getX();
                final float y = event.getY();
                if (x >= 0 && x <= mWidth && y >= 0 && y <= mHeight) {
                    moveShadowCenter(x, y);
                } else {
                    //When this figure move outside, we should cancel animation.
                    cancelShadowAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
            	mView.performClick();
            case MotionEvent.ACTION_CANCEL:
                cancelShadowAnimation();

                break;
        }
        return result;
    }


    /**
     *
     */
    private void startShadowAnimation(MotionEvent event) {
        //When the fingers touch the view, we let the mask appear slowly.
        mX = event.getX();
        mY = event.getY();
        mAnimator = ObjectAnimator.ofFloat(this, "radius", mMinPadding, mViewRadius);
        int mDuration = DEFAULT_DURATION;
        mAnimator.setDuration(mDuration);
        mAnimator.setInterpolator(mInterpolator);
        mAnimator.addListener(mAnimatorListener);
        mAnimator.start();
        if (DBG) {
            Log.i(TAG, "Down,from:" + 0 + ",to:" + mViewRadius);
        }
    }

    /**
     *
     */
    private void cancelShadowAnimation() {
        if (mIsCancelAnimation) {
            return;
        }
        mIsPressed = false;
        //Cancel the shadow animation before the fast cancel animation.
        cancelAnimator();
        mX = mCenterX;
        mY = mCenterY;
        mRadius = Math.max(mRadius, mViewRadius * 0.1f);
            
        int  duration = (int) (DEFAULT_FAST_DURATION * (mViewRadius - mRadius) / mViewRadius);
        if (duration > 0) {
            //When the fingers leave the view, if the mask doesn't cover whole view, we let the mask appear fast.
        	mAnimator = ObjectAnimator.ofFloat(this, "radius", mRadius, mViewRadius);
            mAnimator.setDuration(duration);
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.addListener(mCancelAnimatorListener);
            mAnimator.start();
            if (DBG) {
                Log.i(TAG, "UP,from:" + mRadius + ",to:" + mViewRadius);
            }
        }
        if (duration > 0) {
            showAlphaAnimation();
        }
        mView.invalidate();
    }

    /**
     * We should let the mask layer disappear gradually.
     */
    private void showAlphaAnimation() {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(this, "alpha", DEFAULT_ALPHA, 0);
        alphaAnimator.setDuration(DEFAULT_TRANSPARENT_DURATION);
        alphaAnimator.setInterpolator(new AccelerateInterpolator());
        alphaAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //When the animation end, we should do something.
                mHasDrawMask = false;
                mIsAnimation = false;
                //Reset the alpha value.
                setAlpha(DEFAULT_ALPHA);
                //Handle the click event.
                if (mIsPerformClick) {
                    if (mCallback != null) {
                        mCallback.performClickAfterAnimation();
                    }
                    mIsPerformClick = false;
                }
                if (mIsPerformLongClick) {
                    if (mCallback != null) {
                        mCallback.performLongClickAfterAnimation();
                    }
                    mIsPerformLongClick = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        alphaAnimator.start();
    }

    /**
     * @param x
     * @param y
     */
    private void moveShadowCenter(float x, float y) {
        //When move, the shadow circle should be redraw.
        mHasDrawMask = false;
        mX = x;
        mY = y;
    }

    public void cancelAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    public void draw(Canvas canvas) {
        if (mIsPressed || mIsAnimation || mIsCancelAnimation) {
            //If client is focused or in animation, show focus layer.
            if (DBG) {
                Log.d(TAG, "DrawFocusColor");
            }
            mHasDrawMask = true;
            if(mView.isEnabled()){
	            if(isShowFocusColor())
	            	if(hasSetCorners){
//	            		canvas.drawRoundRect();
	            		Rect rect=new Rect(0,0,mWidth,mHeight);
	            		RectF rectF=new RectF(rect);
	            		canvas.drawRoundRect(rectF,cornerRadius,cornerRadius,mCirclePaint);
	            	}else{
		            	canvas.drawColor(mFocusColor);
	            	}
	            if(isShowRippleAnima()){
	            	if(hasLast){

	            		Rect rect=new Rect(0,0,mWidth,mHeight);
	            		RectF rectF=new RectF(rect);
	            		canvas.drawRoundRect(rectF,cornerRadius,cornerRadius,mCirclePaint);
	            	}else{
		            	canvas.drawCircle(mCenterX, mCenterY, mRadius, mCirclePaint);
	            	}
	            }
            }
        }
    }
    /**
     * This method is called by ObjectAnimator to invalidate mView.
     *
     * @param radius
     */
    public void setRadius(float radius) {
        float percent = 0;
        if (mAnimator != null) {
            percent = mAnimator.getAnimatedFraction();
            hasLast=percent==1.0;
        }
        mRadius = radius;
        hasLast=(mViewRadius-mRadius)<=this.cornerRadius/2;
        mCenterX = mX + percent * (mWidth / 2 - mX);
        mCenterY = mY + percent * (mHeight / 2 - mY);
        float distance = (float) Math.sqrt((mCenterX - mX) * (mCenterX - mX) + (mCenterY - mY) * (mCenterY - mY)) + mMinPadding;
        if (distance > radius) {
            mCenterX = mX + (mCenterX - mX) * radius / distance;
            mCenterY = mY + (mCenterY - mY) * radius / distance;
        }
        if (mView instanceof ViewGroup) {
            mView.setWillNotDraw(false);
        }
        if (mHasDrawMask) {
            //If mask has been drawn, we could only invalidate the circle rect.
            //To improve the efficiency.
            mView.invalidate((int) (mCenterX - mRadius), (int) (mCenterY - mRadius),
                    (int) (mCenterX + mRadius), (int) (mCenterY + mRadius));
        } else {
            mView.invalidate();
        }
    }

    /**
     * This method is called by ObjectAnimator to let the mask layer be transparent.
     *
     * @param alpha
     */
    public void setAlpha(int alpha) {
    	mFocusColor = computeFocusColor(mColor, alpha);
        mCircleColor = computeCircleColor(mColor, alpha);
        resetPaint();
        if (mView instanceof ViewGroup) {
            mView.setWillNotDraw(false);
        }
        mView.invalidate();
    }
    /**
     * Show Touch down focus BackgroundColor
     * @return isShow?
     */
    public boolean isShowFocusColor(){
    	return showFocusColor;
    }
    /**
     * It only happens when the fingers up.
     * See also {@link #handlePerformLongClick()}
     *
     * @return whether it is been handled.
     */
    public boolean handlePerformClick() {
        boolean result = mIsPerformClick;
        mIsPerformClick = true;
        return result;
    }

    /**
     * It only happens when the fingers up.
     * See also {@link #handlePerformClick()} ()}
     *
     * @return whether it is been handled.
     */
    public boolean handlePerformLongClick() {
        boolean result = mIsPerformLongClick;
        mIsPerformLongClick = true;
        return result;
    }

    /**
     * The Callback interface will call handle the click event after animation.
     */
    public interface Callback {
        /**
         * Handle click event after animation.
         */
        void performClickAfterAnimation();

        /**
         * Handle long click event after animation.
         */
        void performLongClickAfterAnimation();
    }
    /**
     * Get the value of color with specified alpha.
     *
     * @param color
     * @param alpha between 0 to 255.
     * @return Return the color with specified alpha.
     */
    public static int getColorAtAlpha(int color, int alpha) {
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("The alpha should be 0 - 255.");
        }
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}
