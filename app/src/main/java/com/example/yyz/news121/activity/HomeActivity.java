package com.example.yyz.news121.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.view.Window;

import com.example.yyz.news121.R;
import com.example.yyz.news121.fragment.ContentFragment;
import com.example.yyz.news121.fragment.LeftmenuFragment;
import com.example.yyz.news121.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeActivity extends SlidingFragmentActivity {

    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static final String MAIN_TAG = "main_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       initSlidingMenu();
        initFragment();
    }

    private void initFragment() {
        //得到manager
        FragmentManager fm=getSupportFragmentManager();
        //开启起事物
        FragmentTransaction ft=fm.beginTransaction();
        //替换左侧菜单
        ft.replace(R.id.fl_leftmenu,new LeftmenuFragment(),LEFTMENU_TAG);

        ft.replace(R.id.fl_home,new ContentFragment(),MAIN_TAG);
        //事物提交
        ft.commit();
    }

    private void initSlidingMenu() {
        //设置左侧菜单
        setBehindContentView(R.layout.left_menu);
        //右侧菜单
        SlidingMenu slidingMenu=getSlidingMenu();
        //slidingMenu.setSecondaryMenu(R.layout.left_menu);

        //设置滑动模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //设置视图模式，左侧+主页
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置主页面占有的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,250));
    }

    //得到左侧菜单
    public LeftmenuFragment getLeftFragment() {
        FragmentManager fm=getSupportFragmentManager();
        LeftmenuFragment leftmenuFragment= (LeftmenuFragment) fm.findFragmentByTag(LEFTMENU_TAG);
        return leftmenuFragment;
    }
    public ContentFragment getContentFragment() {
        FragmentManager fm=getSupportFragmentManager();
        ContentFragment contentFragment= (ContentFragment) fm.findFragmentByTag(MAIN_TAG);
        return contentFragment;
    }
}
