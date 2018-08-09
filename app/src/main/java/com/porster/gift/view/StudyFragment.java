package com.porster.gift.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.porster.gift.R;
import com.porster.gift.core.BaseFragment;
import com.porster.gift.core.DataManager;
import com.porster.gift.core.SessionData;
import com.porster.gift.model.QuestionModel;
import com.porster.gift.utils.AppConstants;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.view.study.StudyAct;
import com.porster.gift.widget.XDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 练习
 * Created by Porster on 17/3/8.
 */

public class StudyFragment extends BaseFragment implements View.OnClickListener{
    public static final String CACHE_HISTORY_STUDY="CACHE_HISTORY_STUDY";
    public static final String SP_STUDY_LAST_CURRENT="SP_STUDY_LAST_CURRENT";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mainView=inflater.inflate(R.layout.fragment_study,container,false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
    }

    private void initUI() {
        addActionBar("练习").getLeft().setVisibility(View.GONE);;
        $(R.id.t_start,this).setEnabled(false);
        $(R.id.study_delete,this);
        loadData();
    }
    private void loadData(){
//        ((ContentLoadingProgressBar)$(R.id.study_load)).show();
        VISIBLE($(R.id.study_load));
        DataManager.getInstance().readListAsync(mContext, CACHE_HISTORY_STUDY, new DataManager.OnReadListener() {
            @Override
            public void onSuccess(Object mObj) {

                ArrayList<QuestionModel> list= (ArrayList<QuestionModel>) mObj;
                ((ContentLoadingProgressBar)$(R.id.study_load)).hide();
                $(R.id.t_start).setEnabled(true);

                if(list!=null&&list.size()>0){

                    int current= (int) SessionData.getObject(mContext,SP_STUDY_LAST_CURRENT,0);

                    setText(R.id.study_start_tip,current==0?"开始练习":"上次做到第"+current+"题");

                    VISIBLE($(R.id.study_delete));
                }else{
                    GONE($(R.id.study_delete));

                    setText(R.id.study_start_tip,"");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t_start:
                MobclickAgent.onEvent(mContext,"GiftStudy");
                Bundle k=new Bundle();
                IntentHelper.openClassResult(mContext,StudyAct.class, AppConstants.REQUEST_CODE);
                break;
            case R.id.study_delete:
                XDialog.showSelectDialog(mContext, "是否清空做题记录", new XDialog.DialogClickListener() {
                    @Override
                    public void confirm() {
                        DataManager.getInstance().saveListAsync(mContext,CACHE_HISTORY_STUDY,new ArrayList<QuestionModel>());
                    }
                    @Override
                    public void cancel() {

                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            int current= (int) SessionData.getObject(mContext,SP_STUDY_LAST_CURRENT,0);
            setText(R.id.study_start_tip,current==0?"开始练习":"上次做到第"+current+"题");
            if(current>0){
                VISIBLE($(R.id.study_delete));
            }else{
                GONE($(R.id.study_delete));
            }
        }
    }
}
