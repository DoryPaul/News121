package com.example.yyz.news121.managerdetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.QiutanBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/2/20.
 */
public class QiutanPager extends DetailBasePager {
    @ViewInject(R.id.analyseViewPager)
    private ViewPager viewPager;

    QiutanDetialPager qiutanDetialPager;
    private List<QiutanBean> qiutanData = new ArrayList<>();
    public List<QiutanDetialPager> qiutanDetialPagers = new ArrayList<>();

    public QiutanPager(Context context) {
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
        qiutanDetialPager = new QiutanDetialPager(mcontext, qiutanData);
        qiutanDetialPagers.add(qiutanDetialPager);
        viewPager.setAdapter(new QiutanDetailAdapter());
    }

    private class QiutanDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return qiutanDetialPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            QiutanDetialPager qiutanDetialPager = qiutanDetialPagers.get(position);
            View rootView = qiutanDetialPager.rootview;
            container.addView(rootView);
            //初始化数据
            qiutanDetialPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //  super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
