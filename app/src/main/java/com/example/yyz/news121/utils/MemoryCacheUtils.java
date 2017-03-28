package com.example.yyz.news121.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * appname:News121
 * Author:  应义正
 * CreateDate: 2017/3/14
 * Function:内存缓存的工具类
 */
public class MemoryCacheUtils {
    private LruCache<String ,Bitmap> lruCache;
    public  MemoryCacheUtils(){
        //得到最大内存的八分之一用于缓存图片
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/1024/8);
        //设置缓存图片的空间
        lruCache=new LruCache<String ,Bitmap>(maxSize){
            //计算每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return  (value.getRowBytes()*value.getHeight())/1024;//返回单位为KB
            }
        };
    }
    //根据url保存图片到内存中
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url,bitmap);
    }
    //根据url获取在内存中缓存的图片
    public Bitmap getBitmap(String url){
        return  lruCache.get(url);
    }
}
