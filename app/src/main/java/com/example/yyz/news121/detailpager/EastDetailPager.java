package com.example.yyz.news121.detailpager;

import android.content.Context;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.EastBean;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**东部详情页面
 * Created by yyz on 2017/2/4.
 */
public class EastDetailPager extends DetailBasePager {
    @ViewInject(R.id.homeViewPager)
    private ViewPager viewPager;

    EastTabPager eastTabPager;

    public List<EastTabPager> eastDetialPagers = new ArrayList<>();
    public EastDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.home_datail, null);
        x.view().inject(this, view);
        return view;
    }

    public void initData(){
        super.initData();
        eastTabPager=new EastTabPager(mcontext);
        eastDetialPagers.add(eastTabPager);
        viewPager.setAdapter(new EastDetailAdapter());


    }


    private class EastDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return eastDetialPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            EastTabPager eastTabPager = eastDetialPagers.get(position);
            View rootView = eastTabPager.rootview;
            container.addView(rootView);
            //初始化数据
            eastTabPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
