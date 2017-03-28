package com.example.yyz.news121.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yyz on 2017/2/10.
 *水平方向的viewpager
 */
public class HorizontalScrollViewPager extends ViewPager {
    public HorizontalScrollViewPager(Context context){
        this(context,null);
    }
    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float startx;
    private float starty;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //一定要先把事件给自己
                getParent().requestDisallowInterceptTouchEvent(true);
                //记录起始坐标
                startx=ev.getRawX();
                starty=ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //来到新的坐标
                float endx=ev.getRawX();
                float endy=ev.getRawY();
                //计算偏移量
                float distantx=endx-startx;
                float distanty=endy- starty;
                //判断滑动方向
                if(Math.abs(distantx)>Math.abs(distanty)){
                    //水平方向滑动,1.第0个位置（从左到右）
                    if(getCurrentItem()==0&&distantx>0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //2.最后一个位置（从右到左）
                    else if(getCurrentItem()==(getAdapter().getCount()-1)&&distantx<0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else {
                        //3.中间部分
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else {
                    //竖直滑动
                    getParent().requestDisallowInterceptTouchEvent(false);

                }
                break;
            case MotionEvent.ACTION_UP:

        }
        return super.dispatchTouchEvent(ev);
    }
}
