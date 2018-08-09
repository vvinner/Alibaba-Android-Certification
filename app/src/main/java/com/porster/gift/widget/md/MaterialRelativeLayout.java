package com.porster.gift.widget.md;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.porster.gift.R;


/**
 * 为RelativeLayout添加Meterial Design效果
 * @version 2015-4-11 下午2:37:43
 */
public class MaterialRelativeLayout extends RelativeLayout implements MaterialBackgroundDetector.Callback {
    private MaterialBackgroundDetector mDetector;

    public MaterialRelativeLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public MaterialRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MaterialRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaterialTextView, defStyle, 0);
        int color = a.getColor(R.styleable.MaterialTextView_maskColor, MaterialBackgroundDetector.DEFAULT_COLOR);
        mDetector = new MaterialBackgroundDetector(getContext(), this, this, color);
        int radius=a.getDimensionPixelOffset(R.styleable.MaterialTextView_radiu, 0);
        if(radius>0){
        	mDetector.setRadius(radius);
        }
        a.recycle();
        
        //
       
    }
    public MaterialBackgroundDetector getDetector(){
    	return mDetector;
    }
    public void setMaterialBackgroundDetector(int color){
    	if(mDetector!=null){
    		mDetector.setColor(color);
    	}else{
            mDetector = new MaterialBackgroundDetector(getContext(), this, this, color);
    	}
    }
    /**
     * 隐藏按下的颜色
     */
    public void setHideBackGroud(){
    	mDetector.setShowFocusColor(false);
    	mDetector.setAlpha(55);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDetector.onSizeChanged(w, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(canTouched){
    		return super.onTouchEvent(event);
    	}
        boolean superResult = super.onTouchEvent(event);
        return mDetector.onTouchEvent(event, superResult);
    }
    boolean canTouched;
    public void requestDisableTouchEvent(){
    	canTouched=true;
    }
    public void cancelAnimator() {
        mDetector.cancelAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        mDetector.draw(canvas);
    }

    /**
     * When the view performClick, we should ensure the background animation appears.
     * So we will handle the action in mDetector;
     *
     * @return
     */
    @Override
    public boolean performClick() {
        return mDetector.handlePerformClick();
    }

    /**
     * When the view performClick, we should ensure the background animation appears.
     * So we will handle the action in mDetector;
     *
     * @return
     */
    @Override
    public boolean performLongClick() {
        return mDetector.handlePerformLongClick();
    }


    @Override
    public void performClickAfterAnimation() {
        super.performClick();
    }

    @Override
    public void performLongClickAfterAnimation() {
        super.performLongClick();
    }
}
