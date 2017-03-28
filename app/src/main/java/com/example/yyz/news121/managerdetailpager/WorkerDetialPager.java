package com.example.yyz.news121.managerdetailpager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.WorkerBean;
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
 * Created by yyz on 2017/2/21.
 */
public class WorkerDetialPager extends DetailBasePager implements AdapterView.OnItemClickListener, View.OnClickListener {
    public List<WorkerBean> workerData = new ArrayList<>();
    private RefreshListView listview;
    private WorkerAdapter workerAdapter;
    private TextView tvname, tvzhiwei, tvsalary, tvyear;
    private Button add, delete, update;
    private String id, a, b, c, d;

    public WorkerDetialPager(Context context, List<WorkerBean> workerData) {
        super(context);
        this.workerData = workerData;
    }

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.worker_detail_pager, null);
        listview = (RefreshListView) view.findViewById(R.id.workerlistView);
        listview.setOnRefreshListener(new MyOnRefreshListener());
        listview.setOnItemClickListener(this);
        //显示头部出现分割线
        listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        listview.setFooterDividersEnabled(false);

        tvname = (TextView) view.findViewById(R.id.stuffname);
        tvzhiwei = (TextView) view.findViewById(R.id.stuffzhiwei);
        tvsalary = (TextView) view.findViewById(R.id.stuffsalary);
        tvyear = (TextView) view.findViewById(R.id.stuffheyue);

        add = (Button) view.findViewById(R.id.add);
        delete = (Button) view.findViewById(R.id.delete);
        update = (Button) view.findViewById(R.id.update);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
        //得到缓存
        String saveJson = CacheUtils.getString(mcontext, Constants.workermanager_url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        WorkerBean worker = workerData.get(i - 1);
        tvname.setText(worker.getName());
        tvzhiwei.setText(worker.getZhiwei());
        tvsalary.setText(worker.getSalary());
        tvyear.setText(worker.getYear());
        id = worker.getId();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                addworker();
                break;
            case R.id.delete:
                deleteworker();
                break;
            case R.id.update:
                 updateworker();
                break;
        }
    }

    private void updateworker() {
        if(id.equals("")){
            Toast.makeText(mcontext, "请选择一个要修改的员工", Toast.LENGTH_SHORT).show();
        }else {
            a = tvname.getText().toString();
            b = tvzhiwei.getText().toString();
            c = tvsalary.getText().toString();
            d = tvyear.getText().toString();
            RequestParams pa=new RequestParams(Constants.updateworker_url);
            pa.addBodyParameter("workername", a);
            pa.addBodyParameter("zhiwei", b);
            pa.addBodyParameter("salary", c);
            pa.addBodyParameter("year", d);
            pa.addBodyParameter("id",id);
            x.http().post(pa, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        String status=result.getString("state");
                        if(status.equals("updatesuccess")){
                            Toast.makeText(mcontext, "修改成功", Toast.LENGTH_SHORT).show();
                            id="";
                            tvname.setText("");
                            tvsalary.setText("");
                            tvyear.setText("");
                            tvzhiwei.setText("");
                            getDataFromNet();
                        }else {
                            LogUtil.e(status);
                            Toast.makeText(mcontext, "修改失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
    }

    private void deleteworker() {
        if (id.equals("")) {
            Toast.makeText(mcontext, "请选择一个要删除的员工", Toast.LENGTH_SHORT).show();
        } else {
            RequestParams pa=new RequestParams(Constants.deleteworker_url);
            pa.addBodyParameter("id",id);
            x.http().post(pa, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        String status=result.getString("state");
                        if(status.equals("deletesuccess")){
                            Toast.makeText(mcontext, "删除成功", Toast.LENGTH_SHORT).show();
                            id="";
                            tvname.setText("");
                            tvsalary.setText("");
                            tvyear.setText("");
                            tvzhiwei.setText("");
                            getDataFromNet();
                        }else {
                            Toast.makeText(mcontext, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
    }

    private void addworker() {
        a = tvname.getText().toString();
        b = tvzhiwei.getText().toString();
        c = tvsalary.getText().toString();
        d = tvyear.getText().toString();
        RequestParams params = new RequestParams(Constants.addworker_url);
        if (a.equals("") || b.equals("") || c.equals("") || d.equals("")) {
            Toast.makeText(mcontext, "必须输入内容", Toast.LENGTH_SHORT).show();
        } else {
            params.addBodyParameter("workername", a);
            params.addBodyParameter("zhiwei", b);
            params.addBodyParameter("salary", c);
            params.addBodyParameter("year", d);
            x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        String status = result.getString("state");
                        if (status.equals("addsuccess")) {
                            Toast.makeText(mcontext, "添加成功", Toast.LENGTH_SHORT).show();
                            id="";
                            tvname.setText("");
                            tvsalary.setText("");
                            tvyear.setText("");
                            tvzhiwei.setText("");
                            getDataFromNet();
                        } else {
                            Toast.makeText(mcontext, "添加失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(mcontext, "出错", Toast.LENGTH_SHORT).show();
                    LogUtil.e(ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    LogUtil.e(cex.getMessage());
                }

                @Override
                public void onFinished() {
                    LogUtil.e("结束");
                }
            });
        }

    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void OnPullDownRefresh() {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.workermanager_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //缓存
                CacheUtils.putString(mcontext, Constants.workermanager_url, result);
                processData(result);
                listview.onRefreshFinish(true);
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
        workerData.clear();
        JSONArray ja = null;
        try {
            ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String id = object.getString("id");
                String name = object.getString("name");
                String zhiwei = object.getString("zhiwei");
                String salary = object.getString("salary");
                String year = object.getString("year");

                workerData.add(new WorkerBean(id, name, zhiwei, salary, year));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        workerAdapter = new WorkerAdapter();
        listview.setAdapter(workerAdapter);
    }

    static class ViewHolder {
        TextView id, name, zhiwei, salary, year;
    }

    private class WorkerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return workerData.size();
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

                viewholder.id = (TextView) view.findViewById(R.id.qiutanname);
                viewholder.name = (TextView) view.findViewById(R.id.qiutanschool);
                viewholder.zhiwei = (TextView) view.findViewById(R.id.qiutanage);
                viewholder.salary = (TextView) view.findViewById(R.id.qiutanweizhi);
                viewholder.year = (TextView) view.findViewById(R.id.qiutanscore);

                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }
            WorkerBean workerbean = workerData.get(i);
            viewholder.id.setText(workerbean.getId());
            viewholder.name.setText(workerbean.getName());
            viewholder.zhiwei.setText(workerbean.getZhiwei());
            viewholder.salary.setText(workerbean.getSalary());
            viewholder.year.setText(workerbean.getYear());
            return view;
        }
    }
}
