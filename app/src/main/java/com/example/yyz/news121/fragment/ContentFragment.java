package com.example.yyz.news121.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.HomeActivity;
import com.example.yyz.news121.base.BaseFragment;
import com.example.yyz.news121.base.BasePager;
import com.example.yyz.news121.pager.HomePager;
import com.example.yyz.news121.pager.QiuyuanPager;
import com.example.yyz.news121.pager.SettingPager;
import com.example.yyz.news121.pager.MatchPager;
import com.example.yyz.news121.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**左侧菜单fragment
 * Created by yyz on 2017/1/27.
 */
public class ContentFragment extends BaseFragment{
    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewpager;

    @ViewInject(R.id.rg_home)
    private RadioGroup radioGroup;

    //装五个页面
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
       View view=View.inflate(context, R.layout.content_fragment,null);
        x.view().inject(ContentFragment.this,view);
        return view;
    }

    public void initData(){
        super.initData();

        basePagers=new ArrayList<>();
        basePagers.add(new HomePager(context));//添加主页面
        basePagers.add(new QiuyuanPager(context));//添加球员页面
        basePagers.add(new MatchPager(context));//添加比赛页面
        basePagers.add(new SettingPager(context));//添加设置页面

        //设置适配器
        viewpager.setAdapter(new ContentFragmentAdapter());

        //设置radiogroup的监听
        radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选中主页
        radioGroup.check(R.id.rb_home);

        //监听页面改变的方法
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        basePagers.get(0).initData();
        //默认设置左侧菜单不可以滑动
        isEnableSlidingMenu(false);

    }

    //得到新闻中心
    public QiuyuanPager getNewsCenterPager() {
        return (QiuyuanPager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //哪个页面被选中就加载对应的数据
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.rb_home://主页
                    viewpager.setCurrentItem(0,false);
                    isEnableSlidingMenu(false);
                    break;
                case R.id.rb_player://新闻,true则有切换动画
                    viewpager.setCurrentItem(1,false);
                    isEnableSlidingMenu(true);
                    break;
                case R.id.rb_match:
                    viewpager.setCurrentItem(2,false);
                    isEnableSlidingMenu(false);
                    break;
                case R.id.rb_setting:
                    viewpager.setCurrentItem(4,false);
                    isEnableSlidingMenu(false);
                    break;

            }
        }
    }
    //设置左侧菜单是否可以侧滑
    private void isEnableSlidingMenu(boolean is){
        HomeActivity homeActivity= (HomeActivity) context;
        SlidingMenu slidingMenu=homeActivity.getSlidingMenu();
        if(is){
            //可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            //不可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }


    }
    class  ContentFragmentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return basePagers.size();
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager=basePagers.get(position);//得到五个页面
            View view=basePager.rootview;
            //初始化数据
          //  basePager.initData();//屏蔽加载下一个页面的数据
            container.addView(view);
            return view;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
