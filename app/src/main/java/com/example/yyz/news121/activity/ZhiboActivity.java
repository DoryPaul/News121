package com.example.yyz.news121.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yyz.news121.R;
import com.example.yyz.news121.base.ZhiboBean;
import com.example.yyz.news121.bean.MatchBean;
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

public class ZhiboActivity extends Activity {

    public List<MatchBean> matchdata = new ArrayList<>();
    private List<ZhiboBean> zhibodata=new ArrayList<>();
    private ZhiboAdapter zhiboAdapter;
    private TextView zhiboneirong,zhibozhuchiren,zhibotime;
    private RefreshListView match_listview;
    private String matchid;
    private ViewHolder viewholder;
    private RelativeLayout matchzhibo;

   private Handler handler=new Handler();
   private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            handler.postDelayed(this, 30000);
            zhibo();
            getDataFromNet();
           // LogUtil.e("刷新一次");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhibo);
        initView();
        intiData();
        handler.postDelayed(runnable, 30000);//每三十秒执行一次runnable.

    }



    private void intiData() {
        Intent intent=getIntent();
        matchid=intent.getStringExtra("matchid");
      getDataFromNet();
        zhibo();
    }

    private void initView() {
        match_listview= (RefreshListView) findViewById(R.id.match_listview);
        viewholder = new ViewHolder();
        viewholder.team1= (TextView) findViewById(R.id.teamname1);
        viewholder.team2= (TextView) findViewById(R.id.teamname2);
        viewholder.tvbifen1= (TextView) findViewById(R.id.tvscore1);
        viewholder.tvbifen2= (TextView) findViewById(R.id.tvscore2);
        viewholder.team1icon= (ImageView)findViewById(R.id.team1);
        viewholder.team2icon= (ImageView) findViewById(R.id.team2);

        matchzhibo= (RelativeLayout) findViewById(R.id.matchzhibo);
        //监听控件刷新
        match_listview.setOnRefreshListener(new MyOnRefreshListener());

    }
    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void OnPullDownRefresh() {
            getDataFromNet();
            zhibo();
        }
    }


    static class ViewHolder {
        TextView team1, team2, tvbifen1,tvbifen2;
        ImageView team1icon,team2icon;
    }
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.matchbyid_url);
        params.addBodyParameter("matchid",matchid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(ZhiboActivity.this, Constants.matchbyid_url, result);
                processData(result);
              //  LogUtil.e("比赛"+matchid);
                match_listview.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
               // LogUtil.e("失败=" + ex.getMessage());
                match_listview.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
               // LogUtil.e("onCancelled" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                //LogUtil.e("onFinished");
            }
        });
    }
    private void processData(String result) {
        matchdata.clear();
         BitmapUtils bitmapUtils;

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case NetCacheUtils.SUCCESS:
                        Bitmap bitmap= (Bitmap) msg.obj;
                        int position=msg.arg1;
                        //Log.e("TAG","图片请求成功"+position);
                        if(matchzhibo!=null&&matchzhibo.isShown()){
                            ImageView imageView= (ImageView) matchzhibo.findViewWithTag(position);
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
        bitmapUtils=new BitmapUtils(handler);
        try {
            JSONArray ja = new JSONArray(result);
            bitmapUtils=new BitmapUtils(handler);
                JSONObject object = ja.getJSONObject(0);
                String matchid1=object.getString("matchid");
                String teamone = object.getString("team1");
                String teamtwo = object.getString("team2");
                String teamonescore = object.getString("team1score");
                String teamtwoscore = object.getString("team2score");
                String team1icon=object.getString("team1icon");
                String team2icon=object.getString("team2icon");
                matchdata.add(new MatchBean(matchid1,teamone, teamtwo, teamonescore, teamtwoscore,team1icon,team2icon));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MatchBean matchBean = matchdata.get(0);
        String url= (String) matchBean.getTeam1icon();
        viewholder.team1icon.setTag(0);
        Bitmap bitmap=bitmapUtils.getBitmap(url,0);
        if(bitmap!=null){
            //图片来自内存或者本地
            viewholder.team1icon.setImageBitmap(bitmap);
        }

        String url1= (String) matchBean.getTeam2icon();
        viewholder.team2icon.setTag(0);
        Bitmap bitmap1=bitmapUtils.getBitmap(url1,0);
        if(bitmap1!=null){
            //图片来自内存或者本地
            viewholder.team2icon.setImageBitmap(bitmap1);
        }
       /* Glide
                .with(ZhiboActivity.this)
                .load(matchBean.getTeam1icon())
                .centerCrop()
                .crossFade()
                .into(viewholder.team1icon);*/
       /* Glide
                .with(ZhiboActivity.this)
                .load(matchBean.getTeam2icon())
                .centerCrop()
                .crossFade()
                .into(viewholder.team2icon);*/
        viewholder.team1.setText(matchBean.getTeam1());
        viewholder.team2.setText(matchBean.getTeam2());
        viewholder.tvbifen1.setText(matchBean.getTeam1score());
        viewholder.tvbifen2.setText(matchBean.getTeam2score());

    }

    private void zhibo() {
        RequestParams params1 = new RequestParams(Constants.zhibo_url);
        params1.addBodyParameter("matchid",matchid);
        x.http().post(params1, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(ZhiboActivity.this, Constants.zhibo_url, result);
                zhiboData(result);

                match_listview.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                LogUtil.e("fail"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void zhiboData(String result) {
        zhibodata.clear();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String time = object.getString("time");
                String content = object.getString("content");
                String name = object.getString("name");
                zhibodata.add(new ZhiboBean(time, content, name));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        zhiboAdapter=new ZhiboAdapter();
        match_listview.setAdapter(zhiboAdapter);
    }

    private class ZhiboAdapter  extends BaseAdapter{
        @Override
        public int getCount() {
            return zhibodata.size();
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
            if (view == null) {
                view = View.inflate(ZhiboActivity.this, R.layout.zhibo_item, null);
                zhibotime= (TextView)view. findViewById(R.id.zhibotime);
                zhibozhuchiren= (TextView)view. findViewById(R.id.zhibozhuchiren);
                zhiboneirong= (TextView)view. findViewById(R.id.zhiboneirong);

            }
            ZhiboBean zhibobean=zhibodata.get(i);
           // LogUtil.e("success"+zhibobean.getTime());
            zhibotime.setText(zhibobean.getTime());
           zhiboneirong.setText(zhibobean.getContent());
            zhibozhuchiren.setText("主播"+zhibobean.getName()+":");
            return view;
        }
    }
}
