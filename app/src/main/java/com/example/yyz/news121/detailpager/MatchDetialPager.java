package com.example.yyz.news121.detailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.ZhiboActivity;
import com.example.yyz.news121.base.DetailBasePager;
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

import es.dmoral.toasty.Toasty;

/**
 * Created by yyz on 2017/2/19.
 */
public class MatchDetialPager extends DetailBasePager implements AdapterView.OnItemClickListener {

    public List<MatchBean> matchdata = new ArrayList<>();
    private RefreshListView listview;
    private MatchAdapter matchAdapter;
    private ScaleAnimation sc;
    private LayoutAnimationController la;

    public MatchDetialPager(Context mcontext, List<MatchBean> matchdata) {
        super(mcontext);
        this.matchdata = matchdata;
    }

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.home_detail_pager, null);
        listview = (RefreshListView) view.findViewById(R.id.homelistview);
        listview.setDivider(mcontext.getDrawable(R.drawable.list_item));
        //显示头部出现分割线
        listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        listview.setFooterDividersEnabled(false);
     /*   sc=new ScaleAnimation(0,1,0,1);
        sc.setDuration(500);
        la=new LayoutAnimationController(sc,0.5f);*/
        //动画
        //监听控件刷新
        listview.setOnRefreshListener(new MyOnRefreshListener());
        listview.setOnItemClickListener(this);
      //  listview.setLayoutAnimation(la);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
        //得到缓存
        String saveJson = CacheUtils.getString(mcontext, Constants.match_url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        boolean islogin= NBAApplication.isLogin;
        if(islogin==true){
            /*ViewHolder viewHolder=new ViewHolder();
            viewHolder.dec= (RelativeLayout) view.findViewById(R.id.dec);
            if(viewHolder.dec.getVisibility()==View.GONE){
                viewHolder.dec.setVisibility(View.VISIBLE);
            }else {
                viewHolder. dec.setVisibility(View.GONE);
            }*/
            Intent intent=new Intent(mcontext, ZhiboActivity.class);
            intent.putExtra("matchid",matchdata.get(i-1).getMatchid());
            /*intent.putExtra("newsid",newdata.get(i-1).getNewsid());
            intent.putExtra("title",newdata.get(i-1).getTitle());
            intent.putExtra("time",newdata.get(i-1).getTime());
            intent.putExtra("desc",newdata.get(i-1).getDesc());*/
            mcontext.startActivity(intent);
        }
        else {
           // Toast.makeText(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT).show();
            Toasty.warning(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT,true).show();
        }
    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void OnPullDownRefresh() {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.match_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(mcontext, Constants.match_url, result);
                processData(result);
                listview.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("失败=" + ex.getMessage());
                listview.onRefreshFinish(false);
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
        matchdata.clear();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String matchid=object.getString("matchid");
                String teamone = object.getString("team1");
                String teamtwo = object.getString("team2");
                String teamonescore = object.getString("team1score");
                String teamtwoscore = object.getString("team2score");
                String team1icon=object.getString("team1icon");
                String team2icon=object.getString("team2icon");
                matchdata.add(new MatchBean(matchid,teamone, teamtwo, teamonescore, teamtwoscore,team1icon,team2icon));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        matchAdapter = new MatchAdapter(mcontext,matchdata);
        listview.setAdapter(matchAdapter);

    }

    class MatchAdapter extends BaseAdapter {
        private LayoutInflater minflater;
        private Context mcontext;
        private  List<MatchBean> list;
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
        public MatchAdapter(Context context, List<MatchBean> list){
            this.mcontext=context;
            this.list=list;
            minflater=LayoutInflater.from(context);
            bitmapUtils=new BitmapUtils(handler);
        }
        @Override
        public int getCount() {
            return matchdata.size();
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
                view = View.inflate(mcontext, R.layout.match_item, null);

                viewholder.team1= (TextView) view.findViewById(R.id.teamname1);
                viewholder.team2= (TextView) view.findViewById(R.id.teamname2);
                viewholder.tvbifen1= (TextView) view.findViewById(R.id.tvscore1);
                viewholder.tvbifen2= (TextView) view.findViewById(R.id.tvscore2);
                viewholder.team1icon= (ImageView) view.findViewById(R.id.team1);
                viewholder.team2icon= (ImageView) view.findViewById(R.id.team2);

                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }
            MatchBean matchBean = matchdata.get(i);
            String url1= (String) matchBean.getTeam1icon();
            viewholder.team1icon.setTag(i);
            Bitmap bitmap=bitmapUtils.getBitmap(url1,i);
            if(bitmap!=null){
                //图片来自内存或者本地
                viewholder.team1icon.setImageBitmap(bitmap);
            }

            String url2= (String) matchBean.getTeam2icon();
            viewholder.team2icon.setTag(i);
            Bitmap bitmap1=bitmapUtils.getBitmap(url2,i);
            if(bitmap1!=null){
                //图片来自内存或者本地
                viewholder.team2icon.setImageBitmap(bitmap1);
            }
           /* Glide
                    .with(mcontext)
                    .load(matchBean.getTeam1icon())
                    .centerCrop()
                    .crossFade()
                    .into(viewholder.team1icon);*/
           /* Glide
                    .with(mcontext)
                    .load(matchBean.getTeam2icon())
                    .centerCrop()
                    .crossFade()
                    .into(viewholder.team2icon);*/
            viewholder.team1.setText(matchBean.getTeam1());
            viewholder.team2.setText(matchBean.getTeam2());
            viewholder.tvbifen1.setText(matchBean.getTeam1score());
            viewholder.tvbifen2.setText(matchBean.getTeam2score());

            return view;
        }
    }

    static class ViewHolder {
        TextView team1, team2, tvbifen1,tvbifen2;
        ImageView team1icon,team2icon;
    }

}
