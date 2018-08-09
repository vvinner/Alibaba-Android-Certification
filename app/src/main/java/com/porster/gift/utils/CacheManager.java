package com.porster.gift.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;


public class CacheManager {
	
	public static CacheManager manager;
	
	public static synchronized CacheManager getInstance(){
		if(manager==null){
			manager=new CacheManager();
			return manager;
		}
		return manager;
	}
	private CacheManager() {
		mCache=new LruCache<>(1024*1024*1024);
	}
	
	private LruCache<String, Object> mCache;
	
	public String createUserInfoCachePath(Context context,String uid) {
		return AppConstants.APP_SP+File.separator+"info"+uid+".data";
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> ArrayList<T> readList(Context context,String key){
		final String saveType= "list_"+key;
		String filePath = createUserInfoCachePath(context,saveType);
		
		Object obj=mCache.get(saveType);
		if(obj!=null){
			return (ArrayList<T>)obj;
		}
		
		ArrayList<T> bean=FeaturesUtils.readSerializable(filePath, false);
		return bean;
	}
	@SuppressWarnings("unchecked")
	public <T extends Object> ArrayList<T> readList(final Context context,String key,final Callback mCallback){
		final String saveType="list_"+key;
		
		Object obj=mCache.get(saveType);
		if(obj!=null){
			Message msg=Message.obtain();
			msg.obj=obj;
			mCallback.handleMessage(msg);
			return (ArrayList<T>)obj;
		}
		ApiUtils.execute(new AsyncTask<Object, Integer, Object>(){
			@Override
			protected Object doInBackground(Object... params) {
				return FeaturesUtils.readSerializable(context,saveType, false);
			}
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				Message msg=Message.obtain();
				msg.obj=result;
				mCallback.handleMessage(msg);
			}
		});
		return null;
	}
	public <T> void saveList(final Context context,final ArrayList<T> bean,final String key){
		ApiUtils.execute(new AsyncTask<Object, Integer, Object>(){
			@Override
			protected Object doInBackground(Object... params) {
				final String saveType="list_"+key;
				mCache.put(saveType,bean!=null?bean:new ArrayList<T>());
				FeaturesUtils.saveSerializable(context,saveType,bean);
				return null;
			}
		});
	}
	@SuppressWarnings("unchecked")
	public <T extends Object> T readObj(Context context,String key){
		final String saveType="obj_"+key;
		
		Object obj=mCache.get(saveType);
		if(obj!=null){
			return (T)obj;
		}
		
		String filePath = createUserInfoCachePath(context, saveType);
		T bean=FeaturesUtils.readSerializable(filePath, false);
		return bean;
	}
	/**
	 * 读取
	 * @param context
	 * @param key
	 * @param mCallback	回调
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T readObj(final Context context,String key,final Callback mCallback){
		final String saveType="obj_"+key;
		//从内存中查找
		Object obj=mCache.get(saveType);
		if(obj!=null){
			Message msg=Message.obtain();
			msg.obj=obj;
			mCallback.handleMessage(msg);
			return (T)obj;
		}
		//从本地查找
		ApiUtils.execute(new AsyncTask<Object, Integer, Object>(){
			@Override
			protected Object doInBackground(Object... params) {
				String filePath = createUserInfoCachePath(context, saveType);
				return FeaturesUtils.readSerializable(filePath, false);
			}
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				Message msg=Message.obtain();
				msg.obj=result;
				mCallback.handleMessage(msg);
			}
		});
		return null;
	}
	/**
	 * 保存
	 * @param key		
	 */
	public <T extends Serializable> void saveObj(final Context context,final T bean,final String key){
		ApiUtils.execute(new AsyncTask<Object, Integer, Object>(){
			@Override
			protected Object doInBackground(Object... params) {
				final String saveType="obj_"+key;
				mCache.put(saveType, bean);
				String filePath = createUserInfoCachePath(context,saveType);
				FeaturesUtils.saveSerializable(filePath,bean);
				return null;
			}
		});
	}
}