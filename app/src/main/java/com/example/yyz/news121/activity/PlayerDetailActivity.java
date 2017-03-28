package com.example.yyz.news121.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yyz.news121.R;

import es.dmoral.toasty.Toasty;

public class PlayerDetailActivity extends Activity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibBack;
    private ImageButton ibTextsize;
    private ImageButton ibShare;

    /**
     * 1.自定义可以做浏览器
     * 2.在WebView中播放直播视频
     * 3.解析电脑网页或者手机网页（H5）
     * 4.通过WebView这个控件，实现Java代码和javascript代码交互
     * 5.Android 4.4以下用的内核是webkit
     */
    private WebView webview;
    private ProgressBar pbStatus;
    /*网络地址*/
    private String url;
    private int temp=2;
    private int textSize=temp;
    private WebSettings webSettings;

    private void findViews() {
        setContentView(R.layout.activity_news_detail);
        tvTitle = (TextView)findViewById( R.id.tv_title );
        ibMenu = (ImageButton)findViewById( R.id.ib_menu );
        ibBack = (ImageButton)findViewById( R.id.ib_back );
        ibTextsize = (ImageButton)findViewById( R.id.ib_textsize );
        ibShare = (ImageButton)findViewById( R.id.ib_share );
        webview = (WebView)findViewById( R.id.webview );
        pbStatus = (ProgressBar)findViewById( R.id.pb_status );

        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        ibBack.setOnClickListener( this );
        ibTextsize.setOnClickListener( this );
        ibShare.setOnClickListener( this );
    }
    @Override
    public void onClick(View v) {
        if ( v == ibBack ) {
            // Handle clicks for ibBack
            finish();
        } else if ( v == ibTextsize ) {
            // Handle clicks for ibTextsize
           // Toast.makeText(PlayerDetailActivity.this, "设置文字大小", Toast.LENGTH_SHORT).show();
           showChangTextSizeDialog();
        } else if ( v == ibShare ) {
            // Handle clicks for ibShare
           // Toast.makeText(PlayerDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
            Toasty.info(PlayerDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
        }
    }



    private void showChangTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = new String[]{"超大字体","大字体","正常","小字体","超小字体"};
        builder.setSingleChoiceItems(items, textSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                temp = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textSize = temp;
                //改变字体大小
                changeTextSize();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 改变文字的大小
     */
    private void changeTextSize() {
        switch (textSize){
            case 0:
                webSettings.setTextZoom(200);
                break;
            case 1:
                webSettings.setTextZoom(150);
                break;
            case 2:
                webSettings.setTextZoom(100);
                break;
            case 3:
                webSettings.setTextZoom(75);
                break;
            case 4:
                webSettings.setTextZoom(50);
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        url = getIntent().getStringExtra("url");
         webSettings=webview.getSettings();
        //设置支持javascript
        webSettings.setJavaScriptEnabled(true);

        //设置双击变大变小
        webSettings.setUseWideViewPort(true);

        webSettings.setTextZoom(100);
        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbStatus.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(url);
    }


}
