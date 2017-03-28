package com.example.yyz.news121.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * appname:News121
 * Author:  应义正
 * CreateDate: 2017/3/13
 * Function:网络缓存工具
 */
public class NetCacheUtils {
    /**
     * 请求图片成功
     */
    public static final int SUCCESS = 1;
    /**
     * 请求图片失败
     */
    public static final int FAIL = 2;
    private  final Handler handler;

    //本地缓存工具类
    private final LocalCacheUtils localCacheUtils;
    //内存缓存工具类
    private final MemoryCacheUtils memoryCacheUtils;
    private ExecutorService service;
    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.handler=handler;
       service= Executors.newFixedThreadPool(10);
        this.localCacheUtils=localCacheUtils;
        this.memoryCacheUtils=memoryCacheUtils;
    }

    public void getBitmapFromNet(String url, int position) {

       //new Thread(new MyRunnable(url,position)).start();
        service.execute(new MyRunnable(url,position));

    }
    class MyRunnable implements  Runnable{
        private  final  String url;
        private final int postion;

        MyRunnable(String url, int position) {
            this.url = url;
            this.postion=position;
        }

        @Override
        public void run() {
            //联网请求图片
            try {
                HttpURLConnection conn= (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");//一定要大写
                conn.setConnectTimeout(5000);//链接超时
                conn.setReadTimeout(5000);//读取超时
                conn.connect();//链接
                int code=conn.getResponseCode();//响应码
                if(code==200){//链接成功
                    InputStream is=conn.getInputStream();//得到输入流
                  Bitmap bitmap= BitmapFactory.decodeStream(is);

                    //在内存中保存一份
                 memoryCacheUtils.putBitmap(url,bitmap);
                    //在本地保存一份
                    localCacheUtils.putBitmap(url,bitmap);

                    is.close();
                    //发消息到主线程中，bitmap和position
                    Message msg=Message.obtain();
                    msg.obj=bitmap;
                    msg.arg1=postion;
                    msg.what=SUCCESS;
                    handler.sendMessage(msg);

                }
            } catch (IOException e) {
                e.printStackTrace();
                Message msg=Message.obtain();
                msg.arg1=postion;
                msg.what=FAIL;
                handler.sendMessage(msg);
            }
        }
    }
}
