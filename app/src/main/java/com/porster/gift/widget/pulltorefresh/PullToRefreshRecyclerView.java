/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.porster.gift.widget.pulltorefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;


public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {




	public PullToRefreshRecyclerView(Context context) {
		super(context);
	}

	public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshRecyclerView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}
	
	@Override
	protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
		return new RecyclerView(context,attrs);
	}

    class MyRecyclerView extends RecyclerView{

		public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
			super(context, attrs);
		}

		public MyRecyclerView(Context context) {
			super(context);
		}
	}
	@Override
	protected boolean isReadyForPullEnd() {
		return !ViewCompat.canScrollVertically(getRefreshableView(),1);
	}

	@Override
	protected boolean isReadyForPullStart() {
		return !ViewCompat.canScrollVertically(getRefreshableView(),-1);
	}

	private View mEmptyView;
	public final void setEmptyView(View newEmptyView) {
		FrameLayout refreshableViewWrapper = getRefreshableViewWrapper();

		if (null != newEmptyView) {
			// New view needs to be clickable so that Android recognizes it as a
			// target for Touch Events
			newEmptyView.setClickable(true);


			ViewParent newEmptyViewParent = newEmptyView.getParent();

//			if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
//				((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
//			}

			// We need to convert any LayoutParams so that it works in our
			// FrameLayout
			if(newEmptyViewParent==null){
				FrameLayout.LayoutParams lp = convertEmptyViewLayoutParams(newEmptyView.getLayoutParams());
				if (null != lp) {
					refreshableViewWrapper.addView(newEmptyView, lp);
				} else {
					refreshableViewWrapper.addView(newEmptyView);
				}
			}
			newEmptyView.setVisibility(View.VISIBLE);
		}
		this.mEmptyView=newEmptyView;
	}

	public void hideEmptyView(){
		if(mEmptyView==null){
			return;
		}
		mEmptyView.setVisibility(View.GONE);
//		ViewParent newEmptyViewParent = mEmptyView.getParent();
//		if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
//			((ViewGroup) newEmptyViewParent).removeView(mEmptyView);
//			mEmptyView=null;
//		}
	}

	private static FrameLayout.LayoutParams convertEmptyViewLayoutParams(ViewGroup.LayoutParams lp) {
		FrameLayout.LayoutParams newLp = null;

		if (null != lp) {

			newLp = new FrameLayout.LayoutParams(lp);

			if (lp instanceof LayoutParams) {
				newLp.gravity = ((LayoutParams) lp).gravity;
			} else {
				newLp.gravity = Gravity.CENTER;
			}
		}

		return newLp;
	}
}
