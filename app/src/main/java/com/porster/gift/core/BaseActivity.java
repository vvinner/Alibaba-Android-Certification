package com.porster.gift.core;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.porster.gift.R;
import com.porster.gift.utils.ApiUtils;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.utils.ViewHolder;
import com.porster.gift.utils.ViewUtil;
import com.porster.gift.widget.TitleBar;

/**
 * Created by Porster on 17/2/22.
 */

public class BaseActivity extends FragmentActivity{
    private SparseArray<View> mViewCache = new SparseArray<>();

    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext=this;

//        getWindow().getDecorView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                devModel();
//            }
//        },1000);
    }
    /**
     * 根据ID获取控件
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T $(int id) {
        View cacheView = mViewCache.get(id);
        if (cacheView != null) {// 从缓存中获取，减少findViewById的次数
            return (T) cacheView;
        }
        // 从xml中查找
        View view = findViewById(id);
        if (view != null) {// 查找到
            mViewCache.put(id, view);
        }
        return (T) view;
    }

    /**
     * 根据ID获取控件
     *
     * @param id
     * @param onClickListener
     *            顺便设置点击事件
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T $(int id, View.OnClickListener onClickListener) {
        View view = $(id);
        if (onClickListener != null)
            view.setOnClickListener(onClickListener);
        return (T) view;
    }
    public TextView setText(int id,String s){
        TextView t=$(id);
        t.setText(s);
        return t;
    }

    /**
     * 隐藏一个view
     *
     * @param view
     */
    public void GONE(View view) {
        if (view == null)
            return;
        view.setVisibility(View.GONE);
    }

    /**
     * 显示一个view
     *
     * @param view
     */
    public void VISIBLE(View view) {
        if (view == null)
            return;
        view.setVisibility(View.VISIBLE);
    }

    /** 提示 */
    private Toast t;
    private String text;

    /**
     * 显示一个toast。如果显示的内容相同,只会显示一次
     *
     * @param text
     */
    public void showToast(String text) {
        if (t != null) {
            if (TextUtils.isEmpty(text)) {
                text = "";
            }
            if (!TextUtils.equals(this.text,text)) {
                t = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
            }
        } else {
            t = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 0, 0);
        }
        this.text = text;
        t.show();
    }

    /**
     * 显示一个居中的Toast
     *
     * @param msg
     *            StringId
     */
    public void showToast(int msg) {
        showToast(getString(msg));
    }

    /**支持5.0
     * 5.0可以让布局延伸到状态栏*/
    public void supportLolinpop(){
        if(ApiUtils.isLolinpop()){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    /**
     * 添加点击效果
     *
     * @param v
     *            需要触发的View
     * @return view自身
     */
    public View addClickEffect(View v) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ObjectAnimator.ofFloat(v, "alpha", 0.3f, 1.0f).start();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.3f).start();
                        break;
                }
                return false;
            }
        });
        return v;
    }
    public void to(Class<?> cls){
        IntentHelper.openClass(mContext,cls);
    }
    public TitleBar addActionBar(String title){
        ThemeCore.setThemeColor(this,0);
        return new TitleBar($(R.id.title_bar_layout),mViewCache).show(title);
    }
    /** 弹出框等待条 */
    protected ProgressDialog pDialog;

    /**
     * 等待框
     *
     * @param message
     *            需要提示的信息
     * @return
     */
    public android.app.Dialog showProgressDialog(String message) {
        if (pDialog == null || !pDialog.isShowing()) {
            pDialog = new ProgressDialog(mContext, R.style.ProgressDialogStyle);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            ViewGroup group = null;
            View view = LayoutInflater.from(mContext).inflate(R.layout.progres_dialog, group);
            pDialog.setContentView(view);
        }
        TextView tost=(TextView) pDialog.findViewById(R.id.textView1);
        tost.setText(message);
        tost.setVisibility(TextUtils.isEmpty(message)?View.GONE:View.VISIBLE);
        return pDialog;
    }

    /**
     * 隐藏等待框
     */
    public void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
            pDialog.dismiss();
        }
        pDialog = null;
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    /**
     * 开发模式
     */
    public void devModel(){

    }
    public void startDevModel(boolean needStart){
        if(!needStart){
            return;
        }
        showToast("开始自动答题");
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                devModel();
            }
        },1000);
    }
    public View showEmptyView(String mErrorToast){
        return showEmptyView(mErrorToast,R.drawable.no_data);
    }
    public View showEmptyView(String mErrorToast, int errorIcon){
        return showEmptyView(mErrorToast,errorIcon,false,null);
    }


    protected View mEmptyView;
    public View showEmptyView(String mErrorToast, int mErrorRedId, boolean showProgress, final Handler.Callback mReload){
        //如果找到了,说明布局里已经存在
        if(mEmptyView==null){
            mEmptyView=$(R.id.empty_layout);

            if(mEmptyView==null){
                mEmptyView= LayoutInflater.from(mContext).inflate(R.layout.layout_list_empty_view,null);
            }
        }
        VISIBLE(mEmptyView);

        TextView error= ViewHolder.get(mEmptyView,R.id.empty_toast);
        if(!TextUtils.isEmpty(mErrorToast)){
            error.setText(mErrorToast);
        }
        if(mErrorRedId>0){
            ViewUtil.setDrawableToTextTop(mContext,mErrorRedId,error);
        }else{
            error.setCompoundDrawables(null,null,null,null);
        }

        ProgressBar progressBar=ViewHolder.get(mEmptyView,R.id.empty_load);
        if(showProgress){
            VISIBLE(progressBar);
        }else{
            GONE(progressBar);
        }
        //重载按钮
        View view=ViewHolder.get(mEmptyView,R.id.empty_reload);
        if(mReload!=null){
            VISIBLE(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReload.handleMessage(Message.obtain());
                }
            });
        }else{
            GONE(view);
        }
        return mEmptyView;
    }
}
