package com.porster.gift.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.porster.gift.R;

/**
 * Created by Porster on 17/2/22.
 */

public class PathSearch extends View {
    Paint mPaint;
    Path mPath;
    Bitmap mBitmap;

    PathMeasure mPathMeasure;

    float[] pos=new float[2];
    boolean mNeedBitmap;

    public PathSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);

        mPath=new Path();
//        mPath.moveTo(100,100);
//        mPath.lineTo(300,100);
//        mPath.lineTo(400,200);
//        mPath.lineTo(200,200);
            //默认封闭图形
//        mPath.close();

            //二阶贝塞尔
//        mPath.quadTo(200,200,300,100);
            //三阶贝塞尔
//        mPath.cubicTo(200,200,250,50,400,100);


        mBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.wel_search);
    }
    public void start(final OnPathEnd mOnPathEnd){
        ValueAnimator valueAnimatorCompat= ValueAnimator.ofFloat(0,mPathMeasure.getLength());
        valueAnimatorCompat.setDuration(1200);
        valueAnimatorCompat.setStartDelay(500);
        valueAnimatorCompat.setInterpolator(new DecelerateInterpolator());
        valueAnimatorCompat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value,pos,null);
                postInvalidate();
            }
        });
        valueAnimatorCompat.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mNeedBitmap=true;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
//                mNeedBitmap=false;
                if(mOnPathEnd!=null){
                    mOnPathEnd.OnEnd();
                }
            }
        });
        valueAnimatorCompat.setRepeatCount(1);
        valueAnimatorCompat.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int move=w/4;
        RectF rectF=new RectF(move,move,move*3,move*3);
        mPath.addArc(rectF,45,360);

        mPathMeasure=new PathMeasure(mPath,true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawPath(mPath,mPaint);
        if(mNeedBitmap){
            canvas.drawBitmap(mBitmap,pos[0]-mBitmap.getWidth()/2,pos[1]-mBitmap.getHeight()/2,mPaint);
        }
    }
    public interface OnPathEnd{
        void OnEnd();
    }
}
