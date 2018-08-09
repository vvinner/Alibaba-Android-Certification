package com.porster.gift.view.study;

import android.content.Context;

import com.porster.gift.core.BaseFragment;
import com.porster.gift.model.QuestionModel;

import java.util.ArrayList;

/**
 * Created by Porster on 17/3/31.
 */

public class StudyPresenter implements StudyP{

    private StudyM mM;
    private StudyV mV;
    private Context mCtx;

    public StudyPresenter(Context mCtx,StudyV mV){
        this.mCtx=mCtx;
        this.mV=mV;
        mM=new StudyImpl(this);
    }

    @Override
    public void handleQuestion(ArrayList<QuestionModel> models) {
        ArrayList<BaseFragment> fragments=mM.createQuestions(models);
        mV.showQuestionFragment(fragments);
    }

    @Override
    public void gotoLastQuestion() {
        int index=mM.gotoLastQuestion(mCtx);
        mV.gotoLastQuestion(index);
    }

    @Override
    public void cehckNeedShowAdvert() {
        boolean needShow=mM.showAdvert();
        mV.switchAdvert(needShow);
    }
}
