package com.example.yyz.news121.pager;

import android.content.Context;
import android.view.View;

import com.example.yyz.news121.activity.ManagerMainActivity;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.base.ManagerBasePager;
import com.example.yyz.news121.fragment.LeftmenuFragment;
import com.example.yyz.news121.fragment.ManagerLeftmenuFragment;
import com.example.yyz.news121.managerdetailpager.AnalysePager;
import com.example.yyz.news121.managerdetailpager.QiutanPager;
import com.example.yyz.news121.managerdetailpager.WorkerPager;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/2/20.
 */
public class ManagerPager extends ManagerBasePager {
    private List<String> managerdata;
    private List<DetailBasePager> detailBasePagers;
    public ManagerPager(Context context) {
        super(context);
    }
    @Override
    public void initData() {
        super.initData();
        //设置标题
        title.setText("经理人主页");
        LogUtil.e("经理人主页面被初始化了");

        managerdata=new ArrayList<>();
        managerdata.add("球员分析");
        managerdata.add("球探报告");
        managerdata.add("员工管理");
       // managerdata.add("转会交易");
        //managerdata.add("罚款缴纳");
        //managerdata.add("经理交流");
        //得到左侧菜单
        ManagerMainActivity managerMainActivity = (ManagerMainActivity) mcontext;
        ManagerLeftmenuFragment managerleft = managerMainActivity.getManagerLeftFragment();

        //创建六个页面
        detailBasePagers = new ArrayList<DetailBasePager>();
        detailBasePagers.add(new AnalysePager(mcontext));
        detailBasePagers.add(new QiutanPager(mcontext));
        detailBasePagers.add(new WorkerPager(mcontext));
        managerleft.setData(managerdata);

    }

    public void switchPager(int i) {
        //设置标题
        title.setText(managerdata.get(i));
        //设置内容
        DetailBasePager detailBasePager = detailBasePagers.get(i);
        View view = detailBasePager.rootview;
        //初始化数据
        detailBasePager.initData();

        fl_content.removeAllViews();//把之前移除
        fl_content.addView(view);//添加
    }
}
