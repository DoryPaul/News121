package com.example.yyz.news121.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.yyz.news121.base.BasePager;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.detailpager.MatchDetail;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/1/28.
 */
public class MatchPager extends BasePager {
    //private List<DetailBasePager> matchDetail;

    public MatchPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //设置标题
        title.setText("比赛");

       // matchDetail = new ArrayList<>();
       // matchDetail.add(new MatchDetail(mcontext));

        MatchDetail matchDetialPager = new MatchDetail(mcontext);
        matchDetialPager.initData();
        View view = matchDetialPager.rootview;
        fl_content.removeAllViews();
        fl_content.addView(view);

    }
}
