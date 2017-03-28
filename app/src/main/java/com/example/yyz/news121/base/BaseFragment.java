package com.example.yyz.news121.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**公共类
 * Created by yyz on 2017/1/27.
 */
public abstract class BaseFragment extends Fragment{
    public Context context;
    /*
    当该类别创建的时候，被回掉
     */
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }
/*
创建视图是回掉这个方法
 */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }
//让子类是先给方法，写自己特有的布局
    public abstract View initView() ;
//当activity创建实例化好了之后回掉这个方法
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    //当孩子需要初始化数据时候重写该方法，或者联网请求数据
    public void initData() {

    }
}
