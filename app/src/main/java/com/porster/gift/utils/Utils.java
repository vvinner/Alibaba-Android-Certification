package com.porster.gift.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Collection;

/**
 * 工具类
 * Created by Porster on 17/3/9.
 */

public class Utils {
    public static boolean isEmpty(Collection<?> mlist){
        return mlist==null||mlist.size()==0;
    }
    /**
     * 获取版本名称
     * @return      1.1.1
     */
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        String version="";
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
