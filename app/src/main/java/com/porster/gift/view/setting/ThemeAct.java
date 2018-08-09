package com.porster.gift.view.setting;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;

import com.porster.gift.R;
import com.porster.gift.core.BaseRecyclerActivity;
import com.porster.gift.core.ThemeCore;
import com.porster.gift.utils.ViewHolder;
import com.porster.gift.utils.ViewUtil;
import com.porster.gift.widget.CircleView;
import com.porster.gift.widget.pulltorefresh.PullToRefreshBase;
import com.porster.gift.widget.recycler.BaseAdapter;
import com.porster.gift.widget.recycler.OnListItemPartClickListener;
import com.porster.gift.widget.recycler.RecyclerSpaceDivider;

import java.util.ArrayList;

/**
 * 主题颜色设置
 * Created by Porster on 17/5/8.
 */

public class ThemeAct extends BaseRecyclerActivity<Integer>{
    public static final int REQ_THEME=666;
    private boolean mDataChanged;
    @ColorInt
    int[] colors={0xFFFD4831,0xFFE62565,0xFF9B2FAE,0xFF673FB4,0xFF4054B2,0xFF2A96ED,
        0xFF1EA9F0,0xFF1FBBD0,0xFF159588,0xFF50AE55,0xFFCCD949,0xFFFEE94E,
        0xFFFDC02F,0xFFFA9527,0xFFF9572F,0xFF775448,0xFF9C9C9C,0xFF5E7985};

    @Override
    public int getLayoutId() {
        return R.layout.act_theme;
    }

    @Override
    protected void initUI() {
        addActionBar("主题色");
        getRecyclerView().getRefreshableView().addItemDecoration(new RecyclerSpaceDivider(ViewUtil.dip2px(mContext,5),6));
        getAdapter().setItemClick(new OnListItemPartClickListener() {
            @Override
            public void onListItemPartClick(View view, Object obj, int state) {
                mDataChanged=true;
                ThemeCore.saveThemeColor(ThemeAct.this, (Integer) obj);
                initSeekBar((Integer) obj);
            }
        });

        getRecyclerView().setMode(PullToRefreshBase.Mode.DISABLED);

        ArrayList<Integer> list=new ArrayList<>();
        for (int color : colors) {
            list.add(color);
        }
        getAdapter().addItems(list);


        initSeekBar(ThemeCore.getThemeColor(this));

    }
    private void initSeekBar(int themeColor){
        r   = (themeColor & 0x00ff0000) >> 16;
        g = (themeColor & 0x0000ff00) >> 8;
        b  = (themeColor & 0x000000ff);
        drawSeekBar((SeekBar) $(R.id.seekBar_r),r);
        drawSeekBar((SeekBar) $(R.id.seekBar_g),g);
        drawSeekBar((SeekBar) $(R.id.seekBar_b),b);
    }
    private int r;
    private int g;
    private int b;
    private void drawSeekBar(SeekBar seekBar, final int position){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()) {
                    case R.id.seekBar_r:
                        setText(R.id.seekBar_r_count,progress+"");
                        r=progress;
                        break;
                    case R.id.seekBar_g:
                        g=progress;
                        setText(R.id.seekBar_g_count,progress+"");
                        break;
                    case R.id.seekBar_b:
                        b=progress;
                        setText(R.id.seekBar_b_count,progress+"");
                        break;
                }

                if(!fromUser){
                    return;
                }
                int themeColor=Color.argb(255,r,g,b);
                mDataChanged=true;
                ThemeCore.saveThemeColor(ThemeAct.this, themeColor);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(position);
    }

    @Override
    protected boolean autoRefresh() {
        return false;
    }

    @Override
    public void finish() {
        if(mDataChanged){
            setResult(RESULT_OK);
        }
        super.finish();

    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManager() {
        return new GridLayoutManager(mContext,6);
    }

    @Override
    protected BaseAdapter<Integer> initAdapter() {
        final int wh;
        wh=(ViewUtil.getScreenWidth(mContext)-ViewUtil.dip2px(mContext,10)*7)/6;
        return new BaseAdapter<Integer>(mContext) {
            @Override
            public int getLayoutId() {
                return R.layout.act_theme_item;
            }

            @Override
            public void bindView(View view, int position, Integer bean) {
                CircleView circle= ViewHolder.get(view,R.id.circle);
                circle.setColor(bean);

                circle.getLayoutParams().height=wh;
                circle.getLayoutParams().width=wh;

                setOnClick(addClickEffect(circle),bean,position);
            }
        };
    }

    @Override
    public void onListItemPartClick(View view, Object obj, int state) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}
