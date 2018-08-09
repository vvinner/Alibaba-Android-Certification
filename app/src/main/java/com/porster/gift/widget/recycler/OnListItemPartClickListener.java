package com.porster.gift.widget.recycler;

import android.view.View;

/**
 * <p>列表上的小部件点击事件,配合 ALDBaseAdapter 来使用</p>
 * <p>调用方式：ALDBaseAdapter.setOnItemPartClick(OnListItemPartClickListener onItemPartClick)</p>
 */
public interface OnListItemPartClickListener {

	/**
	 * 列表部件点击事件
	 * @param view 控件
	 * @param obj list item所用的数据对象
	 * @param state
	 */
	 void onListItemPartClick(View view, Object obj, int state);
}