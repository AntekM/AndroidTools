package com.mysliborski.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Horizontal Scroll View with support for end scroll listeners
 * (za strona http://stackoverflow.com/questions/8181828/android-detect-when-scrollview-stops-scrolling) 
 * @author antonimysliborski
 */
public class AMHorizontalScrollView extends HorizontalScrollView {

	public AMHorizontalScrollView(Context context) {
		super(context);
		initScroll();
	}

	public AMHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initScroll();
	}

	public AMHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initScroll();
	}

	private Runnable scrollerTask;
	private int initialPosition;

	private int newCheck = 100;

	public interface OnScrollStoppedListener {
		void onScrollStopped();
	}

	private OnScrollStoppedListener onScrollStoppedListener;

	private void initScroll() {

		scrollerTask = new Runnable() {

			public void run() {

				int newPosition = getScrollX();
				if (initialPosition - newPosition == 0) {// has stopped
					if (onScrollStoppedListener != null) {

						onScrollStoppedListener.onScrollStopped();
					}
				} else {
					initialPosition = getScrollX();
					postDelayed(scrollerTask, newCheck);
				}
			}
		};
		this.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					startScrollerTask();
				}
				return false;
			}
		});
	}

	public void setOnScrollStoppedListener(OnScrollStoppedListener listener) {
		onScrollStoppedListener = listener;
	}

	public void startScrollerTask() {
		if (onScrollStoppedListener != null) {
			initialPosition = getScrollX();
			postDelayed(scrollerTask, newCheck);
		}
	}

}
