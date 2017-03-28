package com.example.yyz.news121.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yyz.news121.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import okhttp3.Call;
import okhttp3.Request;

public class TukuDetailActivity extends AppCompatActivity {
   private String name,shengao,tizhong,weizhi,playerurl,qiuduiid,shengri,gongzi;
    private ImageView qiuyuantouxiang;
    private TextView playername,playershengao,playertizhong,playerweizhi,playershengri,playergongzi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuku_detail);
        setTitle("球员信息");
        initView();
        intiData();

    }

    private void initView() {
        qiuyuantouxiang= (ImageView) findViewById(R.id.qiuyuantouxiang);
        playername= (TextView) findViewById(R.id.name);
        playergongzi= (TextView) findViewById(R.id.gongzi);
        playerweizhi= (TextView) findViewById(R.id.weizhi);
        playershengao= (TextView) findViewById(R.id.shengao);
        playertizhong= (TextView) findViewById(R.id.tizhong);
        playershengri= (TextView) findViewById(R.id.shengri);


    }

    private void intiData() {
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        shengao=intent.getStringExtra("shengao");
        tizhong=intent.getStringExtra("tizhong");
        weizhi=intent.getStringExtra("weizhi");
        playerurl=intent.getStringExtra("playerurl");
        qiuduiid=intent.getStringExtra("qiuduiid");
        shengri=intent.getStringExtra("shengri");
        gongzi=intent.getStringExtra("gongzi");

        playername.setText("球员姓名"+name);
        playergongzi.setText("球员薪资"+gongzi);
        playerweizhi.setText("球员位置"+weizhi);
        playershengao.setText("球员身高"+shengao);
        playertizhong.setText("球员体重"+tizhong);
        playershengri.setText("球员生日"+shengri);

        OkHttpUtils
                .get()//
                .url(playerurl)//
                .build()//
                .execute(new BitmapCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(Bitmap response, int id) {
                        qiuyuantouxiang.setImageBitmap(response);
                    }
                });
    }
}
