package com.example.yyz.news121.managerdetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.FenxiActivity;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.AnalyseBean;
import com.example.yyz.news121.utils.BitmapUtils;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.utils.NetCacheUtils;
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
public class AnalyseDetialPager extends DetailBasePager implements AdapterView.OnItemClickListener {
    public List<AnalyseBean> analysedata = new ArrayList<AnalyseBean>();
    private RefreshListView listview;
    private AnalyseAdapter analyseAdapter;

    public AnalyseDetialPager(Context context,List<AnalyseBean> analysedata) {
        super(context);
        this.analysedata=analysedata;
    }

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.analyse_detail_pager, null);
        listview = (RefreshListView) view.findViewById(R.id.analyselistview);
        listview.setOnRefreshListener(new MyOnRefreshListener());
        listview.setOnItemClickListener(this);
        //显示头部出现分割线
        listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        listview.setFooterDividersEnabled(false);
        return view;
    }
    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
        //得到缓存
        String saveJson = CacheUtils.getString(mcontext, Constants.analyse_url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent();
    AnalyseBean analyseBean=analysedata.get(i-1);
    intent.putExtra("url", analyseBean.getQiuyuanpicurl());
    intent.putExtra("defen", analyseBean.getScore());
    intent.putExtra("zhugong", analyseBean.getAssist());
    intent.putExtra("qiangduan", analyseBean.getSteal());
    intent.putExtra("lanban", analyseBean.getRebound());
    intent.putExtra("gaimao", analyseBean.getBlock());
    intent.putExtra("name", analyseBean.getName());

    intent.setClass(mcontext,FenxiActivity.class);
    mcontext.startActivity(intent);


    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void OnPullDownRefresh() {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.analyse_url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(mcontext, Constants.analyse_url, result);
                processData(result);
                listview.onRefreshFinish(true);
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
        analysedata.clear();
        JSONArray ja = null;
        try {
            ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String qiuyuaniconurl = object.getString("qiuyuanpicurl");
                String name = object.getString("name");
                String weizhi = object.getString("weizhi");
                String blengqiudui = object.getString("blengqiudui");
                String gongzi = object.getString("gongzi");
                String score= String.valueOf(object.getDouble("score"));
                String assist= String.valueOf(object.getDouble("assist"));
                String block= String.valueOf(object.getDouble("block"));
                String rebound= String.valueOf(object.getDouble("rebound"));
                String steal= String.valueOf(object.getDouble("steal"));

                analysedata.add(new AnalyseBean(qiuyuaniconurl, name, weizhi, blengqiudui,gongzi,
                        score,assist,block,rebound,steal));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        analyseAdapter=new AnalyseAdapter(mcontext,analysedata);
        listview.setAdapter(analyseAdapter);
    }

    private class AnalyseAdapter extends BaseAdapter {
        private Context mcontext;
        private  List<AnalyseBean> list;
        private BitmapUtils bitmapUtils;
        private Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case NetCacheUtils.SUCCESS:
                        Bitmap bitmap= (Bitmap) msg.obj;
                        int position=msg.arg1;
                        //Log.e("TAG","图片请求成功"+position);
                        if(listview!=null&&listview.isShown()){
                            ImageView imageView= (ImageView) listview.findViewWithTag(position);
                            if(imageView!=null&&bitmap!=null){
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                        break;
                    case NetCacheUtils.FAIL:
                        position=msg.arg1;
                        //  Log.e("TAG","图片请求失败"+position);
                        break;
                }
            }
        };
        public AnalyseAdapter(Context context, List<AnalyseBean> list){
            this.mcontext=context;
            this.list=list;
            bitmapUtils=new BitmapUtils(handler);
        }
        @Override
        public int getCount() {
            return analysedata.size();
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
                view = View.inflate(mcontext, R.layout.analyse_item, null);

                viewholder.playername= (TextView) view.findViewById(R.id.qiuyuantv);
                viewholder.weizhi= (TextView) view.findViewById(R.id.qiuyuanweizhi);
                viewholder.qiudui= (TextView) view.findViewById(R.id.qiuiyuanqiudui);
                viewholder.gongzi= (TextView) view.findViewById(R.id.gongzi);
                viewholder. analyseBeanicon= (ImageView) view.findViewById(R.id.qiuyuanimageView);
                viewholder.score= (TextView) view.findViewById(R.id.scoretv);
                viewholder.assist= (TextView) view.findViewById(R.id.assisttv);
                viewholder.block= (TextView) view.findViewById(R.id.blocktx);
                viewholder. rebound= (TextView) view.findViewById(R.id.reboundtv);
                viewholder.steal= (TextView) view.findViewById(R.id.stealtv);

                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }
            AnalyseBean analyseBean = analysedata.get(i);
            String analyseurl = analyseBean.getQiuyuanpicurl();
            viewholder.analyseBeanicon.setTag(i);
            Bitmap bitmap=bitmapUtils.getBitmap(analyseurl,i);
            if(bitmap!=null){
                //图片来自内存或者本地
                viewholder.analyseBeanicon.setImageBitmap(bitmap);
            }
           /* Glide
                    .with(mcontext)
                    .load(analyseBean.getQiuyuanpicurl())
                    .centerCrop()
                    .crossFade()
                    .into(viewholder.analyseBeanicon);*/
            viewholder.playername.setText(analyseBean.getName());
            viewholder.weizhi.setText(analyseBean.getWeizhi());
            viewholder.qiudui.setText(analyseBean.getQiudui());
            viewholder.gongzi.setText(analyseBean.getGongzi());
            viewholder.score.setText("得分："+analyseBean.getScore());
            viewholder.assist.setText("助攻："+analyseBean.getAssist());
            viewholder. block.setText("盖帽："+analyseBean.getBlock());
            viewholder. rebound.setText("篮板："+analyseBean.getRebound());
            viewholder.steal.setText("抢断："+analyseBean.getSteal());
            return view;
        }

    }
    static class ViewHolder {
        TextView playername, weizhi, qiudui,gongzi,score,assist,block,rebound,steal;
        ImageView analyseBeanicon;
    }
}
