package com.example.yyz.news121.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.BasePager;
import com.example.yyz.news121.detailpager.SettingDetail;

import org.xutils.common.util.LogUtil;

/**
 * Created by yyz on 2017/1/28.
 */
public class SettingPager extends BasePager {
    private TextView textView;
    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //设置标题
        title.setText("设置");
        SettingDetail setting = new SettingDetail(mcontext);
        setting.initData();
        View view = setting.rootview;
        fl_content.removeAllViews();

        //添加到帧布局,和子页面形成一个整体
       fl_content.addView(view);

    }
}
