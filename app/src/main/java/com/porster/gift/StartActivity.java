package com.porster.gift;

import android.os.Bundle;
import android.view.KeyEvent;

import com.porster.gift.core.BaseActivity;
import com.porster.gift.view.MainTabAct;

/**
*   欢迎页
*   @author Porster
*   @time   2017/9/25 10:45
**/
public class StartActivity extends BaseActivity{
    long mStartTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        supportLolinpop();
        setContentView(R.layout.activity_start);
        mStartTime=System.currentTimeMillis();
        //Ad
        VISIBLE($(R.id.wel_name));
        getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goOn();
                }
            },2000);
    }
    public void goOn(){
        to(MainTabAct.class);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
