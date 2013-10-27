package com.mysliborski.tools.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView that holds internal aspect ration when one of the sides is getting
 * resized
 * 
 * TODO: uogolnic na szerokosc
 * 
 * @author antonimysliborski
 * 
 */
public class AspectRatioImageView extends ImageView {

	public AspectRatioImageView(Context context) {
		super(context);
	}

	public AspectRatioImageView(Context context, AttributeSet attSet) {
		super(context, attSet);
	}

	public AspectRatioImageView(Context context, AttributeSet attSet, int style) {
		super(context, attSet, style);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable drawable = getDrawable();
		if (drawable != null) {
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = MeasureSpec.getSize(heightMeasureSpec);
			int widthMode = MeasureSpec.getMode(widthMeasureSpec);
			int heightMode = MeasureSpec.getMode(heightMeasureSpec);
			int realW = drawable.getIntrinsicWidth();
			int realH = drawable.getIntrinsicHeight();
			if (realW > 0 && (width > 0 || height > 0)) {
				if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
					if (realW>realH) {
						setMeasuredDimension(width, width * realH / realW);
					} else {
						setMeasuredDimension(height * realW / realH, height);
					}
				} else if (width > 0) {
					setMeasuredDimension(width, width * realH / realW);
				} else {
					setMeasuredDimension(height * realW / realH, height);
				}
			} else
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} else
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}