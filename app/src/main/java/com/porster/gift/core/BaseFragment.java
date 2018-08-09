package com.porster.gift.core;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.porster.gift.R;
import com.porster.gift.utils.ViewHolder;
import com.porster.gift.utils.ViewUtil;
import com.porster.gift.widget.TitleBar;

/**
 * Created by Porster on 17/2/23.
 */

public class BaseFragment extends Fragment{
    public Context mContext;
    public View mainView;
    private SparseArray<View> mViewCache=new SparseArray<View>();
    /**
     * Fragment标识
     */
    private String tagKey;

    /**
     * @param  {@linkplain #tagKey} 属性值
     * @return tagKey
     */
    public String getTagKey() {
        if (TextUtils.isEmpty(tagKey)) {
            return this.getClass().getName();
        }
        return tagKey;
    }
    public BaseActivity mActivity;
    /**
     * 设置{@linkplain #tagKey}属性值
     * @param tagKey
     */
    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof BaseActivity){
            mActivity=(BaseActivity)activity;
        }
        mContext =activity;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if(activity instanceof BaseActivity){
            mActivity=(BaseActivity)activity;
        }
        mContext =activity;
    }
    /**
     * 根据ID获取控件
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T $(int id){
        View cacheView=mViewCache.get(id);
        if(cacheView!=null){//从缓存中获取，减少findViewById的次数
            return (T)cacheView;
        }
        //从xml中查找
        View view=mainView.findViewById(id);
        if(view!=null){//查找到
            mViewCache.put(id, view);
        }
        return (T) view;
    }
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

    public void GONE(View view){
        if(view!=null)
            view.setVisibility(View.GONE);
    }
    public void VISIBLE(View view){
        if(view!=null)
            view.setVisibility(View.VISIBLE);
    }
    public TitleBar addActionBar(String title){
        tintTheme();
        return new TitleBar($(R.id.title_bar_layout),mViewCache).show(title);
    }
    public void tintTheme(){
        if (mainView != null) {
            ThemeCore.setThemeColor(mainView,0);
        }
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
    /**
     * 开发模式
     */
    public void devModel(){

    }
}
