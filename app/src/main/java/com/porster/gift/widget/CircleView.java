package com.porster.gift.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆
 * Created by Porster on 17/5/8.
 */

public class CircleView extends View{

    private Paint mPaint;

    private int color;

    public void setColor(int color) {
        this.color = color;
        mPaint.setColor(color);
        invalidate();
    }

    private int w,h;

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        color= Color.BLUE;
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w=getMeasuredWidth();
        h=getMeasuredHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(w/2,h/2,w/2,mPaint);
    }
}
