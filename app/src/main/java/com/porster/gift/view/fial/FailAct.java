package com.porster.gift.view.fial;

import android.content.Context;
import android.text.TextUtils;

import com.porster.gift.R;
import com.porster.gift.core.DataManager;
import com.porster.gift.core.SessionData;
import com.porster.gift.core.async.XAsyncTask;
import com.porster.gift.core.async.XAsyncTaskListenerCompat;
import com.porster.gift.model.AnswerModel;
import com.porster.gift.model.QuestionModel;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.utils.LogCat;
import com.porster.gift.utils.Utils;
import com.porster.gift.view.FailFragment;
import com.porster.gift.view.setting.HighSetAct;
import com.porster.gift.view.study.StudyAct;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 错题
 * Created by Porster on 17/5/26.
 */

public class FailAct extends StudyAct{

    /**答错次数*/
    int fail_count;
    /**待移除队列*/
    private List<QuestionModel> mPrepareRemove=new ArrayList<>();


    @Override
    public void initUI() {
        super.initUI();

        fail_count= (int) SessionData.getObject(mContext, HighSetAct.SP_REMOVE_COUNT,2);

        GONE($(R.id.study_total_record_layout));

        GONE($(R.id.title_bar_right_txt));

        VISIBLE(setText(R.id.fail_count_tips,"答对"+fail_count+"次后，将移除错题，次数可以在高级设置中设定"));
    }

    @Override
    public void initData() {
        mGiftModels= (ArrayList<QuestionModel>) getIntent().getSerializableExtra(IntentHelper.KEY1);
        for (QuestionModel giftModel : mGiftModels) {
            giftModel.hasAnswer=false;
            if(giftModel.errorCount==0){//有错误次数的不再重新计数
                giftModel.errorCount=fail_count;
            }
            giftModel.resultIsRight =false;
            if(!Utils.isEmpty(giftModel.choose)){
                for (AnswerModel answerModel : giftModel.choose) {
                    answerModel.isRightAnswer=false;
                    answerModel.isSelected=false;
                }
            }
        }

        mPresenter.handleQuestion(mGiftModels);
        mTitle.setText(MessageFormat.format("1/{0}", mGiftModels.size()));
    }



    @Override
    public void updata(QuestionModel model) {
        //答对一次，则errorCount-1
        if(model.resultIsRight){
            model.errorCount--;
            if(model.errorCount==0){
                //移除错题本
                mPrepareRemove.add(model);
            }
        }
    }

    @Override
    public void onBackPressed() {
        for (QuestionModel giftModel : mGiftModels) {
            LogCat.i(giftModel.errorCount+" "+giftModel);
        }
        XAsyncTask.execute(mContext, new XAsyncTaskListenerCompat<Object, Object>() {
            @Override
            public Object doInBackground(Context context, Object... objects) {
                LogCat.w("准备移除"+mPrepareRemove.size()+"道 ，总:"+mGiftModels.size()+"道");
                if(mPrepareRemove.size()>0){//移除已经回答的问题
                    for (int i = mGiftModels.size() - 1; i >= 0; i--) {

                        QuestionModel mNow=mGiftModels.get(i);

                        for (QuestionModel giftModel : mPrepareRemove) {
                            //找到移除
                            if(TextUtils.equals(mNow.id,giftModel.id)){
                                mGiftModels.remove(i);
                            }
                        }
                    }
                }
                LogCat.w("移除结束，剩余"+mGiftModels.size()+"道");

                DataManager.getInstance().saveList(mContext, FailFragment.CACHE_FAIL_DATA,mGiftModels);
                return null;
            }

            @Override
            public void onPostExecute(Context context, Object o) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
