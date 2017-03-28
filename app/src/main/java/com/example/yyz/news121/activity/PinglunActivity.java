package com.example.yyz.news121.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.bean.PinglunBean;
import com.example.yyz.news121.detailpager.HomeDetialPager;
import com.example.yyz.news121.utils.CacheUtils;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.utils.DatabaseHelper;
import com.example.yyz.news121.view.RefreshListView;
import com.wx.goodview.GoodView;

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

public class PinglunActivity extends Activity {

    private List<PinglunBean> pinglundata=new ArrayList<>();
    private PinglunAdapter pinglunAdapter;
    String status;
    private  boolean zan1;
    private RefreshListView newsrefresh;
    private TextView news_title, news_time, news_neirong;
    //private EditText news_editText;
    //private Button news_button;
    private DatabaseHelper helper;
    private String title,newsid,time,desc;
    private LinearLayout ll_comment_bottom,ll_like_bottom,pinglunlinear;
    private ImageButton news_button;
    private  EditText news_editText;
    private  ImageView iv_like_bottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinglun);
        helper=new DatabaseHelper(this);
        intiView();
        initData();
    }


    private void intiView() {
        newsrefresh = (RefreshListView) findViewById(R.id.newsrefresh);
        //显示头部出现分割线
        newsrefresh.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        newsrefresh.setFooterDividersEnabled(false);
        View topView = View.inflate(PinglunActivity.this, R.layout.news_top_view, null);
        //View footView = View.inflate(PinglunActivity.this, R.layout.news_foot_view, null);
        newsrefresh.addHeaderView(topView);
       // newsrefresh.addFooterView(footView);

        news_title = (TextView) findViewById(R.id.news_title);
        news_time = (TextView) findViewById(R.id.news_time);
        news_neirong = (TextView) findViewById(R.id.news_neirong);
       // news_editText = (EditText) findViewById(R.id.news_editText);
        //news_button = (Button) findViewById(R.id.news_button);

     ll_comment_bottom= (LinearLayout)findViewById(R.id.ll_comment_bottom);
      ll_like_bottom= (LinearLayout)findViewById(R.id.ll_like_bottom);
        iv_like_bottom= (ImageView)findViewById(R.id.iv_like_bottom);
       pinglunlinear= (LinearLayout)findViewById(R.id.pinglunlinear);
      news_editText= (EditText)findViewById(R.id.news_editText);
      news_button= (ImageButton)findViewById(R.id.news_button);


        newsrefresh.setOnRefreshListener(new MyOnRefreshListener());


    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {
        @Override
        public void OnPullDownRefresh() {
            getDataFromNet();
        }
    }

    private void initData() {

        Intent intent=getIntent();
        time=intent.getStringExtra("time");
        title=intent.getStringExtra("title");
        newsid=intent.getStringExtra("newsid");
        desc=intent.getStringExtra("desc");

        int a = queryIfZANExists(newsid, NBAApplication.userid);
        if (a == 1) {
            zan1 = true;
        } else {
            zan1 = false;
        }



        news_time.setText(time);
        news_title.setText(title);
        news_neirong.setText("    "+"    "+desc);
        //得到缓存
        String saveJson = CacheUtils.getString(this, Constants.pinglun_url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();

        if(zan1==false){
            ll_like_bottom.setEnabled(true);
        }else {
            iv_like_bottom.setImageResource(R.drawable.timeline_icon_like);
            ll_like_bottom.setEnabled(false);
        }


        ll_comment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinglunlinear.setVisibility(View.VISIBLE);
            }
        });
        final GoodView goodView = new GoodView(PinglunActivity.this);
       ll_like_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    iv_like_bottom.setImageResource(R.drawable.timeline_icon_like);
                 ll_like_bottom.setEnabled(false);
                    goodView.setText("+1");
                    goodView.show(view);
                zan1=true;
                if(zan1==true) {
                    SQLiteDatabase db=helper.getWritableDatabase();
                    db.beginTransaction();
                    ContentValues values = new ContentValues();
                    values.put("news_id", newsid);
                    values.put("yonghu_id", NBAApplication.userid);
                    values.put("zan",1);
                    db.insert("Zan", null, values);
                    values.clear();
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                }
            }
        });
    news_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ne=news_editText.getText().toString();
                if(ne.equals("")){
                   // Toast.makeText(PinglunActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    Toasty.info(PinglunActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }else {


                    RequestParams pa=new RequestParams(Constants.pinglunsubmit_url);
                    pa.addBodyParameter("neirong",ne);
                    pa.addBodyParameter("yonghuid",NBAApplication.userid.toString());
                    pa.addBodyParameter("newsid",newsid);
                    x.http().post(pa, new Callback.CommonCallback<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {

                            try {

                                String status=result.getString("status");
                                if (status.equals("success")) {;
                                   // Toast.makeText(PinglunActivity.this, "发表评论成功", Toast.LENGTH_SHORT).show();
                                    Toasty.success(PinglunActivity.this, "发表评论成功", Toast.LENGTH_SHORT).show();
                                    pinglunlinear.setVisibility(View.GONE);
                                    getDataFromNet();
                                }else {
                                    //Toast.makeText(PinglunActivity.this, "发表评论失败", Toast.LENGTH_LONG).show();
                                    Toasty.error(PinglunActivity.this, "发表评论失败", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            //LogUtil.e("错误"+ex.getMessage());
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                          //  LogUtil.e("错误"+cex.getMessage());
                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
            }
        });
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


    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.pinglun_url);
        params.addBodyParameter("newsid", newsid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("null")){
                   // Toast.makeText(PinglunActivity.this, "无此数据，请重新查询", Toast.LENGTH_SHORT).show();
                    Toasty.error(PinglunActivity.this, "无此数据，请重新查询", Toast.LENGTH_SHORT).show();
                }else {
                    CacheUtils.putString(PinglunActivity.this, Constants.pinglun_url, result);
                    processData(result);
                    newsrefresh.onRefreshFinish(true);
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
    private void processData(String result) {
        pinglundata.clear();
        JSONArray ja = null;
        try {
            ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                String neirong=object.getString("neirong");
                String username=object.getString("username");
                pinglundata.add(new PinglunBean(neirong,username));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pinglunAdapter=new PinglunAdapter();
        newsrefresh.setAdapter(pinglunAdapter);
    }
    static class ViewHolder {
        TextView item_username, item_neirong;

    }
    private class PinglunAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return pinglundata.size();
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
                view = View.inflate(PinglunActivity.this, R.layout.pinglun_item, null);

                viewholder.item_neirong= (TextView) view.findViewById(R.id.item_neirong);
                viewholder.item_username= (TextView) view.findViewById(R.id.item_username);
                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }
            PinglunBean pinglunbean=pinglundata.get(i);
            viewholder.item_username.setText(pinglunbean.getUsername()+":");
            viewholder.item_neirong.setText(pinglunbean.getNeirong());
            return view;
        }
    }

}