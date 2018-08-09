package com.porster.gift.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.porster.gift.R;

/**
 * Created by Porster on 17/5/3.
 */

public class LightingView extends View{
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    int mWidth;
    int mHeight;
    int mLightWidth;
    int color;

    private Paint mPaint;
    private Path mPath;
    private Path mLightPath;

    public LightingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mLightPath=new Path();
        color=ContextCompat.getColor(getContext(), R.color.theme_color);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
        mLightWidth=w/3;

        mPath=new Path();
        //画出一个菱形
        mPath.moveTo(0,mHeight/2);

        mPath.lineTo(mWidth/2,0);

        mPath.lineTo(mWidth,mHeight/2);

        mPath.lineTo(mWidth/2,mHeight);

        mPath.close();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        mPaint.setColor(color);
        canvas.drawPath(mPath,mPaint);

        mPaint.setXfermode(xfermode);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white_light));
        canvas.drawPath(mLightPath,mPaint);
        mPaint.setXfermode(null);

        canvas.restoreToCount(sc);
    }

    //Animtion

    int moveX;
    ObjectAnimator startAnim;

    public void startLighting(){
        startAnim=ObjectAnimator.ofInt(this,"moveX",0,mWidth+mLightWidth);
        startAnim.setDuration(500);
        startAnim.setInterpolator(new AccelerateInterpolator());
        startAnim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                startAnim.setStartDelay(3000);
                startAnim.start();
            }
        });
        startAnim.start();
    }
    public void stopLighting(){
        startAnim.cancel();
    }
    public void setMoveX(int x){
        moveX=x;
        mLightPath.reset();

        mLightPath.moveTo(mWidth/2+moveX,0);//Top-Right

        mLightPath.lineTo(-mWidth/2+moveX,mHeight);//Bottom-Right

        mLightPath.lineTo(-mWidth/2-mLightWidth+moveX,mHeight);//Bottom-Left

        mLightPath.lineTo(mWidth/2-mLightWidth+moveX,0);//Top-Left

        mLightPath.close();

        postInvalidate();
    }
}
