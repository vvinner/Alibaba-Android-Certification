package com.porster.gift.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.porster.gift.utils.AppConstants;

/**
*   SharedPreferences快捷使用
*   @author Porster
*   @time   16/11/3 01:41
**/
public class SessionData {

    public static final String SP_SKIP_ANIM="SP_SKIP_ANIM";


    /**
     * 获取自定义存储
     * @param key	健
     * @param value	存储类型 Long String Boolean....
     * @return 对象
     */
    public static Object getObject(Context mContex,String key,Object value) {

        SharedPreferences sp = mContex.getSharedPreferences(AppConstants.APP_SP, Context.MODE_PRIVATE);

        if(value instanceof String){
            return sp.getString(key, (String) value);
        }else if(value instanceof Integer){
            return sp.getInt(key, (Integer) value);
        }else if(value instanceof Boolean){
            return sp.getBoolean(key, (Boolean) value);
        }else if(value instanceof Long){
            return sp.getLong(key, (Long) value);
        }else{
            return value;
        }
    }
    /**
     * 自定义存储
     * @param key 健
     * @param value 值
     * @return 写入成功
     */
    public static boolean setObject(Context mContex,String key,Object value) {
        SharedPreferences sp = mContex.getSharedPreferences(AppConstants.APP_SP, Context.MODE_PRIVATE);
        boolean commit = false;
        if(value instanceof String){
            commit=sp.edit().putString(key, (String) value).commit();
        }else if(value instanceof Integer){
            commit=sp.edit().putInt(key, (Integer) value).commit();
        }else if(value instanceof Boolean){
            commit=sp.edit().putBoolean(key, (Boolean) value).commit();
        }else if(value instanceof Long){
            commit=sp.edit().putLong(key, (Long) value).commit();
        }
        return commit;
    }


}
