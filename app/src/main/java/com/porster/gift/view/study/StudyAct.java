package com.porster.gift.view.study;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.porster.gift.R;
import com.porster.gift.core.BaseActivity;
import com.porster.gift.core.BaseFragment;
import com.porster.gift.core.DataManager;
import com.porster.gift.core.SessionData;
import com.porster.gift.core.async.XAsyncTask;
import com.porster.gift.core.async.XAsyncTaskListenerCompat;
import com.porster.gift.model.QuestionModel;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.utils.LogCat;
import com.porster.gift.utils.Utils;
import com.porster.gift.view.FailFragment;
import com.porster.gift.view.StudyFragment;
import com.porster.gift.view.dev.DevToolsAct;
import com.porster.gift.widget.TitleBar;
import com.porster.gift.widget.XDialog;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * 练习
 * 按照题目数量创建对应个数的Fragment
 * 0.5%概率底部出现广告
 * Created by Porster on 17/3/8.
 */

public class StudyAct extends BaseActivity implements View.OnClickListener,StudyV{
    public TextView mTitle;
    public ViewPager viewpager;
    public ArrayList<QuestionModel> mGiftModels;
    public ArrayList<BaseFragment> mFragments;

    /**答错的题目*/
    public LinkedHashSet<QuestionModel> mFailModels;

    public StudyPresenter mPresenter;

    /**是否允许滑动*/
    public boolean isNeedScroll;

    /**是否做过了题目*/
    public boolean mDataChanged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_study);
        initUI();
        initData();
    }

    public void initUI() {
        mPresenter=new StudyPresenter(mContext,this);

        TitleBar mTitleBar=addActionBar("");
        mTitle=mTitleBar.getTitle();
        mTitleBar.getRight("跳页").setOnClickListener(this);
        mTitleBar.getLeft().setOnClickListener(this);

        $(R.id.study_auto_answer,this);

        //创建ViewPager
        viewpager=$(R.id.viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                mPresenter.cehckNeedShowAdvert();
                mTitle.setText(MessageFormat.format("{0}/{1}", position + 1, mGiftModels.size()));

                //检查该页面是否可以滑到下一个页面：当前界面的hasAnswer=true
                checkHasScrollToNextPage(position);

                if(DevToolsAct.DEV_AUTO_ANSWER){
                    viewpager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            devModel();
                        }
                    },200);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            //控制是否能滑动到下一页
            private float downX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isNeedScroll){
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX=event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(event.getX()>downX){
                            downX=event.getX();
                        }
                        if(event.getX()<downX){
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }
    public void initData(){
        showProgressDialog("");

        DataManager.getInstance().readListAsync(mContext, StudyFragment.CACHE_HISTORY_STUDY, new DataManager.OnReadListener() {
            @Override
            public void onSuccess(Object mlist) {

                ArrayList<QuestionModel> list= (ArrayList<QuestionModel>) mlist;

                dismissProgressDialog();
                boolean hasHistory=!Utils.isEmpty(list);
                if(!hasHistory){
                    LogCat.i("未找到历史答题记录,重新创建");
                    mGiftModels=new ArrayList<>();

                    for (QuestionModel giftModel : DataManager.getInstance().getGiftModels()) {
                        QuestionModel bean= null;
                        try {
                            bean = (QuestionModel) giftModel.clone();
                            mGiftModels.add(bean);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    //打乱题目顺序
                    Collections.shuffle(mGiftModels);

                }else{
                    mGiftModels=list;
                }

                mPresenter.handleQuestion(mGiftModels);
                //跳转到上次页面
                if(hasHistory){
                    mPresenter.gotoLastQuestion();
                }else{
                    mTitle.setText("1/"+mGiftModels.size());
                }
                //计算答题统计
                updata(null);

                startDevModel(DevToolsAct.DEV_AUTO_ANSWER);

                loadFaild();

            }
        });
    }
    protected void loadFaild(){
        //加载错题
        ArrayList<QuestionModel> mLocFailModels = null;
        try {
            mLocFailModels= (ArrayList<QuestionModel>) DataManager.getInstance().readList(mContext, FailFragment.CACHE_FAIL_DATA);
        } catch (Exception e) {
//                    e.printStackTrace();
        }
        if(mLocFailModels==null){
            mFailModels=new LinkedHashSet<>();
        }else{
            mFailModels=new LinkedHashSet<QuestionModel>(mLocFailModels);
        }
        LogCat.i("错题记录="+mFailModels.size()+"条");
    }


    /**
     * 检查该页面是否可以滑到下一个页面：当前界面的hasAnswer=true
     * @param position  当前页面
     */
    public void checkHasScrollToNextPage(Integer position){
        if(position==null){
            position=viewpager.getCurrentItem();
        }
        if(position<mGiftModels.size()){
            position=Math.max(position,0);
            isNeedScroll = mGiftModels.get(position).hasAnswer;
            LogCat.i(mGiftModels.get(position).toString());
            LogCat.i("第"+(position+1)+"已经回答过可以滑动"+isNeedScroll);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.study_auto_answer:
                Button study_auto_answer= (Button) v;
                if(study_auto_answer.getText().toString().contains("停止")){
                    mAutoAnswer=false;
                    study_auto_answer.setText("开始自动答题");
                }else{
                    mAutoAnswer=true;
                    study_auto_answer.setText("停止自动答题");
                    startDevModel(DevToolsAct.DEV_AUTO_ANSWER);
                }
                break;
            case R.id.title_bar_left_txt:
                onBackPressed();
                break;
            case R.id.title_bar_right_txt:
                SkipPageFragment skipPageFragment=new SkipPageFragment();
                Bundle k=new Bundle();
                k.putInt(IntentHelper.KEY1,viewpager.getCurrentItem()+1);

                //当前已答题的进度
                int mNowAnswerCount = 0;
                for (int i = 0; i < mGiftModels.size(); i++) {
                    QuestionModel bean=mGiftModels.get(i);
                    if(!bean.hasAnswer){//发现答过的题,则停止
                        mNowAnswerCount=i+1;
                        break;
                    }
                }
//                mNowAnswerCount=1163;
                k.putInt(IntentHelper.KEY2,mNowAnswerCount);
                skipPageFragment.setArguments(k);
                skipPageFragment.show(getFragmentManager(),"");
                break;
        }
    }

    @Override
    public void showQuestionFragment(final ArrayList<BaseFragment> baseFragments) {
        this.mFragments=baseFragments;
        viewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return baseFragments.get(position);
            }

            @Override
            public int getCount() {
                return baseFragments.size();
            }
        });

    }

    @Override
    public void gotoLastQuestion(int index) {
        viewpager.setCurrentItem(index);
        mTitle.setText(MessageFormat.format("{0}/{1}", index + 1, mGiftModels.size()));
        checkHasScrollToNextPage(index);
    }
    @Override
    public void switchAdvert(boolean show) {
    }

    /**
     * 更新答题统计
     */
    public void updata(QuestionModel model){
        TextView total_ok=$(R.id.total_ok);
        TextView total_no=$(R.id.total_no);
        TextView total_persent=$(R.id.total_persent);

        int rightCount=0;
        int failCount=0;
        if (model != null) {
            mDataChanged=true;
            try {
                rightCount=Integer.parseInt(total_ok.getText().toString());
                failCount=Integer.parseInt(total_no.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            Boolean answerResult=model.resultIsRight;
            if(answerResult){
                rightCount++;
            }else{
                failCount++;
            }
            //计入错题本
            if(!model.resultIsRight){
                mFailModels.add(model);
            }
        }else{
            //计算出答题统计
            for (QuestionModel giftModel : mGiftModels) {
                if(giftModel.hasAnswer){
                    if(giftModel.resultIsRight){
                        rightCount++;
                    }else{
                        failCount++;
                    }
                }else{
                    break;
                }
            }
        }
        if((rightCount+failCount)==0){
            total_persent.setText("0%");
        }else{
            float persent=rightCount*1.0f/(rightCount+failCount)*100f;
            String persentStr=(persent+"");
            int index=persentStr.indexOf(".");
            if(index!=-1){
                persentStr=persentStr.substring(0,index);
            }
            total_persent.setText(MessageFormat.format("{0}%", persentStr));
        }
        total_ok.setText(MessageFormat.format("{0}", rightCount));
        total_no.setText(MessageFormat.format("{0}", failCount));

        complete(rightCount,failCount,total_persent);
    }
    public void complete(int rightCount,int failCount,TextView total_persent){
        //答完了
        if(rightCount+failCount==mGiftModels.size()){
            XDialog.showRadioDialog(mContext, "你完了成所有题目\n正确" + rightCount + " 错误" + failCount + "\n正确率" + total_persent.getText().toString(), new XDialog.DialogClickListener() {
                @Override
                public void confirm() {
                    //清空记录
                    onBackClick(0,new ArrayList<QuestionModel>());
                }
                @Override
                public void cancel() {

                }
            });
        }
    }
    public void next(long millis){
        viewpager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewpager.setCurrentItem(viewpager.getCurrentItem()+1,true);
            }
        },millis);
    }
    private void onBackClick(final int nowPage, final ArrayList<QuestionModel> nowRecord){
        if(!mDataChanged){
            finish();
            return;
        }
        XAsyncTask.execute(mContext, new XAsyncTaskListenerCompat<Object, Object>() {
            @Override
            public void onPreExecute(Context context) {

                SessionData.setObject(mContext, StudyFragment.SP_STUDY_LAST_CURRENT,nowPage);
                showProgressDialog(nowPage==0?"":"保存进度...");
            }

            @Override
            public Object doInBackground(Context context, Object... objects) {
                for (QuestionModel failModel : mFailModels) {
                    LogCat.i("错题记录="+failModel);
                }
                DataManager.getInstance().saveList(mContext,FailFragment.CACHE_FAIL_DATA,new ArrayList<>(mFailModels));

                DataManager.getInstance().saveList(mContext,StudyFragment.CACHE_HISTORY_STUDY,nowRecord);
                return null;
            }

            @Override
            public void onPostExecute(Context context, Object o) {
                dismissProgressDialog();
                setResult(RESULT_OK);
                finish();


            }
        });
    }

    @Override
    public void onBackPressed() {
        onBackClick(viewpager.getCurrentItem(),mGiftModels);
    }

    private boolean mAutoAnswer;
    @Override
    public void devModel() {
        if(!mAutoAnswer){
            return;
        }
        QuestionFragment fragment= (QuestionFragment) mFragments.get(viewpager.getCurrentItem());
        fragment.devModel();
    }

    @Override
    public void startDevModel(boolean needStart) {
        super.startDevModel(needStart);
        if(needStart){
            mAutoAnswer=true;
            VISIBLE($(R.id.study_auto_answer));
        }
    }
}
