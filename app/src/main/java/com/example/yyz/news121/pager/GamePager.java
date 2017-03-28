package com.example.yyz.news121.pager;

import android.content.Context;
import android.view.View;

import com.example.yyz.news121.base.BasePager;
import com.example.yyz.news121.base.ManagerBasePager;
import com.example.yyz.news121.detailpager.HomeDetail;

import org.xutils.common.util.LogUtil;

/**
 * Created by yyz on 2017/1/28.
 */
public class GamePager extends ManagerBasePager {
    //private List<DetailBasePager> homeDetail;

    public GamePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //设置标题
        title.setText("新闻");
        LogUtil.e("新闻页面被初始化了");

       // homeDetail = new ArrayList<>();
       // homeDetail.add(new HomeDetail(mcontext));

        HomeDetail homeDetialPager = new HomeDetail(mcontext);
        homeDetialPager.initData();
        View view = homeDetialPager.rootview;
        fl_content.removeAllViews();
        fl_content.addView(view);

    }

}
