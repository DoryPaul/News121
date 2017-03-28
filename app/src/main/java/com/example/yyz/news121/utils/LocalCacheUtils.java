package com.example.yyz.news121.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * appname:News121
 * Author:  应义正
 * CreateDate: 2017/3/14
 * Function:本地缓存工具类
 */
public class LocalCacheUtils {
    //内存缓存工具类
    private final MemoryCacheUtils memoryCacheUtils;
    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils=memoryCacheUtils;
    }

    //根据图片url保存图片
    public void putBitmap(String url, Bitmap bitmap) {

        try {
            //对图片路径加密，文件名称,无后坠
            String filename=MD5Encoder.encode(url);

            //创建文件
            File file=new File(Environment.getExternalStorageDirectory()+"/nbatuku",filename);
            File parentFile=file.getParentFile();//mnt/shell/emulated//nbatuku
            if (!parentFile.exists()){
                parentFile.mkdirs();//创建
            }

            //保存sd卡
            FileOutputStream os=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,os);//100为质量
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Bitmap getBitmapFromUrl(String url) {
        String fileName=null;
        try {
            fileName=MD5Encoder.encode(url);
            //创建文件
            File file=new File(Environment.getExternalStorageDirectory()+"/nbatuku",fileName);
            if(file.exists()){
                FileInputStream fis=new FileInputStream(file);
                Bitmap bitmap=BitmapFactory.decodeStream(fis);
                if(bitmap!=null){
                    memoryCacheUtils.putBitmap(url,bitmap);
                }
                fis.close();
                return  bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;

    }
}
