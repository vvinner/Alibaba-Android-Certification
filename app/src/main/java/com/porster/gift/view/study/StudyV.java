package com.porster.gift.view.study;

import com.porster.gift.core.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Porster on 17/3/31.
 */

public interface StudyV {


    /**
     * 显示问题Fragment
     */
    void showQuestionFragment(ArrayList<BaseFragment> baseFragments);

    void gotoLastQuestion(int index);
    /**
     * 切换广告
     * @param show 是否显示
     */
    void switchAdvert(boolean show);
}
