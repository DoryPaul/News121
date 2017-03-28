package com.example.yyz.news121.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.MatchBean;
import com.example.yyz.news121.bean.NewsBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/2/19.
 */
public class MatchDetail extends DetailBasePager {
    @ViewInject(R.id.homeViewPager)
    private ViewPager viewPager;
    MatchDetialPager matchDetialPager;
    private List<MatchBean> matchData = new ArrayList<>();
    public List<MatchDetialPager> matchDetialPagers = new ArrayList<>();

    public MatchDetail(Context context) {
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
        matchDetialPager = new MatchDetialPager(mcontext, matchData);
        matchDetialPagers.add(matchDetialPager);
        viewPager.setAdapter(new MatchDetailAdapter());
    }

    private class MatchDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return matchDetialPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MatchDetialPager matchDetialPager = matchDetialPagers.get(position);
            View rootView = matchDetialPager.rootview;
            container.addView(rootView);
            //初始化数据
            matchDetialPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //  super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
