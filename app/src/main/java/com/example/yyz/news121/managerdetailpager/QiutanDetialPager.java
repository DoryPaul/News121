package com.example.yyz.news121.managerdetailpager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.QiutanBean;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.view.RefreshListView;

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
 * Created by yyz on 2017/2/20.
 */
public class QiutanDetialPager extends DetailBasePager {
    public List<QiutanBean> qiutandata = new ArrayList<>();
    private RefreshListView listview;
    private QiutanAdapter qiutanAdapter;

    private Spinner sp1,sp2;
    String university,weizhi;
    private Button bt;
    public QiutanDetialPager(Context context, List<QiutanBean> qiutanData) {
        super(context);
        this.qiutandata=qiutanData;
    }

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.qiutan_detail_pager, null);
        listview = (RefreshListView) view.findViewById(R.id.qiutanlistview);
        listview.setOnRefreshListener(new MyOnRefreshListener());
        //显示头部出现分割线
        listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        listview.setFooterDividersEnabled(false);
        sp1= (Spinner) view.findViewById(R.id.spinner);
        sp2= (Spinner)view. findViewById(R.id.spinner2);
        bt= (Button) view.findViewById(R.id.search);
        return view;
    }
    @Override
    public void initData() {
        super.initData();
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                university=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                weizhi=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromNet();
            }
        });

        //得到缓存
        String saveJson = CacheUtils.getString(mcontext, Constants.qiutan_url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
    }
    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void OnPullDownRefresh() {
            getDataFromNet();
        }
    }
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.qiutan_url);
        params.addBodyParameter("university", university);
        params.addBodyParameter("weizhi", weizhi);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if(result.equals("null")){
                    Toast.makeText(mcontext, "无此数据，请重新查询", Toast.LENGTH_SHORT).show();
                }else {
                    CacheUtils.putString(mcontext, Constants.analyse_url, result);
                    processData(result);
                    listview.onRefreshFinish(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("失败=" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished");
            }
        });
    }
    private void processData(String result) {
        qiutandata.clear();
        JSONArray ja = null;
        try {
            ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String name=object.getString("name");
                String school=object.getString("university");
                String age=object.getString("age");
                String local=object.getString("weizhi");
                String score=object.getString("score");
                qiutandata.add(new QiutanBean(name,school,age,local,score));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        qiutanAdapter=new QiutanAdapter();
        listview.setAdapter(qiutanAdapter);
    }

    static class ViewHolder {
        TextView name, school, age,weizhi,score;

    }
    private class QiutanAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return qiutandata.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewholder;
            if (view == null) {
                viewholder = new ViewHolder();
                view = View.inflate(mcontext, R.layout.qiutan_item, null);

                viewholder.name= (TextView) view.findViewById(R.id.qiutanname);
                viewholder.school= (TextView) view.findViewById(R.id.qiutanschool);
                viewholder. age= (TextView) view.findViewById(R.id.qiutanage);
                viewholder. weizhi= (TextView) view.findViewById(R.id.qiutanweizhi);
                viewholder.score= (TextView) view.findViewById(R.id.qiutanscore);

                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }
            QiutanBean qiutanBean = qiutandata.get(i);
            viewholder.name.setText(qiutanBean.getName());
            viewholder.school.setText(qiutanBean.getUniversity());
            viewholder.age.setText(qiutanBean.getAge());
            viewholder.weizhi.setText(qiutanBean.getWeizhi());
            viewholder. score.setText(qiutanBean.getScore());
            return view;
        }
    }
}
