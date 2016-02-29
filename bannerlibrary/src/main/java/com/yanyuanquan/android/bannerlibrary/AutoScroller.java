package com.yanyuanquan.android.bannerlibrary;

import android.content.Context;
import android.print.PrintJob;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by apple on 16/2/29.
 */
public class AutoScroller extends Scroller{
    private int duration = 1000;

    public AutoScroller(Context context) {
        super(context);
    }

    public AutoScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public AutoScroller(Context context, Interpolator interpolator, int duration) {
        super(context, interpolator);
        this.duration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.duration);
    }
}
