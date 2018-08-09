package com.porster.gift.core.async;

import android.content.Context;

/**
 * Created by Porster on 2018/1/29.
 */

public interface XAsyncTaskListener<Result,Params>{


    public Result doInBackground(Context context, Params... params);

    public void onPostExecute(Context context, Result result);

    public void onPreExecute(Context context);

    /**
     * 是否启动生命周期绑定,启动后Activity被销毁，则不会再触发
     * @see #onPostExecute(Context,Object)
     * @return true 启动
     */
    public boolean isBindLifeCycle();

}
