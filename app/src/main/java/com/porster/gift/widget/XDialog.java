package com.porster.gift.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.porster.gift.R;
import com.porster.gift.utils.ViewUtil;


/**
 * 仿IOS7编写的Dialog,提供了选择、单选、列表单选等等</p>
 */
public class XDialog {
	/**选择对话框*/
	private final static int SELECT_DIALOG=1;
	/**单选对话框*/
	private final static int RADIO_DIALOG=2;

	public static android.app.Dialog showListDialog(Context context,String title,String[] items,final DialogItemClickListener dialogItemClickListener){
		return ShowDialog(context,title,items,0,dialogItemClickListener);
	}
	/**
	 * 创建一个内容多选对话框
	 * @param context	ctx
	 * @param title	标题
	 * @param items 数组
	 * @param itemsColor 选中项的字体颜色
	 * @param dialogItemClickListener 监听点击的内容结果
	 * @return	Dialog
	 */
	public static android.app.Dialog showListDialog(Context context,String title,String[] items,int itemsColor,final DialogItemClickListener dialogItemClickListener){
		return ShowDialog(context,title,items,itemsColor,dialogItemClickListener);
	}
	
	/**
	 * 创建一个单选对话框
	 * @param toast 提示消息
	 * @param dialogClickListener 点击监听
	 * @return	Dialog
	 */
	public static android.app.Dialog showRadioDialog(Context context,String toast,final DialogClickListener dialogClickListener){
		return ShowDialog(context,context.getResources().getString(R.string.x_point_message),toast,"","",dialogClickListener,RADIO_DIALOG);
	}
	/**
	 * 创建一个单选对话框
	 * @param title 标题
	 * @param toast 提示消息
	 * @param dialogClickListener 点击监听
	 * @return	Dialog
	 */
	public static android.app.Dialog showRadioDialog(Context context,String title,String toast,final DialogClickListener dialogClickListener){
		return ShowDialog(context,title,toast,"","",dialogClickListener,RADIO_DIALOG);
	}
	/**
	 * 创建一个选择对话框
	 * @param toast 提示消息
	 * @param dialogClickListener 点击监听
	 * @return	Dialog
	 */
	public static android.app.Dialog showSelectDialog(Context context,String toast,final DialogClickListener dialogClickListener){
		return ShowDialog(context,context.getResources().getString(R.string.x_point_message),toast,"","",dialogClickListener,SELECT_DIALOG);
	}
	/**
	 * 创建一个选择对话框
	 * @param toast 提示消息
	 * @param confirmStr 确定按钮的文字
	 * @param dialogClickListener 点击监听
	 * @return	Dialog
	 */
	public static android.app.Dialog showSelectDialogCustomConfirm(Context context,String toast,String confirmStr,final DialogClickListener dialogClickListener){
		return ShowDialog(context,context.getResources().getString(R.string.x_point_message),toast,"",confirmStr,dialogClickListener,SELECT_DIALOG);
	}
	/**
	 * 创建一个选择对话框
	 * @param toast 提示消息
	 * @param cancelStr 取消按钮的文字
	 * @param confirmStr 确定按钮的文字
	 * @param dialogClickListener 点击监听
	 * @return	Dialog
	 */
	public static android.app.Dialog showSelectDialogCustomButtonName(Context context,String toast,String cancelStr,String confirmStr,final DialogClickListener dialogClickListener){
		return ShowDialog(context,context.getResources().getString(R.string.x_point_message),toast,cancelStr,confirmStr,dialogClickListener,SELECT_DIALOG);
	}
	/**
	 * 创建一个选择对话框
	 * @param title 标题文字
	 * @param toast 提示消息
	 * @param cancelStr 取消按钮的文字
	 * @param confirmStr 确定按钮的文字
	 * @param dialogClickListener 点击监听
	 * @return	Dialog
	 */
	public static android.app.Dialog showSelectDialogCustomButtonName(Context context,String title,String toast,String cancelStr,String confirmStr,final DialogClickListener dialogClickListener){
		return ShowDialog(context,title,toast,cancelStr,confirmStr,dialogClickListener,SELECT_DIALOG);
	}
	/**
	 * 创建一个选择对话框
	 * @param title 提示标题
	 * @param toast 提示消息
	 * @param dialogClickListener 点击监听
	 * @return	Dialog
	 */
	public static android.app.Dialog showSelectDialog(Context context,String title,String toast,final DialogClickListener dialogClickListener){
		return ShowDialog(context,title,toast,"","",dialogClickListener,SELECT_DIALOG);
	}
	private static android.app.Dialog ShowDialog(Context context,String title,String toast,String cancelStr,String confirmStr,final DialogClickListener dialogClickListener,int DialogType){
		final android.app.Dialog dialog=new android.app.Dialog(context, R.style.DialogStyle);
		dialog.setCancelable(false);
		final View view=LayoutInflater.from(context).inflate(R.layout.x_dialog, null);
		dialog.setContentView(view);
		((TextView)view.findViewById(R.id.point)).setText(title);
		((TextView)view.findViewById(R.id.toast)).setText(toast);
		((TextView)view.findViewById(R.id.toast)).setMovementMethod(ScrollingMovementMethod.getInstance());
		if(DialogType==RADIO_DIALOG){
		}else{
			view.findViewById(R.id.ok).setVisibility(View.GONE);
			view.findViewById(R.id.divider).setVisibility(View.VISIBLE);
		}
		TextView cancel=(TextView) view.findViewById(R.id.cancel);
		if(!TextUtils.isEmpty(cancelStr))//自定义确定按钮的字符
			cancel.setText(cancelStr);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				view.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(dialogClickListener!=null)
							dialogClickListener.cancel();
					}
				},300);
				dialog.dismiss();
			}
		});
		TextView confirm=(TextView) view.findViewById(R.id.confirm);
		if(!TextUtils.isEmpty(confirmStr))//自定义确定按钮的字符
			confirm.setText(confirmStr);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				view.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(dialogClickListener!=null)
							dialogClickListener.confirm();
					}
				},200);
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				view.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(dialogClickListener!=null)
							dialogClickListener.confirm();
					}
				},200);
				dialog.dismiss();
			}
		});
		Window mWindow=dialog.getWindow();
		if(mWindow!=null){
			WindowManager.LayoutParams lp = mWindow.getAttributes();
			if(context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){//横屏
				lp.width= ViewUtil.getScreenHeight(context)/10*8;
			}else{
				lp.width=ViewUtil.getScreenWidth(context)/10*8;
			}
			mWindow.setAttributes(lp);
		}

		dialog.show();
		
		return dialog;
	}

	private static android.app.Dialog ShowDialog(Context context, String title, String[] items, final int itemsColor, final DialogItemClickListener dialogClickListener){
		final android.app.Dialog dialog=new android.app.Dialog(context, R.style.DialogStyle);
//		dialog.setCancelable(false);
		View view=LayoutInflater.from(context).inflate(R.layout.x_dialog_radio, null);
		dialog.setContentView(view);
		((TextView)view.findViewById(R.id.title)).setText(title);
		//根据items动态创建
		LinearLayout parent=(LinearLayout) view.findViewById(R.id.dialogLayout);
		parent.removeAllViews();
		final int length=items.length;
		for (int i = 0; i < items.length; i++) {
			LayoutParams params1=new LayoutParams(-1,-2);
			params1.rightMargin=1;
			final TextView tv=new TextView(context);
			tv.setLayoutParams(params1);
			tv.setTextSize(18);
			tv.setText(items[i]);
			if(itemsColor!=0)
				tv.setTextColor(itemsColor);
			else
				tv.setTextColor(context.getResources().getColor(R.color.x_dialog_txt_color));
			int pad= ViewUtil.dip2px(context,10);
			tv.setPadding(pad,pad,pad,pad);
			tv.setSingleLine(true);
			tv.setGravity(Gravity.CENTER);
			if(i!=length-1)
				tv.setBackgroundResource(R.drawable.x_menudialog_center_selector);
			else
				tv.setBackgroundResource(R.drawable.x_menudialog_bottom2_selector);
				
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
					if(dialogClickListener!=null)
						dialogClickListener.confirm(tv.getText().toString());
				}
			});
			parent.addView(tv);
			if(i!=length-1){
				TextView divider=new TextView(context);
				LayoutParams params=new LayoutParams(-1, 1);
				divider.setLayoutParams(params);
				divider.setBackgroundResource(android.R.color.darker_gray);
				parent.addView(divider);
			}

			ViewTreeObserver vto = tv.getViewTreeObserver();
			final int index = i;
			final ScrollView scrollView= (ScrollView)view.findViewById(R.id.scroll);
			vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					tv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					int itemHeight = tv.getHeight();
					if(index==0) {
						if(length>=6) {
							scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int)(itemHeight * 5.5f)));
						}
					}
				}
			});
		}
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Window mWindow=dialog.getWindow();
		WindowManager.LayoutParams lp;
		if (mWindow != null) {//Maybe is null
			lp = mWindow.getAttributes();
			if(context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){//横屏
				lp.width=ViewUtil.getScreenHeight(context)/10*8;
			}else{
				lp.width=ViewUtil.getScreenWidth(context);
			}
			mWindow.setGravity(Gravity.BOTTOM);
			//添加动画
			mWindow.setWindowAnimations(R.style.dialogAnim);
			mWindow.setAttributes(lp);
		}
		dialog.show();
		return dialog;
	}
	public interface DialogClickListener{
		void confirm();
		void cancel();
	}
	public interface DialogItemClickListener{
		void confirm(String result);
	}
}