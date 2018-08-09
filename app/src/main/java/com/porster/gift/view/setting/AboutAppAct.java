package com.porster.gift.view.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.porster.gift.R;
import com.porster.gift.core.BaseActivity;

/**
 * 关于软件
 * Created by Porster on 17/5/3.
 */

public class AboutAppAct extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about_app);
        addActionBar("关于软件");

    }
}
