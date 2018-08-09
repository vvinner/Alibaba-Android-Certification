package com.porster.gift;

import android.app.Application;

import com.porster.gift.core.DataManager;
import com.porster.gift.utils.LogCat;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Porster on 17/2/28.
 */

public class GApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        boolean debug=BuildConfig.DEBUG;
        LogCat.setDebug(debug);
        MobclickAgent.setDebugMode(debug);
        DataManager.getInstance().init(this);

    }
}
