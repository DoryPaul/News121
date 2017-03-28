package com.example.yyz.news121.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.ManagerMainActivity;
import com.example.yyz.news121.base.BaseFragment;
import com.example.yyz.news121.base.ManagerBasePager;
import com.example.yyz.news121.pager.GamePager;
import com.example.yyz.news121.pager.ManagerPager;
import com.example.yyz.news121.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by yyz on 2017/2/20.
 */
public class ManagerFragment extends BaseFragment {
    @ViewInject(R.id.manager_viewpager)
    private NoScrollViewPager viewpager;

    @ViewInject(R.id.managerrg_home)
    private RadioGroup radioGroup;

    //装两个页面
    private ArrayList<ManagerBasePager> managerPagers;
    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.manager_fragment,null);
        x.view().inject(ManagerFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        managerPagers=new ArrayList<>();
        managerPagers.add(new ManagerPager(context));
        managerPagers.add(new GamePager(context));

        //设置适配器
        viewpager.setAdapter(new ManagerFragmentAdapter());

        //设置radiogroup的监听
        radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选中主页
        radioGroup.check(R.id.managerrb_home);

        //监听页面改变的方法
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        managerPagers.get(0).initData();
        //默认设置左侧菜单不可以滑动
        isEnableSlidingMenu(true);
    }

    class ManagerFragmentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return managerPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ManagerBasePager managerBasePager=managerPagers.get(position);//得到两个页面
            View view=managerBasePager.rootview;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.managerrb_home:
                    viewpager.setCurrentItem(0,false);
                    isEnableSlidingMenu(true);
                    break;
                case R.id.managerrb_player:
                    viewpager.setCurrentItem(1,false);
                    isEnableSlidingMenu(false);
                    break;
                default:
                    break;
            }

        }
    }

    //设置左侧菜单是否可以侧滑
    private void isEnableSlidingMenu(boolean is){
        ManagerMainActivity man= (ManagerMainActivity) context;
        SlidingMenu slidingMenu=man.getSlidingMenu();
        if(is){
            //可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            //不可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //哪个页面被选中就加载对应的数据
            managerPagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //得到
    public ManagerPager getManagerPager() {
        return (ManagerPager) managerPagers.get(0);
    }
}
