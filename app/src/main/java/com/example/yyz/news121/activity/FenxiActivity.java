package com.example.yyz.news121.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yyz.news121.R;
import com.example.yyz.news121.utils.BitmapUtils;
import com.example.yyz.news121.utils.NetCacheUtils;
import com.example.yyz.news121.utils.TitleValueEntity;
import com.example.yyz.news121.view.SpiderWebChart;

import java.util.ArrayList;
import java.util.List;

public class FenxiActivity extends Activity {
    float defen,zhugong,qiangduan,gaimao,lanban;
    String name,url;
    SpiderWebChart spiderwebchart;
    private  ImageView icon;
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
                    if(icon!=null&&icon.isShown()){
                        ImageView imageView= (ImageView) icon.findViewWithTag(position);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenxi);

       icon= (ImageView) findViewById(R.id.analyseicon);
        TextView na= (TextView) findViewById(R.id.name);
        Intent intent=getIntent();

        bitmapUtils=new BitmapUtils(handler);
        url=intent.getStringExtra("url");
        defen= Float.parseFloat(intent.getStringExtra("defen"))/3;
        zhugong= Float.parseFloat(intent.getStringExtra("zhugong"));
        qiangduan= Float.parseFloat(intent.getStringExtra("qiangduan"))*2;
        gaimao= Float.parseFloat(intent.getStringExtra("gaimao"))*5;
        lanban= Float.parseFloat(intent.getStringExtra("lanban"));
        name=intent.getStringExtra("name");
        na.setText("红色为"+name+"的能力值！");

        icon.setTag(0);
        Bitmap bitmap=bitmapUtils.getBitmap(url,0);
        if(bitmap!=null){
            //图片来自内存或者本地
            icon.setImageBitmap(bitmap);
        }
       /* Glide
                .with(this)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(icon);*/
        initSpiderWebChart();
    }

    private void initSpiderWebChart()
    {
        this.spiderwebchart = (SpiderWebChart)findViewById(R.id.spiderwebchart);

        List<TitleValueEntity> data1 = new ArrayList<TitleValueEntity>();
        data1.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title1),defen));
        data1.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title2),zhugong));
        data1.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title3),lanban));
        data1.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title4),gaimao));
        data1.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title5),qiangduan));

        List<TitleValueEntity> data2 = new ArrayList<TitleValueEntity>();
        data2.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title1),8));
        data2.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title2),7));
        data2.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title3),4));
        data2.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title4),2));
        data2.add(new TitleValueEntity(getResources().getString(R.string.spiderwebchart_title5),3));

        List<List<TitleValueEntity>> data = new ArrayList<List<TitleValueEntity>>();
        data.add(data1);
        data.add(data2);

        spiderwebchart.setData(data);
        spiderwebchart.setLatitudeNum(5);
    }
}
