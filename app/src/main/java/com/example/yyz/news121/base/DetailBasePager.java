package com.example.yyz.news121.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yyz.news121.R;

/**
 * Created by yyz on 2017/1/31.
 */
public abstract class DetailBasePager {
    public Context mcontext;
    //代表不同页面
    public View rootview;
    //实例化
    public FrameLayout fl_content;
    public DetailBasePager(Context context){
        this.mcontext=context;
        //初始化视图
        rootview=initView();

    }

    public abstract View initView();

    //调用数据
    public  void initData(){

    }

}
