package com.porster.gift.widget;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.porster.gift.R;
import com.porster.gift.utils.ViewUtil;


/**
*   标题栏
*   @author Porster
*   @time   16/11/10 14:09
**/
public class TitleBar {

    private TextView mTitle;

    private TextView mLeft;

    private TextView mRight;

    private ImageButton mRightBtn;

    private ImageButton mRightBtnExtra;

    private View title_bar_layout;

    public TextView getTitle() {
        return mTitle;
    }

    public TextView getLeft() {
        return mLeft;
    }

    public TextView getRight() {
        return getRight("");
    }
    public TextView getRight(String text) {
        mRight.setText(text);
        mRight.setVisibility(View.VISIBLE);
        addClickEffect(mRight);
        return mRight;
    }

    public ImageButton getRightBtn() {
        mRightBtn.setVisibility(View.VISIBLE);
        return mRightBtn;
    }

    public ImageButton getRightBtnExtra() {
        mRightBtnExtra.setVisibility(View.VISIBLE);
        return mRightBtnExtra;
    }

    public View getTitle_bar_layout() {
        return title_bar_layout;
    }

    private SparseArray<View> mViewCache;

    public TitleBar(View title_bar_layout){
        this(title_bar_layout,null);
    }
    public TitleBar(View title_bar_layout,SparseArray<View> mViewCache){
        this.title_bar_layout=title_bar_layout;
        this.mViewCache=mViewCache;
        init();
    }
    private void init(){
        mTitle=$(R.id.title_bar_title);
        mLeft=$(R.id.title_bar_left_txt);
        mRight=$(R.id.title_bar_right_txt);
        mRightBtn=$(R.id.title_bar_right_img);
        mRightBtnExtra=$(R.id.title_bar_right_img_extra);
    }

    public TitleBar show(String title){
        return show(true,R.drawable.back,title);
    }
    public TitleBar show(boolean mNeedBack,int leftResId,String title){
        if(mNeedBack){
            mLeft.setVisibility(View.VISIBLE);
            addClickEffect(mLeft);
            mLeft.setOnClickListener(mClick);
            ViewUtil.setDrawableToText(mLeft.getContext(),leftResId,mLeft);
        }
        mTitle.setText(title);
        return this;
    }
    private View.OnClickListener mClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ((Activity)v.getContext()).onBackPressed();
        }
    };

    /**
     * 根据ID获取控件
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    private  <T extends View> T $(int id) {
        if(mViewCache==null){
            return (T) title_bar_layout.findViewById(id);
        }
        View cacheView = mViewCache.get(id);
        if (cacheView != null) {// 从缓存中获取，减少findViewById的次数
            return (T) cacheView;
        }
        // 从xml中查找
        View view = title_bar_layout.findViewById(id);
        if (view != null) {// 查找到
            mViewCache.put(id, view);
        }
        return (T) view;
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

    public void setRightImage(int resID) {
        mRightBtn.setImageResource(resID);
        mRightBtn.setVisibility(View.VISIBLE);
    }
}
