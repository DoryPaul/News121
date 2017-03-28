package com.example.yyz.news121.managerdetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.WorkerBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/2/21.
 */
public class WorkerPager extends DetailBasePager {
    @ViewInject(R.id.analyseViewPager)
    private ViewPager viewPager;

   WorkerDetialPager workerDetialPager;
    private List<WorkerBean> workerData = new ArrayList<>();
    public List<WorkerDetialPager> workerDetialPagers = new ArrayList<>();


    public WorkerPager(Context context) {
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
        workerDetialPager = new WorkerDetialPager(mcontext, workerData);
        workerDetialPagers.add(workerDetialPager);
        viewPager.setAdapter(new WorkerDetailAdapter());
    }

    private class WorkerDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return workerDetialPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            WorkerDetialPager workerDetialPager = workerDetialPagers.get(position);
            View rootView = workerDetialPager.rootview;
            container.addView(rootView);
            //初始化数据
            workerDetialPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //  super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
