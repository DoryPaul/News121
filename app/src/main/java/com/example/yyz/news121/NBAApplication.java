package com.example.yyz.news121;

import android.app.Application;
import android.graphics.Bitmap;

import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * Created by yyz on 2017/1/28.
 */
public class NBAApplication extends Application{
    public static boolean isLogin = false;
    public static String username = "";
    public static String userid = "";
    public static String usericonurl = "";
   public static Bitmap bitmap;
    public static String token = "";

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);

         /*init rongcloud*/
        RongIM.init(this);
    }
}
