package com.porster.gift.utils;


import android.util.SparseArray;
import android.view.View;

import com.porster.gift.R;


/**
 * @version 2014年10月27日 上午10:29:38
 */
public class ViewHolder {
	 @SuppressWarnings("unchecked")
	    public static <T extends View> T get(View view, int id) {
	        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag(R.id.view_holder);
	        if (viewHolder == null) {
	            viewHolder = new SparseArray<View>();
	            view.setTag(R.id.view_holder,viewHolder);
	        }
	        View childView = viewHolder.get(id);
	        if (childView == null) {
	            childView = view.findViewById(id);
	            viewHolder.put(id, childView);
	        }
	        return (T) childView;
	    }
}
