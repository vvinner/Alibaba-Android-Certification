package com.porster.gift.view.study;

import android.content.Context;

import com.porster.gift.core.BaseFragment;
import com.porster.gift.model.QuestionModel;

import java.util.ArrayList;

/**
 *
 * Created by Porster on 17/3/31.
 */

public interface StudyM {

    /**
     * 创建题目
     * @param giftModels    题目
     */
    ArrayList<BaseFragment> createQuestions(ArrayList<QuestionModel> giftModels);

    /**
     * 跳转到上一次答题界面
     */
    int gotoLastQuestion(Context mCtx);
    /**
     * 显示广告
     * @return 是否需要显示
     */
    boolean showAdvert();

    /**
     * 隐藏广告
     */
    void hideAdvert();
}