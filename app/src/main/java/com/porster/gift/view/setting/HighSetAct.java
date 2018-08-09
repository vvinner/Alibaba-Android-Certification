package com.porster.gift.view.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import com.porster.gift.R;
import com.porster.gift.core.BaseActivity;
import com.porster.gift.core.SessionData;

/**
 * 高级设置
 * Created by Porster on 17/5/25.
 */

public class HighSetAct extends BaseActivity{
    /**错题，答对几次移除*/
    public static final String SP_REMOVE_COUNT="SP_REMOVE_COUNT";

    private int errorCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_high_set);
        addActionBar("高级设置");
        initUI();
    }

    private void initUI() {

        SeekBar seekBar_failed=$(R.id.seekBar_failed);
        seekBar_failed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress+=1;
                errorCount=progress;
                setText(R.id.seekBar_failed_txt,progress+"次");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int failCount=(Integer) SessionData.getObject(mContext,SP_REMOVE_COUNT,2);
        seekBar_failed.setProgress(failCount-1);
    }

    @Override
    protected void onDestroy() {
        SessionData.setObject(mContext,SP_REMOVE_COUNT,errorCount);
        super.onDestroy();
    }
}
