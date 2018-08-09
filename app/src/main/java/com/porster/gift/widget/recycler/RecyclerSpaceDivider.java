package com.porster.gift.widget.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * 间距
 * Created by Porster on 16/11/23.
 */

public class RecyclerSpaceDivider extends RecyclerView.ItemDecoration{

    private int space;

    private int mLineCount;

    public RecyclerSpaceDivider(int space,int mLineCount) {
        this.space = space;
        this.mLineCount=mLineCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int mCount=state.getItemCount();
        int position=parent.getChildAdapterPosition(view);
        //Y
        outRect.top = space;
        int lastLineCount = mCount%mLineCount == 0 ? mLineCount:  mCount%mLineCount;
        if(position>=mCount - (lastLineCount==mLineCount? mLineCount : lastLineCount)){//最底部一排
            outRect.bottom = space;
        }
        //X
        if(position%mLineCount==0){//左侧
            outRect.left=space;
            outRect.right=space;
        }else{//右侧
            outRect.right=space;
        }
    }
}
