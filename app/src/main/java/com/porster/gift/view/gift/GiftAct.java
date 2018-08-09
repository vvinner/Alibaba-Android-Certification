package com.porster.gift.view.gift;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.porster.gift.R;
import com.porster.gift.core.DataManager;
import com.porster.gift.core.async.XAsyncTask;
import com.porster.gift.core.async.XAsyncTaskListenerCompat;
import com.porster.gift.model.AnswerModel;
import com.porster.gift.model.QuestionModel;
import com.porster.gift.utils.LogCat;
import com.porster.gift.utils.Utils;
import com.porster.gift.view.FailFragment;
import com.porster.gift.view.dev.DevToolsAct;
import com.porster.gift.view.study.QuestionFragment;
import com.porster.gift.view.study.StudyAct;
import com.porster.gift.widget.XDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 模拟测试
 * Created by Porster on 17/5/26.
 */

public class GiftAct extends StudyAct implements View.OnClickListener{
    /**认证题目数量*/
    public static final int TOTAL_COUNT=40;

    /**答题时间*/
    int mCurrentTime;


    @Override
    public void initUI() {
        super.initUI();
        GONE($(R.id.study_total_record_layout));
        VISIBLE($(R.id.g_auth_submit,this));
        isNeedScroll=true;
        viewpager.setOnTouchListener(null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadFaild();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()==R.id.g_auth_submit) {

            int rightCount=0;

            for (int i = 0; i < mGiftModels.size(); i++) {
                QuestionModel questionModel=mGiftModels.get(i);

                boolean hasChoose=false;
                for (AnswerModel answerModel : questionModel.choose) {
                    if(answerModel.isSelected){
                        hasChoose=true;
                        break;
                    }
                }
                if(!hasChoose){
                    showToast("第"+(i+1)+"题未作答");
                    return;
                }
                //统计回答结果
                List<AnswerModel> choose=new ArrayList<>();
                for (AnswerModel answerModel : questionModel.choose) {
                    if(answerModel.isSelected){
                        choose.add(answerModel);
                    }
                }
                boolean resultIsRight;

                if(questionModel.multi){//多选
                    resultIsRight= QuestionFragment.compareResult(choose,questionModel.choose);
                }else{
                    resultIsRight=choose.get(0).result;
                }
                if(resultIsRight){
                    rightCount++;
                }else{
                    mFailModels.add(questionModel);
                }
            }

            XDialog.showRadioDialog(mContext, "你完了成所有题目\n 得分 " + ((int) (rightCount * 100/TOTAL_COUNT)), new XDialog.DialogClickListener() {
                @Override
                public void confirm() {
                    end();
                }

                @Override
                public void cancel() {

                }
            });

        }
    }

    @Override
    public void initData() {
        //抽取40道题目

        mGiftModels= DataManager.getInstance().getGiftModels(TOTAL_COUNT);

        for (QuestionModel giftModel : mGiftModels) {
            LogCat.i(giftModel.toString());
        }

        mPresenter.handleQuestion(mGiftModels);
        mTitle.setText(String.format(Locale.getDefault(),"1/%d", mGiftModels.size()));

        startDevModel(DevToolsAct.DEV_AUTO_ANSWER);

        //开始计时
        mHandler.postDelayed(mTimeRun,1000);
    }
    //////////////////////////////////计时器/////////////////////////////////////////
    Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            int time=msg.what;

            //分
            int minute=time/60;
            //秒
            int seconde=time%60;

            setText(R.id.title_bar_right_txt,minute+"分"+seconde+"秒");

            return false;
        }
    });
    Runnable mTimeRun=new Runnable() {
        @Override
        public void run() {

            mCurrentTime++;

            mHandler.sendEmptyMessage(mCurrentTime);

            mHandler.postDelayed(this,1000);
        }
    };
    //////////////////////////////////计时器/////////////////////////////////////////
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void complete(int rightCount, int failCount, TextView total_persent) {
//        super.complete(rightCount, failCount, total_persent);
    }


    @Override
    public void onBackPressed() {
        XDialog.showSelectDialog(mContext, "是否放弃", new XDialog.DialogClickListener() {
            @Override
            public void confirm() {
                end();
            }
            @Override
            public void cancel() {

            }
        });
    }
    void end(){

        XAsyncTask.execute(mContext, new XAsyncTaskListenerCompat<Object, Object>() {
            @Override
            public void onPreExecute(Context context) {
                showProgressDialog("");
            }

            @Override
            public Object doInBackground(Context context, Object... objects) {
                if(!Utils.isEmpty(mFailModels)){
                    DataManager.getInstance().saveList(mContext, FailFragment.CACHE_FAIL_DATA,new ArrayList<>(mFailModels));
                }
                return null;
            }

            @Override
            public void onPostExecute(Context context, Object o) {
                dismissProgressDialog();
                finish();
            }
        });
    }
}