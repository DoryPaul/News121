package com.example.yyz.news121.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * appname:News121
 * Author:  应义正
 * CreateDate: 2017/3/13
 * Function:图片三级缓存工具类
 */
public class BitmapUtils {
    //网络缓存工具
    private  NetCacheUtils netCacheUtils;
    //本地缓存工具类
    private LocalCacheUtils localCacheUtils;
    //内存缓存工具类
    private  MemoryCacheUtils memoryCacheUtils;

    public  BitmapUtils(Handler handler){
        memoryCacheUtils=new MemoryCacheUtils();
        localCacheUtils=new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils=new NetCacheUtils(handler,localCacheUtils,memoryCacheUtils);
    }
    /*
    *从内存中取图片
    *
    * 从本地取图片，向内存保存
    *
    * 从网络取图片，向内存中保存，向本地保存
     */
    public Bitmap getBitmap(String url, int position) {
        //从内存中取图片
        if(memoryCacheUtils!=null){
            Bitmap bitmap=memoryCacheUtils.getBitmap(url);
            if(bitmap!=null){
                Log.e("TAG","从内存获取图片"+position);
                return bitmap;
            }
        }
        //从本地取图片
        if(localCacheUtils!=null){
            Bitmap bitmap=localCacheUtils.getBitmapFromUrl(url);
            if(bitmap!=null){
                Log.e("TAG","从本地获取图片"+position);
                return bitmap;
            }
        }
        //从网络取图片
        if(netCacheUtils!=null){
            netCacheUtils.getBitmapFromNet(url,position);
        }

        return null;
    }
}
