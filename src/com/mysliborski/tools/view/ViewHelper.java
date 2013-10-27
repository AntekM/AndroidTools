package com.mysliborski.tools.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ViewHelper {

    public static int dpToPx(Context ctx, int dp) {
        Resources r = ctx.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    public static int percWidthToPx(Context ctx, double perc) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        return (int) (perc * width);
    }

    public static int screenWitdthDpi(Context ctx) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, metrics.widthPixels, metrics);
    }

    public static int screenHeightDpi(Context ctx) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels, metrics);
    }
}
