package com.porster.gift.view.study;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.porster.gift.R;
import com.porster.gift.core.BaseFragment;
import com.porster.gift.model.AnswerModel;
import com.porster.gift.model.QuestionModel;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.utils.LogCat;
import com.porster.gift.utils.ViewHolder;
import com.porster.gift.view.dev.DevToolsAct;
import com.porster.gift.view.gift.GiftAct;
import com.porster.gift.widget.recycler.BaseAdapter;
import com.porster.gift.widget.recycler.OnListItemPartClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Porster on 17/3/31.
 */

public class QuestionFragment extends BaseFragment implements View.OnClickListener{
    private RecyclerView mRecycler;
    private SelectAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(mainView==null){
            mainView=inflater.inflate(R.layout.fragment_question,container,false);

            mRecycler=$(R.id.q_select);
            mRecycler.setLayoutManager(new LinearLayoutManager(mContext));

            mAdapter=new SelectAdapter(mContext);
            mRecycler.setAdapter(mAdapter);

            mGiftModel= (QuestionModel) getArguments().getSerializable(IntentHelper.KEY1);

            initUI();
        }
        return mainView;
    }

    @Override
    public void onDestroyView() {
        if(mainView!=null){
            ((ViewGroup)mainView.getParent()).removeView(mainView);
        }
        super.onDestroyView();
    }

    private QuestionModel mGiftModel;

    public void initUI() {

        setText(R.id.q_title,mGiftModel.id+"、"+(mGiftModel.multi?"(多选)":"(单选)")+mGiftModel.title);

        $(R.id.g_multi_submit).setOnClickListener(this);

        $(R.id.g_multi_submit).setVisibility(mGiftModel.hasAnswer?View.GONE:View.VISIBLE);

        if(getActivity() instanceof GiftAct){//认证模式不显示提交按钮
            GONE($(R.id.g_multi_submit));
        }

        final List<AnswerModel> models=mGiftModel.choose;
        Collections.shuffle(models);

        mAdapter.clear();
        mAdapter.addItems(models);

        mAdapter.setItemClick(new OnListItemPartClickListener() {
            @Override
            public void onListItemPartClick(View view, Object obj, int state) {
                AnswerModel bean= (AnswerModel) obj;


                bean.isSelected=!bean.isSelected;

                if(!mGiftModel.multi){//单选

                    for (AnswerModel answerModel : getAdapter().getList()) {
                        if(answerModel==bean){
                            continue;
                        }
                        answerModel.isSelected=false;
                    }
                    mAdapter.notifyItemRangeChanged(0,getAdapter().getItemCount());
                }else{

                    mAdapter.notifyItemChanged(state);
                }


            }
        });

    }
    public SelectAdapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView getRecycler() {
        return mRecycler;
    }

    public QuestionModel getGiftModel() {
        return mGiftModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.g_multi_submit:
                //回答结果
                List<AnswerModel> choose=new ArrayList<>();
                for (AnswerModel answerModel : getAdapter().getList()) {
                    if(answerModel.isSelected){
                        choose.add(answerModel);
                    }
                }
                if(choose.size()==0){
                    mActivity.showToast("请选择");
                    return;
                }

                mGiftModel.hasAnswer =true;

                boolean resultIsRight;

                if(mGiftModel.multi){//多选
                    resultIsRight=compareResult(choose,getAdapter().getList());
                }else{
                    resultIsRight=choose.get(0).result;
                }
                mGiftModel.resultIsRight =resultIsRight;//标记结果

                if(!resultIsRight){//回答错误
                    ((StudyAct) getActivity()).showToast("答案不正确");
                    if(getActivity() instanceof StudyAct){
                        ((StudyAct) getActivity()).checkHasScrollToNextPage(null);
                    }
                }else{//回答正确
                    if(getActivity() instanceof StudyAct){
                        ((StudyAct) getActivity()).next(DevToolsAct.DEV_AUTO_ANSWER?100:500);
                    }
                }
                mAdapter.notifyDataSetChanged();

                if(getActivity() instanceof StudyAct){
                    ((StudyAct) getActivity()).updata(mGiftModel);
                }
                GONE($(R.id.g_multi_submit));

                LogCat.i(mGiftModel.resultIsRight?"回答正确":"回答错误");

                break;
        }
    }

    /**
     * 比较选择结果是否正确
     * @param choose        选择
     * @param questions     问题
     */
    public static boolean compareResult(List<AnswerModel> choose,List<AnswerModel> questions){

        int rightLen=0;
        for (AnswerModel question : questions) {
            if(question.result){
                rightLen++;
            }
        }
        if(choose.size()!=rightLen){//结果与题目正确数量对不上，直接判定回答错误
            return false;
        }
        for (AnswerModel question : questions) {
            if(question.result){

                boolean isFind=false;//结果里不包含正确答案,判定回答错误

                for (AnswerModel chooseAnswer : choose) {
                    if(TextUtils.equals(question.title,chooseAnswer.title)){
                        isFind=true;
                        break;
                    }
                }

                if(!isFind){
                    return false;
                }

            }else{

                boolean isFind=false;//错误的结果里包含了选择的答案，判定回答错误

                for (AnswerModel chooseAnswer : choose) {
                    if(TextUtils.equals(question.title,chooseAnswer.title)){
                        isFind=true;
                        break;
                    }
                }

                if(isFind){
                    return false;
                }
            }
        }


        return true;
    }

    private class SelectAdapter extends BaseAdapter<AnswerModel>{


         SelectAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getLayoutId() {
            return R.layout.fragment_question_item;
        }

        @Override
        public void bindView(View view, int position, AnswerModel bean) {
            setText(view,R.id.s_name,bean.title);
            ImageView s_no= ViewHolder.get(view,R.id.s_no);

            RadioButton s_check=ViewHolder.get(view,R.id.s_check);

            s_check.setChecked(bean.isSelected);
            view.setEnabled(!mGiftModel.hasAnswer);

            if(mGiftModel.hasAnswer){//回答过了，显示答案
                s_no.setImageResource(bean.result?R.drawable.ok:R.drawable.no);
                if(bean.result||bean.isSelected){//仅显示答案以及选择的选择
                    s_no.setVisibility(View.VISIBLE);
                }else{
                    s_no.setVisibility(View.GONE);
                }
            }else{
                s_no.setVisibility(View.GONE);
            }

            setOnClick(s_check,bean,position);
            setOnClick(view,bean,position);
        }
    }

    @Override
    public void devModel() {
        if(DevToolsAct.DEV_AUTO_ANSWER){

            for (int i = 0; i < mGiftModel.choose.size(); i++) {
                AnswerModel bean=mGiftModel.choose.get(i);
                if(bean.result){
                    View v=mRecycler.getChildAt(i);
                    v.performClick();
                }
            }
            ((StudyAct) getActivity()).next(DevToolsAct.DEV_AUTO_ANSWER?100:500);
        }
    }
}
