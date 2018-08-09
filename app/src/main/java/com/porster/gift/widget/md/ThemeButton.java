package com.porster.gift.widget.md;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.porster.gift.R;
import com.porster.gift.utils.ViewUtil;


/**
 * 主题颜色按钮
 * @version 2015-7-6 下午3:30:15
 */
public class ThemeButton extends MaterialTextView {
	int dp10;
	public ThemeButton(Context context) {
		super(context);
		init();
	}

	public ThemeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ThemeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	private void init(){
		dp10= ViewUtil.dip2px(getContext(),15);
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		setTextColor(Color.WHITE);
		setGravity(Gravity.CENTER);
		setMaterialBackgroundDetector(getResources().getColor(R.color.black_dark));
		setBackgroundResource(R.drawable.shape_theme_btn);
		
		setPadding(dp10,dp10,dp10, dp10);
		setRaius((int)(dp10*0.3));
	}
}