
package com.example.yyz.news121.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.yyz.news121.R;
import com.example.yyz.news121.fragment.ManagerFragment;
import com.example.yyz.news121.fragment.ManagerLeftmenuFragment;
import com.example.yyz.news121.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class ManagerMainActivity extends SlidingFragmentActivity {

    public static final String MANAGERLEFTMENU_TAG = "managerleftmenu_tag";
    public static final String MANAGER_1_TAG = "manager1_tag";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);
        initSlidingMenu();
        initFragment();
    }

    private void initFragment() {
        //得到manager
        FragmentManager fm=getSupportFragmentManager();
        //开启起事物
        FragmentTransaction ft=fm.beginTransaction();
        //替换左侧菜单
        ft.replace(R.id.fl_leftmenumanager,new ManagerLeftmenuFragment(),MANAGERLEFTMENU_TAG);

        ft.replace(R.id.fl_manager,new ManagerFragment(), MANAGER_1_TAG);
        //事物提交
        ft.commit();
    }

    private void initSlidingMenu() {
        //设置左侧菜单
        setBehindContentView(R.layout.left_menu_manager);
        //右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        //设置滑动模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置视图模式，左侧+主页
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置主页面占有的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this, 250));
    }

    //得到左侧菜单
    public ManagerLeftmenuFragment getManagerLeftFragment() {
        FragmentManager fm=getSupportFragmentManager();
        ManagerLeftmenuFragment managerLeftmenuFragment= (ManagerLeftmenuFragment) fm.findFragmentByTag(MANAGERLEFTMENU_TAG);
        return managerLeftmenuFragment;
    }
    public ManagerFragment getManagerFragment() {
        FragmentManager fm=getSupportFragmentManager();
        ManagerFragment managerFragment= (ManagerFragment) fm.findFragmentByTag(MANAGER_1_TAG);
        return managerFragment;
    }
}
