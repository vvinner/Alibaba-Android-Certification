package com.porster.gift.view.study;

import android.content.Context;
import android.os.Bundle;

import com.porster.gift.core.BaseFragment;
import com.porster.gift.core.SessionData;
import com.porster.gift.model.QuestionModel;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.view.StudyFragment;

import java.util.ArrayList;

/**
 * Created by Porster on 17/3/31.
 */

public class StudyImpl implements StudyM{

    private StudyP mStudyP;

    public StudyImpl(StudyP mStudyP){
        this.mStudyP=mStudyP;
    }

    @Override
    public ArrayList<BaseFragment> createQuestions(ArrayList<QuestionModel> giftModels) {
        ArrayList<BaseFragment> mFragments=new ArrayList<>();
        for (int i = 0; i < giftModels.size(); i++) {
            QuestionModel model=giftModels.get(i);
            QuestionFragment mQuestionFragment=new QuestionFragment();
            Bundle b=new Bundle();
            b.putSerializable(IntentHelper.KEY1,model);
            mQuestionFragment.setArguments(b);
            mFragments.add(mQuestionFragment);
        }
        return mFragments;
    }

    @Override
    public int gotoLastQuestion(Context mCtx) {
        return (int) SessionData.getObject(mCtx, StudyFragment.SP_STUDY_LAST_CURRENT,0);
    }

    @Override
    public boolean showAdvert() {
        int persent= (int) (Math.random()*1000);
        return persent<=10;
    }

    @Override
    public void hideAdvert() {

    }
}
