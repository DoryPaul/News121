package com.example.yyz.news121.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.MatchBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/2/19.
 */
public class SettingDetail extends DetailBasePager {
    @ViewInject(R.id.homeViewPager)
    private ViewPager viewPager;
    SettingDetialPager settingDetialPager;
    public List<SettingDetialPager> settingDetialPagers = new ArrayList<>();

    public SettingDetail(Context context) {
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
        settingDetialPager = new SettingDetialPager(mcontext);
        settingDetialPagers.add(settingDetialPager);
        viewPager.setAdapter(new SettingDetailAdapter());
    }

    private class SettingDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return settingDetialPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SettingDetialPager ettingDetialPager = settingDetialPagers.get(position);
            View rootView = settingDetialPager.rootview;
            container.addView(rootView);
            //初始化数据
            settingDetialPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //  super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
