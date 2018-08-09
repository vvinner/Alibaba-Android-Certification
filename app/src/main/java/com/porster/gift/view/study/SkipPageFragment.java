package com.porster.gift.view.study;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.porster.gift.R;
import com.porster.gift.core.BaseActivity;
import com.porster.gift.utils.IntentHelper;
import com.porster.gift.utils.ViewHolder;
import com.porster.gift.utils.ViewUtil;

import java.text.MessageFormat;

/**
 * 跳页弹窗
 * Created by Porster on 17/3/9.
 */

public class SkipPageFragment extends DialogFragment{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int val=getArguments().getInt(IntentHelper.KEY1);
        int max=getArguments().getInt(IntentHelper.KEY2);
        final int[] mUnAnswer=getArguments().getIntArray(IntentHelper.KEY3);

        final android.app.Dialog dialog=new android.app.Dialog(getActivity(), R.style.DialogStyle);
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_skip_page, null);
        dialog.setContentView(view);

        Window mWindow=dialog.getWindow();
        WindowManager.LayoutParams lp;
        if (mWindow != null) {//Maybe is null
            lp = mWindow.getAttributes();
            if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){//横屏
                lp.width= ViewUtil.getScreenHeight(getActivity())/10*8;
            }else{
                lp.width=ViewUtil.getScreenWidth(getActivity());
            }
            mWindow.setGravity(Gravity.BOTTOM);
            //添加动画
            mWindow.setWindowAnimations(R.style.dialogAnim);
            mWindow.setAttributes(lp);
        }
        final SeekBar mSeekBar= ViewHolder.get(view,R.id.seekBar);
        mSeekBar.setMax(max);
        mSeekBar.setProgress(val);


        TextView seek_go=ViewHolder.get(view,R.id.seek_go);
        seek_go.setText(MessageFormat.format("已答{0}道题", max));
        final TextView seek_now_page=ViewHolder.get(view,R.id.seek_now_page);
        seek_now_page.setText(MessageFormat.format("前往{0}", max));
        new BaseActivity().addClickEffect(seek_now_page);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek_now_page.setText(MessageFormat.format("前往 {0}", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seek_now_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                int progress=Math.max(mSeekBar.getProgress(),1);
                ((StudyAct)getActivity()).gotoLastQuestion(progress-1);
            }
        });

        return dialog;
    }
}
