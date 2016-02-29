package com.yanyuanquan.android.bannerlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;


/**
 * Created by apple on 16/2/27.
 */
public class AutoBannerLayout extends LinearLayout {
    ViewPager viewpager;
    AutoPagerAdapter adapter;
    //是否自动播放
    private boolean autoPlay =true;

    private int playDuration = 2000;

    //-------指示器模in块 -----
    LinearLayout indicatorParent;
    private Config.Shape mShape = Config.Shape.oval;
    private Config.Position mPosition = Config.Position.rightBottom;

    private int focusIndicatorWidth = 16;
    private int focusIndicatorHeight = 16;
    private int unFocusIndicatorWidth = 16;
    private int unFocusIndicatorHeight = 16;
    private int indicatorMargin = 30;
    private int indicatorSpace = 3;

    private int indicatorFocusColor = 0xffff0000;
    private int indicatorUnFocusColor = 0xff00ff00;
    private int indicatorBackground = 0xeeffffff;
    private ShapeDrawable generalDrawable;
    private ShapeDrawable focusDrawable;
    private boolean indicatorClickable = false;

    private AutoBannerLayout(Context context) {
        super(context);
    }

    public AutoBannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public AutoBannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.anto_banner, this, false);
        viewpager = (ViewPager) view.findViewById(R.id.banner_viewpager);
        indicatorParent = (LinearLayout) view.findViewById(R.id.banner_indicator);
        this.addView(view);
        initData(context, attrs);
    }

    private void initData(Context context, AttributeSet attrs) {
        //-----------本地参数列表---------
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoBannerLayout);
        autoPlay = ta.getBoolean(R.styleable.AutoBannerLayout_autoPlay, autoPlay);


        int position = ta.getInt(R.styleable.AutoBannerLayout_indicatorPosition, mPosition.ordinal());
        for (Config.Position position1 : Config.Position.values()) {
            if (position == position1.ordinal()) {
                mPosition = position1;
            }
        }
        int shape = ta.getInt(R.styleable.AutoBannerLayout_indicatorShape, mShape.ordinal());
        for (Config.Shape shape1 : Config.Shape.values()) {
            if (shape == shape1.ordinal()) {
                mShape = shape1;
            }
        }
        indicatorMargin = ta.getDimensionPixelSize(R.styleable.AutoBannerLayout_indicatorMargin, indicatorMargin);
        indicatorSpace = ta.getDimensionPixelSize(R.styleable.AutoBannerLayout_indicatorSpace, indicatorSpace);
        focusIndicatorWidth = ta.getDimensionPixelSize(R.styleable.AutoBannerLayout_focusIndicatorWidth, focusIndicatorWidth);
        focusIndicatorHeight = ta.getDimensionPixelSize(R.styleable.AutoBannerLayout_focusIndicatorHeight, focusIndicatorHeight);
        unFocusIndicatorWidth = ta.getDimensionPixelSize(R.styleable.AutoBannerLayout_unfocusIndicatorWidth, unFocusIndicatorWidth);
        unFocusIndicatorHeight = ta.getDimensionPixelSize(R.styleable.AutoBannerLayout_unfocusIndicatorHeight, unFocusIndicatorHeight);

        indicatorBackground = ta.getColor(R.styleable.AutoBannerLayout_indicatorBackground, indicatorBackground);
        indicatorFocusColor = ta.getColor(R.styleable.AutoBannerLayout_indicatorFocusColor, indicatorFocusColor);
        indicatorUnFocusColor = ta.getColor(R.styleable.AutoBannerLayout_indicatorUnFocusColor, indicatorUnFocusColor);

        indicatorClickable = ta.getBoolean(R.styleable.AutoBannerLayout_indicatorClickable,indicatorClickable);
        playDuration = ta.getInteger(R.styleable.AutoBannerLayout_autoPlayDuration, playDuration);
        ta.recycle();

        switch (mShape) {
            case oval:
                generalDrawable = new ShapeDrawable(new OvalShape());
                focusDrawable = new ShapeDrawable(new OvalShape());
                break;
            default:
                generalDrawable = new ShapeDrawable(new RectShape());
                focusDrawable = new ShapeDrawable(new RectShape());
                break;
        }

        generalDrawable.getPaint().setAntiAlias(true);
        focusDrawable.getPaint().setAntiAlias(true);
        generalDrawable.getPaint().setStyle(Paint.Style.FILL);
        focusDrawable.getPaint().setStyle(Paint.Style.FILL);
        generalDrawable.getPaint().setColor(indicatorUnFocusColor);
        focusDrawable.getPaint().setColor(indicatorFocusColor);

    }

    private int indicatorCount;

    public void setData(List<String> urls) {
        indicatorCount = urls.size();
        if (urls == null || urls.size() < 1) {
            throw new IllegalStateException("item count not equal zero");
        }
        if (urls.size() == 1) {
            urls.add(urls.get(0));
            urls.add(urls.get(0));
        }
        if (urls.size() == 2) {
            urls.add(urls.get(0));
            urls.add(urls.get(1));
        }
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            views.add(getView(urls.get(i), i));
        }
        setView(views);
    }

    public void setDat(List<Integer> drawableIds) {
        indicatorCount = drawableIds.size();
        if (drawableIds == null || drawableIds.size() < 1) {
            throw new IllegalStateException("item count not equal zero");
        }
        if (drawableIds.size() == 1) {
            drawableIds.add(drawableIds.get(0));
            drawableIds.add(drawableIds.get(0));
        }
        if (drawableIds.size() == 2) {
            drawableIds.add(drawableIds.get(0));
            drawableIds.add(drawableIds.get(1));
        }
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < drawableIds.size(); i++) {
            views.add(getView(drawableIds.get(i), i));
        }
        setView(views);

    }

    private View getView(String url, final int position) {
        final ImageView imageView = new ImageView(getContext());
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(imageView, position);
                }
            }
        });
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        Glide.with(getContext()).load(url).into(imageView);
        return imageView;
    }

    private View getView(int drawableId, final int position) {
        final ImageView imageView = new ImageView(getContext());
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onItemClick(imageView, position);
                }
            }
        });
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(getContext()).load(drawableId).into(imageView);
        return imageView;
    }


    public void setView(List<View> views) {
        initIndicator();
        adapter = new AutoPagerAdapter(views);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(Integer.MAX_VALUE / 3);
        setIndicator(Integer.MAX_VALUE);
        try {
            Field field = viewpager.getClass().getDeclaredField("mScroller");
            field.setAccessible(true);
            AutoScroller scroller = new AutoScroller(getContext());
            field.set(viewpager, scroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setIndicator(position);
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setIndicator(int position) {
        int size = indicatorParent.getChildCount();
        for (int i = 0; i < size; i++) {
            ImageView imageView = (ImageView) indicatorParent.getChildAt(i);
            imageView.setBackgroundDrawable(position % indicatorCount== i
                    ? focusDrawable : generalDrawable);
        }
    }

    int i;

    private void initIndicator() {
        indicatorParent.removeAllViews();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (mPosition) {
            case centerBottom:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case centerTop:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case leftBottom:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case leftTop:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case rightBottom:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case rightTop:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
        }
        params.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
        indicatorParent.setLayoutParams(params);
        indicatorParent.requestLayout();
        for (i = 0; i < indicatorCount; i++) {
            final ImageView indicator = new ImageView(getContext());
            LinearLayout.LayoutParams params2 = new LayoutParams(focusIndicatorWidth, focusIndicatorHeight);
            params2.setMargins(indicatorSpace, indicatorSpace, indicatorSpace, indicatorSpace);
            indicator.setLayoutParams(params2);
            indicator.setBackgroundDrawable(focusDrawable);
            indicator.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (indicatorClickable){
                        viewpager.setCurrentItem(i);
                    }
                    if (indicatorItemClickListener != null) {
                        indicatorItemClickListener.onItemClick(indicator, i);
                    }
                }
            });
            indicatorParent.addView(indicator);
        }
        start();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                break;
            case MotionEvent.ACTION_UP:
                start();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private static final int HANDLE_MEG = 2016;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLE_MEG) {
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                handler.sendEmptyMessageDelayed(HANDLE_MEG, playDuration);
            }
        }
    };

    public void stop() {
        handler.removeCallbacksAndMessages(null);
    }

    public void start() {
        stop();
        if (autoPlay) {
            handler.sendEmptyMessageDelayed(HANDLE_MEG, playDuration);
        }
    }


    public void setBannerItemClickListener(onBannerItemClickListener listener) {
        this.listener = listener;
    }

    private onBannerItemClickListener listener;

    public interface onBannerItemClickListener {
        void onItemClick(ImageView view, int position);
    }

    public void setIndicatorItemClickListener(onIndicatorItemClickListener indicatorItemClickListener) {
        this.indicatorItemClickListener = indicatorItemClickListener;
    }

    public interface onIndicatorItemClickListener {
        void onItemClick(ImageView view, int position);
    }

    private onIndicatorItemClickListener indicatorItemClickListener;


}
