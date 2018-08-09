package com.porster.gift.widget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.porster.gift.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerViewAdapter基类<p></p>
 * 如果要使用getItemViewType,重写对应方法即可
 * @param <T>
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder>{
    private List<T> mList;

    public Context mContext;

    public List<T> getList() {
        return mList;
    }

    public Context getContext() {
        return mContext;
    }

    private OnListItemPartClickListener mItemClick;

    public void setItemClick(OnListItemPartClickListener itemClick) {
        mItemClick = itemClick;
    }


    public  BaseAdapter(Context mContext){
        this.mContext=mContext;
        mList=new ArrayList<T>();
    }
    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    };
    public void addItems(List<T> mList){
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }


    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(getLayoutId(),parent,false);

        return new BaseHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        T bean=this.mList.get(position);
        bindView(holder.getView(),position,bean);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public abstract int getLayoutId();

    public abstract void bindView(View view,int position,T bean);


    protected TextView setText(View view, int id, String content){
        TextView tv= ViewHolder.get(view,id);
        tv.setText(content);
        return tv;
    }
    /**
     * 设置点击事件,可以调用setOnItemPartClick来实现监听
     * @param view	控件
     * @param obj	对象
     * @param state	可变整形数据
     */
    protected void setOnClick(View view,final Object obj,final int state) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClick!=null){
                    mItemClick.onListItemPartClick(v, obj, state);
                }
            }
        });
    }
}
