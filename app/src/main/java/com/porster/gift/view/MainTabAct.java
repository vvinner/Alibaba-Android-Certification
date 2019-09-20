package com.porster.gift.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.porster.gift.R;
import com.porster.gift.core.BaseActivity;
import com.porster.gift.core.BaseFragment;
import com.porster.gift.core.DataManager;
import com.porster.gift.core.SessionData;
import com.porster.gift.core.ThemeCore;
import com.porster.gift.utils.ApiUtils;
import com.porster.gift.view.setting.ThemeAct;
import com.porster.gift.widget.XDialog;

/**
 * 主页
 * Created by Porster on 17/2/23.
 */

public class MainTabAct extends BaseActivity implements View.OnClickListener{
    /**当前显示的fragment*/
    public BaseFragment contentFragment;

    StudyFragment mStudyFragment;
    AuthFragment mGiftFragment;
    FailFragment mFailFragment;
    SettingFragment mSettingFragment;

    int[] tabIds={R.id.tab_test,R.id.tab_exam,R.id.tab_failed,R.id.tab_setting};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main_tab);


        mStudyFragment=new StudyFragment();
        mGiftFragment=new AuthFragment();
        mFailFragment=new FailFragment();
        mSettingFragment=new SettingFragment();

        initUI();
        tintTheme();
    }

    private void initUI() {
        for (int tabId : tabIds) {
            $(tabId,this);
        }
        $(tabIds[0]).performClick();


        boolean fisrtRun= (boolean) SessionData.getObject(mContext,"fisrtRun",true);

        if(fisrtRun){
            XDialog.showRadioDialog(mContext,"作者的话","所有内容全部个人整理，出错请提issue，多多包涵",null);
            SessionData.setObject(mContext,"fisrtRun",false);
        }

        //监听解析进度
        if(DataManager.getInstance().getGiftModels().size()==0){
            showProgressDialog("正在解析题库");
            DataManager.getInstance().setOnAnalysisStateListener(new DataManager.OnAnalysisStateListener() {
                @Override
                public void onStart() {
                    showProgressDialog("正在解析题库");
                }
                @Override
                public void onEnd() {
                    dismissProgressDialog();
                }
            });
        }
    }
    /**
     * Fragment切换方法
     *
     * @Date 2014-3-7
     * @param hideFragment 要隐藏的Fragment 可以为null
     * @param startFragment 要启动的Fragment
     * @return Boolean 是否切换了
     */
    public  boolean addFragmentContainer(BaseFragment hideFragment, BaseFragment startFragment, int container) {
        if (contentFragment != null) {
            //当前Fragment与启动发Fragment一致不执行任何操作
            if (contentFragment.getTagKey().equals(startFragment.getTagKey())) {
                //不能直接用startFragment和contentFragment对象地址比较，否则异常闪退的话会出现两层一样的界面
                return false;
            }
        }
        //记录当前Fragment
        contentFragment = startFragment;
        //开启一个事物
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tagKey = startFragment.getTagKey();
        //不为空的话隐藏
        if (hideFragment != null) {
            transaction.hide(hideFragment);
        }
        //已经添加的话显示
        if (startFragment.isAdded()) {
            transaction.show(startFragment);
            //第一次添加，并保存tagKey（Fragment类名）,可以用getSupportFragmentManager()直接根据tagKey名取出该Fragment进行操作
        } else {
            transaction.add(container, startFragment, tagKey);
        }
        transaction.commitAllowingStateLoss();

        return true;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        changeTab(id);
        switch (id){
            case R.id.tab_test:
                addFragmentContainer(contentFragment,mStudyFragment,R.id.container);
                break;
            case R.id.tab_exam:
                addFragmentContainer(contentFragment,mGiftFragment,R.id.container);
                break;
            case R.id.tab_failed:
                addFragmentContainer(contentFragment,mFailFragment,R.id.container);
                break;
            case R.id.tab_setting:
                addFragmentContainer(contentFragment,mSettingFragment,R.id.container);
                break;
        }
    }
    private void changeTab(int id){
        for (int i = 0; i < tabIds.length; i++) {
            $(tabIds[i]).setSelected(false);
        }
        $(id).setSelected(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== ThemeAct.REQ_THEME&&resultCode==RESULT_OK){
            tintTheme();
        }else{
            contentFragment.onActivityResult(requestCode,resultCode,data);
        }
    }
    private void tintTheme(){
        int themeColor=ThemeCore.getThemeColor(this);
        if (ApiUtils.isLolinpop()) {
            getWindow().setStatusBarColor(ThemeCore.getStateBarColor(themeColor));
        }
        ((ViewGroup)$(tabIds[0]).getParent()).setBackgroundColor(themeColor);
        mStudyFragment.tintTheme();
        mSettingFragment.tintTheme();
        mFailFragment.tintTheme();
        mGiftFragment.tintTheme();
    }
    boolean canBack;
    @Override
    public void onBackPressed() {
        if(contentFragment!=mStudyFragment){
            $(tabIds[0]).performClick();
        }else{
            if(canBack){
                finish();
                return;
            }
            canBack=true;
            showToast("再按一次退出");
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    canBack=false;
                }
            },1500);
        }
    }
}
