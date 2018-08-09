package com.porster.gift.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
*   View常用的操作包
*   @author Porster
*   @time   16/11/1 14:09
**/
public class ViewUtil {

    /**
     * 获取状态栏高度
     * @return  状态栏高度
     */
    public static int getStateBarHeight(Context mCtx){
        int statusBarHeight;
        try {
            Class<?> clazz=Class.forName("com.android.internal.R$dimen");
            Object object=clazz.newInstance();
            Field field=clazz.getField("status_bar_height");
            int id = Integer.parseInt(field.get(object).toString());
            statusBarHeight = mCtx.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            statusBarHeight= dip2px(mCtx, 20);
        }
        return statusBarHeight;
    }


    public static TextView setDrawableToText(Context mCtx, int resId, TextView mTv){
        Drawable drawable = ContextCompat.getDrawable(mCtx,resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTv.setCompoundDrawables(drawable, null, null, null);
        return mTv;
    }
    public static TextView setDrawableToTextRight(Context mCtx, int resId, TextView mTv){
        Drawable drawable = ContextCompat.getDrawable(mCtx,resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTv.setCompoundDrawables(null, null, drawable, null);
        return mTv;
    }
    public static TextView setDrawableToTextTop(Context mCtx, int resId, TextView mTv){
        Drawable drawable = ContextCompat.getDrawable(mCtx,resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTv.setCompoundDrawables(null, drawable, null, null);
        return mTv;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public  static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**获取屏幕分辨率宽*/
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    /**获取屏幕分辨率高*/
    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

}
