package com.porster.gift.core.async;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.AsyncTaskCompat;


import java.lang.ref.WeakReference;

/**
 * Activity中使用避免内存泄漏
 * 设置 {@link  XAsyncTaskListener#isBindLifeCycle()} true
 * Created by Porster on 2018/1/29.
 */

public class XAsyncTask {
    public static <Result,Params> void  execute(Context context, XAsyncTaskListener<Result,Params> asyncTask, Params...params){
        AsyncTaskCompat.executeParallel(new Task(context,asyncTask),params);
    }
    static class Task<Result,Params> extends AsyncTask<Params,Integer,Result> {

        WeakReference<Context> mContextWeakReference;
        XAsyncTaskListener<Result, Params> mAsyncTaskImpl;
        Task(Context context, XAsyncTaskListener<Result, Params> asyncTask){
            mContextWeakReference=new WeakReference<Context>(context);
            mAsyncTaskImpl=asyncTask;
        }

        @Override
        protected final Result doInBackground(Params... objects) {
            Context context=mContextWeakReference.get();
            return mAsyncTaskImpl.doInBackground(context,objects);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAsyncTaskImpl.onPreExecute(mContextWeakReference.get());
        }


        @Override
        protected void  onPostExecute(Result o) {
            Context context=mContextWeakReference.get();
            //如果启动绑定生命周期，则Activity被销毁时，不再回调
            if(mAsyncTaskImpl.isBindLifeCycle()){
                if (context != null) {
                    if(context instanceof FragmentActivity){
                        boolean isDestory=((FragmentActivity) context).getSupportFragmentManager().isDestroyed();
                        if(!isDestory){
                            mAsyncTaskImpl.onPostExecute(context,o);
                        }
                    }else{//其他类型不处理，如ApplicationContext
                        mAsyncTaskImpl.onPostExecute(context,o);
                    }
                }
            }else{
                mAsyncTaskImpl.onPostExecute(context,o);
            }
        }
    };
}
