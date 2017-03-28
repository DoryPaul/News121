package com.example.yyz.news121.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.HomeActivity;
import com.example.yyz.news121.activity.ManagerMainActivity;

/**
 * Created by yyz on 2017/1/28.
 */
public class ManagerBasePager {
    public   Context mcontext;
    //代表不同页面
    public View rootview;
    //实例化
    public ImageButton ib_menu;
    public TextView title;
    public FrameLayout fl_content;
    public ManagerBasePager(Context context){
        this.mcontext=context;
        //初始化视图
        rootview=initView();

    }

    private View initView() {
         View view=View.inflate(mcontext, R.layout.basepager,null);
        ib_menu= (ImageButton) view.findViewById(R.id.ib_menu);
        title= (TextView) view.findViewById(R.id.title);
        fl_content= (FrameLayout) view.findViewById(R.id.fl_content);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭左侧菜单
                ManagerMainActivity managerMainActivity= (ManagerMainActivity) mcontext;
                managerMainActivity.getSlidingMenu().toggle();//关->开；
            }
        });
        return view;
    }

    //调用数据
    public  void initData(){

    }

}
