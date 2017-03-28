package com.example.yyz.news121.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yyz on 2017/1/28.
 */
public class NoScrollViewPager extends ViewPager {
    //自定义不可以滑动的viewpager
    public NoScrollViewPager(Context context) {
       this(context,null);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //滑动实践设置为true，把滑动事件消耗掉
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return  true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
