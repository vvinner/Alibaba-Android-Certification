package com.porster.gift.view.dev;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.porster.gift.R;
import com.porster.gift.core.BaseActivity;

/**
 * 开发模式
 * Created by Porster on 17/4/27.
 */

public class DevToolsAct extends BaseActivity{
    public static  boolean DEV_AUTO_ANSWER;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dev_tools);

        addActionBar("开发模式");

        CheckBox dev_auto_answer=$(R.id.dev_auto_answer);
        dev_auto_answer.setChecked(DEV_AUTO_ANSWER);
        dev_auto_answer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DEV_AUTO_ANSWER=isChecked;
            }
        });
    }
}
