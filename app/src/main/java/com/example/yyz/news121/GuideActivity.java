package com.example.yyz.news121;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.yyz.news121.activity.HomeActivity;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.DensityUtil;

import java.util.ArrayList;


public class GuideActivity extends Activity implements View.OnClickListener {


    public static final String START_MAIN = "start_main";
    private ViewPager viewpager;
    private Button btnStartMain;
    private LinearLayout llPointGroup;
    private ImageView ivRedPoint;

    private int widthDpi;

    private ArrayList<ImageView> imageViews;
    /**
     * 两点间距
     */
    private int marginLeft;

    private void findViews() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btnStartMain = (Button) findViewById(R.id.btn_start_main);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        btnStartMain.setOnClickListener( this);
    }

    public void onClick(View v) {
        if (v == btnStartMain) {
            // Handle clicks for btnStartMain
            //进入主页面
            //保存进入主页面信息，关闭引导页面

            CacheUtils.putBoolean(this,START_MAIN,true);
            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);

            finish();;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViews();
        initData();

        //求间距
        //测量-指定位置-检测
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
       //设置页面的监听
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当页面滚动了的时候回调
         * @param position 当前哪个页面滚动
         * @param positionOffset 页面滑动的百分比
         * @param positionOffsetPixels 页面滑动多少像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            //间距间的滑动距离 = 间距 * 屏幕滑动的百分比
            float leftMagin = (position +   positionOffset)*marginLeft;

            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            params.leftMargin=(int) leftMagin;
            ivRedPoint.setLayoutParams(params);
        }
        /**
         * 当某个页面被选中的时候回调
         * @param position 被选中页面的下标位置
         */
        @Override
        public void onPageSelected(int position) {

            if(position==imageViews.size()-1){
                //最后一个页面显示按钮
                btnStartMain.setVisibility(View.VISIBLE);
            }else {
                //隐藏
                btnStartMain.setVisibility(View.GONE);
            }
        }
        /**
         * 页面滑动的状态改变的时候回调
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{

        @Override
        public void onGlobalLayout() {
            //取消注册
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            //间距 = 第1点距离左边的距离 - 第0个距离左边的
         marginLeft=llPointGroup.getChildAt(1).getLeft()-llPointGroup.getChildAt(0).getLeft();


        }
    }
    private void initData() {
        widthDpi= DensityUtil.dip2px(this,10);
        //数据初始化
        int[] ids=new int[]{R.drawable.guide1,R.drawable.guide2,R.drawable.guide3};
        imageViews=new ArrayList<>();
        for(int i=0;i<ids.length;i++){
            ImageView imageView=new ImageView(this);
            //设置背景
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);

            //动态创建点i
            ImageView point=new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            //像素
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(widthDpi,widthDpi);
           if(i!=0){
               params.leftMargin=widthDpi;
           }
            point.setLayoutParams(params);
            llPointGroup.addView(point);

        }
        //设置适配器
        viewpager.setAdapter(new MyPagerAdapter());

    }
    //至少要实现以下四个方法，后两个要手动添加
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView=imageViews.get(position);
            container.addView(imageView);

            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }


    }
}
