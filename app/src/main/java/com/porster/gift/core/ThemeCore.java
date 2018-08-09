package com.porster.gift.core;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.porster.gift.R;
import com.porster.gift.utils.ApiUtils;

/**
 * Created by Porster on 17/5/8.
 */

public class ThemeCore {
    public static final String THEME_COLOR="THEME_COLOR";
    @ColorInt
    public static final int THEME_DEF_COLOR=0xFFFD4831;

    public static void saveThemeColor(Activity act,int themeColor){
        if(themeColor==0){
            themeColor= ContextCompat.getColor(act,R.color.theme_color);
        }
        SessionData.setObject(act,THEME_COLOR,themeColor);
        setThemeColor(act,themeColor);
    }
    public static void setThemeColor(Activity act,int themeColor){
        if(themeColor==0){
            themeColor= (int) SessionData.getObject(act,THEME_COLOR,THEME_DEF_COLOR);
        }
        if(ApiUtils.isLolinpop()){
            act.getWindow().setStatusBarColor(getStateBarColor(themeColor));
        }
        View titleBar=act.findViewById(R.id.title_bar_layout);
        if (titleBar != null) {
            titleBar.setBackgroundColor(themeColor);
        }
    }
    public static void setThemeColor(View act,int themeColor){
        if(themeColor==0){
            themeColor= (int) SessionData.getObject(act.getContext(),THEME_COLOR,THEME_DEF_COLOR);
        }
        View titleBar=act.findViewById(R.id.title_bar_layout);
        if (titleBar != null) {
            titleBar.setBackgroundColor(themeColor);
        }
    }
    public static int getThemeColor(Activity act){
        return (int) SessionData.getObject(act,THEME_COLOR,THEME_DEF_COLOR);
    }


    public static int getStateBarColor(int themeColor){
        int alpha = (themeColor & 0xff000000) >>> 24;
        int red   = (themeColor & 0x00ff0000) >> 16;
        int green = (themeColor & 0x0000ff00) >> 8;
        int blue  = (themeColor & 0x000000ff);
        return Color.argb(alpha,Math.max(red-15,0),Math.max(green-18,0),Math.max(blue-22,0));
    }
}
