package com.porster.gift.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.porster.gift.R;
import com.porster.gift.core.BaseFragment;
import com.porster.gift.core.DataManager;
import com.porster.gift.core.async.XAsyncTask;
import com.porster.gift.core.async.XAsyncTaskListenerCompat;
import com.porster.gift.model.QuestionModel;
import com.porster.gift.utils.AppConstants;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.utils.LogCat;
import com.porster.gift.view.fial.FailAct;
import com.porster.gift.widget.XDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 错题
 * Created by Porster on 17/3/8.
 */

public class FailFragment extends BaseFragment implements View.OnClickListener{

    public static final String CACHE_FAIL_DATA="CACHE_FAIL_DATA";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mainView=inflater.inflate(R.layout.fragment_failed,container,false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
    }

    private void initUI() {
        addActionBar("错题").getLeft().setVisibility(View.GONE);;
        $(R.id.f_start,this).setEnabled(false);
        $(R.id.fail_delete,this);
        loadData();
    }
    private ArrayList<QuestionModel> list;

    private void loadData(){
        showEmptyView("加载中...",R.drawable.no_data,true,null);
        DataManager.getInstance().readListAsync(mContext, CACHE_FAIL_DATA, new DataManager.OnReadListener() {
            @Override
            public void onSuccess(Object mObj) {

                list= (ArrayList<QuestionModel>) mObj;

                $(R.id.f_start).setEnabled(true);

                if(list!=null&&list.size()>0){
                    GONE($(R.id.empty_layout));

                    setText(R.id.fail_start_tip,"剩余"+list.size()+"道");

                    if(LogCat.isDebug()){
                        for (QuestionModel giftModel : list) {
                            LogCat.i("错题"+giftModel.errorCount+" "+giftModel.toString());
                        }
                    }

                }else{
                    showEmptyView("少侠一道题都没有做错呢");

                    setText(R.id.fail_start_tip,"");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f_start:
                MobclickAgent.onEvent(mContext,"GiftFail");
                Bundle k=new Bundle();
                k.putSerializable(IntentHelper.KEY1,list);
                IntentHelper.openClassResult(mContext,FailAct.class, AppConstants.REQUEST_CODE,k);
                break;
            case R.id.fail_delete:
                XDialog.showSelectDialog(mContext, "是否清空错题记录", new XDialog.DialogClickListener() {
                    @Override
                    public void confirm() {
                        XAsyncTask.execute(mContext, new XAsyncTaskListenerCompat<Object, Object>() {
                            @Override
                            public Object doInBackground(Context context, Object... objects) {
                                DataManager.getInstance().saveList(mContext,CACHE_FAIL_DATA,new ArrayList<>());
                                return null;
                            }

                            @Override
                            public void onPostExecute(Context context, Object o) {
                                loadData();
                            }
                        });
                    }
                    @Override
                    public void cancel() {

                    }
                });
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            loadData();
        }
    }
}
