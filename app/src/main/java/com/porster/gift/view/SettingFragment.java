package com.porster.gift.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.porster.gift.R;
import com.porster.gift.core.BaseFragment;
import com.porster.gift.core.ThemeCore;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.utils.LogCat;
import com.porster.gift.utils.Utils;
import com.porster.gift.view.dev.DevToolsAct;
import com.porster.gift.view.setting.AboutAppAct;
import com.porster.gift.view.setting.HighSetAct;
import com.porster.gift.view.setting.ThemeAct;
import com.porster.gift.widget.LightingView;

/**
 * Created by Porster on 17/4/27.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener{

    LightingView view;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        mainView=inflater.inflate(R.layout.fragment_setting,container,false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view=$(R.id.set_lighting);
        addActionBar("设置").getLeft().setVisibility(View.GONE);;

        $(R.id.title_bar_title,this);

        $(R.id.set_high_setting,this);
        $(R.id.set_about,this);
        $(R.id.set_theme,this);
        $(R.id.set_feedback,this);
        $(R.id.check_update,this);

        view.post(new Runnable() {
            @Override
            public void run() {
                view.startLighting();
            }
        });

        setText(R.id.set_version, "v"+Utils.getVersionName(mContext));
    }

    int dev_model=0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_update://检查更新
                mActivity.showToast("没有这个功能");
                break;
            case R.id.set_feedback://意见反馈
                mActivity.showToast("没有这个功能");
                break;
            case R.id.title_bar_title:
                dev_model++;
                LogCat.d("dev_model="+dev_model);
                if(dev_model>5){
                    IntentHelper.openClass(mContext, DevToolsAct.class);
                }
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       dev_model=0;
                    }
                },1000);
                break;
            case R.id.set_high_setting:
                IntentHelper.openClass(mContext, HighSetAct.class);
                break;
            case R.id.set_about:
                IntentHelper.openClass(mContext, AboutAppAct.class);
                break;
            case R.id.set_theme:
                IntentHelper.openClassResult(mContext, ThemeAct.class,ThemeAct.REQ_THEME);
                break;
        }
    }

    @Override
    public void tintTheme() {
        super.tintTheme();
        if (mainView != null) {
            view.setColor(ThemeCore.getThemeColor(getActivity()));
        }
    }
}
