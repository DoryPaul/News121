package com.example.yyz.news121.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.example.yyz.news121.GuideActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**缓存类
 * Created by yyz on 2017/1/22.
 */
public class CacheUtils {
    //保存参数
    public static void putBoolean(Context context, String key, boolean values) {

        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,values).commit();
    }
    //得到保存的参数
    public static boolean getBoolean(Context context, String key) {

        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
       return sp.getBoolean(key,false);
    }


    //保存String类型的数据
    public static void putString(Context context, String key, String value) {
        if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){

            String filename= null;
            try {
                filename = MD5Encoder.encode(key);
                //创建文件
                File file=new File(Environment.getExternalStorageDirectory()+"/nbatuku",filename);
                File parentFile=file.getParentFile();
                if(!parentFile.exists()){
                    parentFile.mkdirs();
                }
                //不存在就创建，无后缀
                if(!file.exists()){
                    file.createNewFile();
                }
                FileOutputStream fos=new FileOutputStream(file);
                fos.write(value.getBytes());
                fos.flush();//刷新
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
            sp.edit().putString(key,value).commit();
        }
    }

    //得到保存的String类型的数据
    public static String getString(Context context, String key) {
        if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){

            String result="";
            String filename= null;
            try {
                filename = MD5Encoder.encode(key);
                //创建文件
                File file=new File(Environment.getExternalStorageDirectory()+"/nbatuku",filename);
               if(file.exists()){
                   byte[] buffer=new byte[1024];
                   ByteArrayOutputStream stream=new ByteArrayOutputStream();
                   FileInputStream fis=new FileInputStream(file);
                   int length;

                   while ((length=fis.read(buffer))!=-1){
                       stream.write(buffer,0,length);
                   }

                   stream.close();
                   fis.close();
                   result=stream.toString();
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }else {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            return sp.getString(key, "");
        }
    }
}
