package com.example.yyz.news121.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.NewsBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/2/19.
 */
public class HomeDetail extends DetailBasePager {
    @ViewInject(R.id.homeViewPager)
    private ViewPager viewPager;
    HomeDetialPager homeDetialPager;
    private List<NewsBean> homeData = new ArrayList<NewsBean>();
    public List<HomeDetialPager> homeDetialPagers = new ArrayList<>();

    public HomeDetail(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.home_datail, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        homeDetialPager = new HomeDetialPager(mcontext, homeData);
        homeDetialPagers.add(homeDetialPager);
        viewPager.setAdapter(new HomeDetailAdapter());
    }

    private class HomeDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return homeDetialPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            HomeDetialPager homeDetialPager = homeDetialPagers.get(position);
            View rootView = homeDetialPager.rootview;
            container.addView(rootView);
            //初始化数据
            homeDetialPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //  super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
