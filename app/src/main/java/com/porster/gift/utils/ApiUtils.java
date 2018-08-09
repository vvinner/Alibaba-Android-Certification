package com.porster.gift.utils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
/**
 * 一些高级API
 * @version 2014年10月24日 下午4:52:52
 */
public class ApiUtils {
	/** AsyncTask任务执行线程池 **/
	private static ExecutorService threadPool;
	
	public static boolean isLolinpop(){
		return VERSION.SDK_INT>=VERSION_CODES.LOLLIPOP;
	}
	public static boolean isMarshmallow(){
		return VERSION.SDK_INT>=VERSION_CODES.M;
	}

	public static boolean isJellyBean() {
		return VERSION.SDK_INT>=VERSION_CODES.JELLY_BEAN;
	}
	
	public static void removeOnGlobalLayoutListener(View view,OnGlobalLayoutListener victim){
		if(VERSION.SDK_INT>= VERSION_CODES.JELLY_BEAN){
			view.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
		}else{
			view.getViewTreeObserver().removeGlobalOnLayoutListener(victim);
		}
	}
	public static void setBackground(View view, Drawable background) {
		if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			view.setBackground(background);
		} else {
			view.setBackgroundDrawable(background);
		}
	}

	public static void execute(AsyncTask<Object, Integer,Object> task,Object...args) {
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			if (threadPool==null) {
				threadPool=Executors.newCachedThreadPool();
			}
			task.executeOnExecutor(threadPool,args);
		}else {
			task.execute(args);
		}
	}
}