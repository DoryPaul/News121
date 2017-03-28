package com.example.yyz.news121.detailpager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.PinglunActivity;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.bean.NewsBean;
import com.example.yyz.news121.bean.PlayerBean;
import com.example.yyz.news121.utils.BitmapUtils;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.utils.DatabaseHelper;
import com.example.yyz.news121.utils.NetCacheUtils;
import com.example.yyz.news121.view.RefreshListView;
import com.wx.goodview.GoodView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by yyz on 2017/2/19.
 */
public class HomeDetialPager extends DetailBasePager  {

    public List<NewsBean> newdata = new ArrayList<NewsBean>();
    private RefreshListView listview;
    private HomeAdapter homeAdapter;
    private  boolean zan=false;
    boolean islogin=NBAApplication.isLogin;
    private DatabaseHelper helper;
    private  List<NewsBean> list;
    private int currentposition=-1;

    public HomeDetialPager(Context mcontext, List<NewsBean> newdata) {
        super(mcontext);
        this.newdata = newdata;
        helper=new DatabaseHelper(mcontext);
    }

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.home_detail_pager, null);
        listview = (RefreshListView) view.findViewById(R.id.homelistview);
        //禁止头部出现分割线
        listview.setHeaderDividersEnabled(false);
        //禁止底部出现分割线
       listview.setFooterDividersEnabled(false);
     /*   sc=new ScaleAnimation(0,1,0,1);
        sc.setDuration(500);
        la=new LayoutAnimationController(sc,0.5f);*/
        //动画
        //监听控件刷新
        listview.setOnRefreshListener(new MyOnRefreshListener());
      //  listview.setOnItemClickListener(this);
      //  listview.setLayoutAnimation(la);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
        //得到缓存
        String saveJson = CacheUtils.getString(mcontext, Constants.nbanews_url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
    }

 /*   @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        boolean islogin=NBAApplication.isLogin;
        if(islogin==true){
            *//*ViewHolder viewHolder=new ViewHolder();
            viewHolder.dec= (RelativeLayout) view.findViewById(R.id.dec);
            if(viewHolder.dec.getVisibility()==View.GONE){
                viewHolder.dec.setVisibility(View.VISIBLE);
            }else {
                viewHolder. dec.setVisibility(View.GONE);
            }*//*
            Intent intent=new Intent(mcontext, PinglunActivity.class);
            intent.putExtra("newsid",newdata.get(i-1).getNewsid());
            intent.putExtra("title",newdata.get(i-1).getTitle());
            intent.putExtra("time",newdata.get(i-1).getTime());
            intent.putExtra("desc",newdata.get(i-1).getDesc());
            mcontext.startActivity(intent);
        }
       else {
            Toast.makeText(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT).show();
        }
    }*/


    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void OnPullDownRefresh() {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.nbanews_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(mcontext, Constants.nbanews_url, result);
                processData(result);
                listview.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
               // LogUtil.e("失败=" + ex.getMessage());
                listview.onRefreshFinish(false);
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
        newdata.clear();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String newsid=object.getString("newsid");
                String title = object.getString("title");
                String desc = object.getString("desc");
                String time = object.getString("time");
                String picurl = object.getString("picurl");
                boolean dian;
                if(islogin==true){
                    int a = queryIfZANExists(newsid, NBAApplication.userid);
                    if (a == 1) {
                      dian = true;
                    } else {
                        dian = false;
                    }
                }else {
                    dian=false;
                }
                newdata.add(new NewsBean(newsid,title, desc, time, picurl,dian));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        homeAdapter = new HomeAdapter(mcontext,list);
        listview.setAdapter(homeAdapter);

    }

    class HomeAdapter extends BaseAdapter {
        private LayoutInflater minflater;
        private Context mcontext;
        private  List<NewsBean> list;
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
        public HomeAdapter(Context context, List<NewsBean> list){
            this.mcontext=context;
            this.list=list;
            minflater=LayoutInflater.from(context);
            bitmapUtils=new BitmapUtils(handler);
        }
        @Override
        public int getCount() {
            return newdata.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder viewholder;
            final int j=i;
            if (view == null) {
                viewholder = new ViewHolder();
                view = View.inflate(mcontext, R.layout.news_item, null);
                viewholder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                viewholder.ivpic = (ImageView) view.findViewById(R.id.ivPic);
                viewholder.news_main= (LinearLayout) view.findViewById(R.id.news_main);
                viewholder.ll_comment_bottom= (LinearLayout) view.findViewById(R.id.ll_comment_bottom);
                viewholder.ll_like_bottom= (LinearLayout) view.findViewById(R.id.ll_like_bottom);
                viewholder.iv_like_bottom= (ImageView) view.findViewById(R.id.iv_like_bottom);
                viewholder.pinglunlinear= (LinearLayout) view.findViewById(R.id.pinglunlinear);
                viewholder.news_editText= (EditText) view.findViewById(R.id.news_editText);
                viewholder.news_button= (ImageButton) view.findViewById(R.id.news_button);

                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }

            NewsBean newsBean = newdata.get(i);
            String url= (String) newsBean.getPicurl();
            viewholder.ivpic.setTag(i);
            Bitmap bitmap=bitmapUtils.getBitmap(url,i);
            if(bitmap!=null){
                //图片来自内存或者本地
                viewholder.ivpic.setImageBitmap(bitmap);
            }
           /* Glide
                    .with(mcontext)
                    .load(newsBean.getPicurl())
                    .centerCrop()
                    .crossFade()
                    .into(viewholder.ivpic);*/
            viewholder.tvTitle.setText(newsBean.getTitle());
            viewholder.tvTitle.setText(newsBean.getTitle());
            if(newsBean.isDian()){
                viewholder.iv_like_bottom.setImageResource(R.drawable.timeline_icon_like);
                viewholder.ll_like_bottom.setEnabled(false);
                zan=newsBean.isDian();
            }
           // LogUtil.e("shipeiqi" + newsBean.getDesc());

            viewholder.news_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(islogin==true){
            /*ViewHolder viewHolder=new ViewHolder();
            viewHolder.dec= (RelativeLayout) view.findViewById(R.id.dec);
            if(viewHolder.dec.getVisibility()==View.GONE){
                viewHolder.dec.setVisibility(View.VISIBLE);
            }else {
                viewHolder. dec.setVisibility(View.GONE);
            }*/
                        Intent intent=new Intent(mcontext, PinglunActivity.class);
                        intent.putExtra("newsid",newdata.get(j).getNewsid());
                        intent.putExtra("title",newdata.get(j).getTitle());
                        intent.putExtra("time",newdata.get(j).getTime());
                        intent.putExtra("desc",newdata.get(j).getDesc());
                        mcontext.startActivity(intent);
                    }
                    else {
                      //  Toast.makeText(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT).show();
                        Toasty.warning(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT,true).show();
                    }
                }
            });

            viewholder.ll_comment_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(islogin==true) {
                        viewholder.pinglunlinear.setVisibility(View.VISIBLE);
                    }else {
                       // Toast.makeText(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT).show();
                        Toasty.warning(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT,true).show();
                    }
                }
            });

           /* if(islogin==true){
                for(int b=0;b<newdata.size();b++) {
                    int a = queryIfZANExists(newdata.get(b).getNewsid(), NBAApplication.userid);
                    if (a == 1) {
                        zan = true;
                        viewholder.iv_like_bottom.setImageResource(R.drawable.timeline_icon_like);
                        viewholder.ll_like_bottom.setEnabled(false);

                    } else {
                        zan = false;
                    }
                }
            }*/
            final GoodView goodView = new GoodView(mcontext);
            viewholder.ll_like_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(islogin==true) {
                        viewholder.iv_like_bottom.setImageResource(R.drawable.timeline_icon_like);
                        viewholder.ll_like_bottom.setEnabled(false);
                        goodView.setText("+1");
                        goodView.show(view);
                        zan=true;
                        if(zan==true) {
                            SQLiteDatabase db=helper.getWritableDatabase();
                            db.beginTransaction();
                            ContentValues values = new ContentValues();
                            values.put("news_id", newdata.get(j).getNewsid());
                            values.put("yonghu_id", NBAApplication.userid);
                            values.put("zan",1);
                            db.insert("Zan", null, values);
                            values.clear();
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    }else {
                       // Toast.makeText(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT).show();
                        Toasty.warning(mcontext, "请登录以便获得更好的服务！", Toast.LENGTH_SHORT,true).show();
                    }
                }
            });
            viewholder.news_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ne= viewholder.news_editText.getText().toString();
                    if(ne.equals("")){
                        //Toast.makeText(mcontext, "请输入内容", Toast.LENGTH_SHORT).show();
                        Toasty.warning(mcontext, "请输入内容！", Toast.LENGTH_SHORT,true).show();
                    }else {
                        RequestParams pa=new RequestParams(Constants.pinglunsubmit_url);
                        pa.addBodyParameter("neirong",ne);
                        pa.addBodyParameter("yonghuid",NBAApplication.userid.toString());
                        pa.addBodyParameter("newsid",newdata.get(j).getNewsid());
                        x.http().post(pa, new Callback.CommonCallback<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject result) {

                                try {

                                    String status=result.getString("status");
                                    if (status.equals("success")) {;
                                      //  Toast.makeText(mcontext, "发表评论成功", Toast.LENGTH_SHORT).show();
                                        Toasty.success(mcontext, "发表评论成功！", Toast.LENGTH_SHORT,true).show();
                                        getDataFromNet();
                                    }else {
                                       // Toast.makeText(mcontext, "发表评论失败", Toast.LENGTH_LONG).show();
                                        Toasty.error(mcontext, "发表评论失败！", Toast.LENGTH_SHORT,true).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                               // LogUtil.e("错误"+ex.getMessage());
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {
                               // LogUtil.e("错误"+cex.getMessage());
                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                }
            });
            return view;
        }
    }

     private int queryIfZANExists(String newsid, String userid) {
            SQLiteDatabase db=helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from Zan where news_id=? and yonghu_id=?", new String[]{newsid, userid});
            if (cursor!=null&&cursor.moveToFirst()) {
                int zan=cursor.getInt(cursor.getColumnIndex("zan"));
                cursor.close();
                return zan;
            }else {
                cursor.close();
                return  0;
            }
    }

    static class ViewHolder {
        TextView tvTitle;
        EditText news_editText;
        ImageButton news_button;
        ImageView ivpic,iv_like_bottom;
        LinearLayout news_main,ll_comment_bottom,ll_like_bottom,pinglunlinear;
    }

}
