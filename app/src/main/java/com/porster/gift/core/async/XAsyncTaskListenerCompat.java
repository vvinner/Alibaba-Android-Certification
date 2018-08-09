package com.porster.gift.core.async;


import android.content.Context;

/**
 * Created by Porster on 2018/1/29.
 */

public abstract  class XAsyncTaskListenerCompat<Result,Params> implements XAsyncTaskListener<Result,Params> {

    @Override
    public void onPostExecute(Context context,Result result) {

    }

    @Override
    public void onPreExecute(Context context) {

    }

    @Override
    public boolean isBindLifeCycle() {
        return true;
    }
}