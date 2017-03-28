package com.example.yyz.news121.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.yyz.news121.activity.HomeActivity;
import com.example.yyz.news121.base.BasePager;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.AllBean;
import com.example.yyz.news121.detailpager.AllDetailPager;
import com.example.yyz.news121.detailpager.EastDetailPager;
import com.example.yyz.news121.detailpager.WestDetailPager;
import com.example.yyz.news121.fragment.LeftmenuFragment;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyz on 2017/1/28.
 */
public class QiuyuanPager extends BasePager {
    private TextView textView;
    private List<DetailBasePager> detailBasePagers;
    private List<String> mDatas;
    //一定要先将list初始化，否则会报空指针异常
    public List<AllBean> data = new ArrayList<>();

    public QiuyuanPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        ib_menu.setVisibility(View.VISIBLE);
        //设置标题
        title.setText("球员");
        LogUtil.e("球员中心被初始化了");
        textView = new TextView(mcontext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setText("我是球员中心内容");

        //添加到帧布局,和子页面形成一个整体
        fl_content.addView(textView);
        String saveJson = CacheUtils.getString(mcontext,Constants.all_url);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }

        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params=new RequestParams(Constants.all_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(mcontext,Constants.all_url,result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void processData(String result) {
        data.clear();
       try {
            JSONArray jsonArr = new JSONArray(result);
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject object = jsonArr.getJSONObject(i);
               String id=object.getString("id");
                String title=object.getString("title");
                String dataurl = object.getString("dataurl");
                data.add(new AllBean(id,title,dataurl));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDatas = new ArrayList<>();
        mDatas.add("全联盟");
        mDatas.add("西部");
        mDatas.add("东部");
        //得到左侧菜单
        HomeActivity homeActivity = (HomeActivity) mcontext;
        LeftmenuFragment leftFragment = homeActivity.getLeftFragment();

        //创建三个页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new AllDetailPager(mcontext,data));
        detailBasePagers.add(new WestDetailPager(mcontext));
        detailBasePagers.add(new EastDetailPager(mcontext));

        //把数据传递给左侧菜单
        leftFragment.setData(mDatas);
    }



    //切换不同详情页面
    public void switchPager(int position) {
        //设置标题
        title.setText(mDatas.get(position));
        //设置内容
        DetailBasePager detailBasePager = detailBasePagers.get(position);
        View view = detailBasePager.rootview;
        //初始化数据
        detailBasePager.initData();

        fl_content.removeAllViews();//把之前移除
        fl_content.addView(view);//添加

    }
}
