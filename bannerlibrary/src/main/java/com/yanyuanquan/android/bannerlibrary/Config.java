package com.yanyuanquan.android.bannerlibrary;

import android.content.Context;

/**
 * Created by apple on 16/2/28.
 */
public class Config {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }



    public static enum Position {
        centerBottom,
        rightBottom,
        leftBottom,
        centerTop,
        rightTop,
        leftTop
    }
    public static enum Shape {
        rect,
        oval
    }
}
