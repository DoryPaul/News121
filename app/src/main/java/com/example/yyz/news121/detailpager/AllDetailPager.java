package com.example.yyz.news121.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.HomeActivity;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.AllBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**全联盟详情页面
 * Created by yyz on 2017/2/4.
 */
public class AllDetailPager extends DetailBasePager {

    @ViewInject(R.id.allViewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.indicator)
    private TabPageIndicator indicator;

    @ViewInject(R.id.next_tab)
    private ImageButton next_tab;
    //数据（标题，网址）
    private List<AllBean> chuanruData=new ArrayList<>();
    //页签页面
   public List<TabDetailPager> tabDetailPagers;
    public AllDetailPager(Context context, List<AllBean> data) {
        super(context);
        this.chuanruData =data;
    }

    @Override
    public View initView() {
        LogUtil.e("全联盟被初始化");
        View view=View.inflate(mcontext, R.layout.player_datail,null);
        x.view().inject(this,view);
        next_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        return view;
    }

    public void initData(){
        super.initData();

        //创建数据
        tabDetailPagers=new ArrayList<TabDetailPager>();
        for(int i=0;i<chuanruData.size();i++){
            TabDetailPager tabDetailPager=new TabDetailPager(mcontext,chuanruData);

           //添加到集合中
            tabDetailPagers.add(tabDetailPager);

        }

        //设置适配器
        viewPager.setAdapter(new AllDetailPagerAdapter());
         //关联viewpager
        indicator.setViewPager(viewPager);
        //以后监听页面用tabPageIndicator
        indicator.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if(position==0){
                //让slidingmenu可以侧滑
                isEnableSlidingMenu(true);
            }else{//让slidingmenu不可以侧滑
                isEnableSlidingMenu(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //设置左侧菜单是否可以侧滑
    private void isEnableSlidingMenu(boolean is){
        HomeActivity homeActivity= (HomeActivity) mcontext;
        SlidingMenu slidingMenu=homeActivity.getSlidingMenu();
        if(is){
            //可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            //不可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }


    }
    class AllDetailPagerAdapter extends PagerAdapter{

        //得到标题
        @Override
        public CharSequence getPageTitle(int position) {
           // LogUtil.e("标题设置");
            return chuanruData.get(position).getTitle();

        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootview;
            container.addView(rootView);

            tabDetailPager.setPosition(position);

            //初始化数据
            tabDetailPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
