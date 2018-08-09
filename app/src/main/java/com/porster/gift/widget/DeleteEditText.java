package com.porster.gift.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.porster.gift.R;


/**
 * <p>自带删除按钮的EditText</p>
 * <p>直接在xml中使用:<com.alidao.android.common.widget.DeleteEditText/></p>
 * @version 2014年11月4日 上午10:57:54
 */
public class DeleteEditText extends EditText {

	private  Drawable imgX;

    public DeleteEditText(Context context) {
            super(context);
            init(context);
    }

    public DeleteEditText(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init(context);
    }
    public DeleteEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
    }

    private void init(Context context) {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    		imgX=getResources().getDrawable(R.drawable.clear, context.getTheme());
    	} else {
    		imgX=getResources().getDrawable(R.drawable.clear);
    	}
            // Set bounds of our X button
            imgX.setBounds(0, 0, imgX.getIntrinsicWidth(),
                            imgX.getIntrinsicHeight());

            // There may be initial text in the field, so we may need to display the
            // button
            manageClearButton();

            this.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                            DeleteEditText et = DeleteEditText.this;

                            // Is there an X showing?
                            if (et.getCompoundDrawables()[2] == null)
                                    return false;
                            // Only do this for up touches
                            if (event.getAction() != MotionEvent.ACTION_UP)
                                    return false;
                            // Is touch on our clear button?
                            if (event.getX() > et.getWidth() - et.getPaddingRight() - imgX.getIntrinsicWidth()) {
                                    et.setText("");
                                    DeleteEditText.this.removeClearButton();
                            }
                            v.performClick();
                            return false;
                    }
            });

            this.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,int count) {

                            DeleteEditText.this.manageClearButton();
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                    }
            });
            
            this.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                            DeleteEditText.this.manageClearButton();
                    }
            });
    }

    public void manageClearButton() {
            if (this.getText().toString().equals("")||!this.isFocused())
                    removeClearButton();
            else
                    addClearButton();
    }

    private void addClearButton() {
            this.setCompoundDrawables(this.getCompoundDrawables()[0],
                            this.getCompoundDrawables()[1], imgX,
                            this.getCompoundDrawables()[3]);
    }

   private void removeClearButton() {
            this.setCompoundDrawables(this.getCompoundDrawables()[0],
                            this.getCompoundDrawables()[1], null,
                            this.getCompoundDrawables()[3]);
    }
}