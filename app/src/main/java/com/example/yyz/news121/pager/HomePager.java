package com.example.yyz.news121.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.yyz.news121.base.BasePager;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.detailpager.HomeDetail;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/1/28.
 */
public class HomePager extends BasePager {
    //private List<DetailBasePager> homeDetail;

    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //设置标题
        title.setText("主页");
        LogUtil.e("主页面被初始化了");

       // homeDetail = new ArrayList<>();
       // homeDetail.add(new HomeDetail(mcontext));

        HomeDetail homeDetialPager = new HomeDetail(mcontext);
        homeDetialPager.initData();
        View view = homeDetialPager.rootview;
        fl_content.removeAllViews();
        fl_content.addView(view);

    }

}
