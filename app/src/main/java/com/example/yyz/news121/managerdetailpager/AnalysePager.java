package com.example.yyz.news121.managerdetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.AnalyseBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/2/20.
 */
public class AnalysePager extends DetailBasePager {

    @ViewInject(R.id.analyseViewPager)
    private ViewPager viewPager;

    AnalyseDetialPager analyseDetialPager;
    private List<AnalyseBean> analyseData = new ArrayList<AnalyseBean>();
    public List<AnalyseDetialPager> analyseDetialPagers = new ArrayList<>();

    public AnalysePager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view=View.inflate(mcontext, R.layout.analyse_datail,null);
        x.view().inject(this,view);
        return view;
    }
    @Override
    public void initData() {
        super.initData();
        analyseDetialPager = new AnalyseDetialPager(mcontext, analyseData);
        analyseDetialPagers.add(analyseDetialPager);
        viewPager.setAdapter(new AnalyseDetailAdapter());
    }

    private class AnalyseDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return analyseDetialPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            AnalyseDetialPager analyseDetialPager = analyseDetialPagers.get(position);
            View rootView = analyseDetialPager.rootview;
            container.addView(rootView);
            //初始化数据
            analyseDetialPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //  super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
