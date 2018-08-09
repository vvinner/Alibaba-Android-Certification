package com.porster.gift.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
*   启动类
*   @author Porster
*   @time   16/11/1 16:51
**/
public class IntentHelper {

    private static IntentHelper ourInstance = new IntentHelper();

    public static IntentHelper getInstance() {
        return ourInstance;
    }

    private IntentHelper() {
    }

    public static final String KEY1="Key1";
    public static final String KEY2="Key2";
    public static final String KEY3="Key3";
    public static final String KEY4="Key4";
    public static final String KEY5="Key5";
    public static final String KEY6="Key6";

    public static final int REQUEST_CODE= 900;
    public static final int RESULT_CODE= Activity.RESULT_OK;

    public static final String PUSH_DATA="push_json_data";


    public static void openClass(Context mContext, Class<?> cls){
        mContext.startActivity(new Intent(mContext,cls));
    }

    public static void openClassResult(Context mContext, Class<?> cls,int requestCode){
        ((Activity)mContext).startActivityForResult(new Intent(mContext,cls),requestCode);
    }
    public static void openClassResult(Context mContext, Class<?> cls,int requestCode,Bundle bundle){
        Intent q=new Intent(mContext,cls);
        q.putExtras(bundle);
        ((Activity)mContext).startActivityForResult(q,requestCode);
    }

    public static void openClass(Context mContext, Class<?> cls,Long value){
        mContext.startActivity(new Intent(mContext,cls).putExtra(IntentHelper.KEY1,value));
    }
    public static void openClass(Context mContext,Class<?> cls,String args){
        Intent q=new Intent(mContext,cls);
        q.putExtra(KEY1,args);
        mContext.startActivity(q);
    }
    public static void openClass(Context mContext,Class<?> cls,Bundle args){
        Intent q=new Intent(mContext,cls);
        q.putExtras(args);
        mContext.startActivity(q);
    }



    //---------------------------------启动网页--------------------------------------------//



}
