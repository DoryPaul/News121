package com.example.yyz.news121.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.view.RoundImageView;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;

public class PhotoActivity extends Activity implements View.OnClickListener{

    private Button tuku,shexiangji;

    private RoundImageView usericon1;
    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;
    private Uri uritempFile;
    private String touxiangurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        tuku= (Button) findViewById(R.id.tuku);
        shexiangji= (Button) findViewById(R.id.shexiangji);
        usericon1= (RoundImageView) findViewById(R.id.usericon1);

        tuku.setOnClickListener(this);
        shexiangji.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode==CAMERA_REQUEST_CODE){
        if(data==null){
            return;
        }else {
            Bundle extras=data.getExtras();
            if(extras!=null){
                Bitmap bm=extras.getParcelable("data");
                NBAApplication.bitmap=bm;
                usericon1.setImageBitmap(bm);
                sendImage(bm);
            }
        }
    }else if(requestCode==GALLERY_REQUEST_CODE){
        if(data==null){
            return;
        }
        Uri uri;
        uri=data.getData();//从图库中获取的uri是content类型，不能直接复制
        Uri fileuri=convertUri(uri);
    }
    }

    private void sendImage(Bitmap bm) {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,60,stream);
        byte[] bytes=stream.toByteArray();
        String img=new String(Base64.encode(bytes,Base64.DEFAULT));

        RequestParams params=new RequestParams(Constants.touxiang_url);
        params.addBodyParameter("img", img);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray ja = new JSONArray(result);
                    JSONObject object = ja.getJSONObject(0);
                    touxiangurl=object.getString("filename");
                    if(touxiangurl.equals("")){
                        //Toast.makeText(PhotoActivity.this, "上传失败!", Toast.LENGTH_LONG).show();
                        Toasty.error(PhotoActivity.this, "上传失败!", Toast.LENGTH_LONG).show();
                    }else {
                        uploadtouxiangurl();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
               // Toast.makeText(PhotoActivity.this, "Upload Success!", Toast.LENGTH_LONG).show();
                Toasty.success(PhotoActivity.this, "Upload Success!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
               // Toast.makeText(PhotoActivity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
                Toasty.error(PhotoActivity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void uploadtouxiangurl() {
        RequestParams pa=new RequestParams(Constants.usertouxiang_url);
        pa.addBodyParameter("touxiangurl",touxiangurl);
        pa.addBodyParameter("userid",NBAApplication.userid);
        x.http().post(pa, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String status=result.getString("status");
                    if (status.equals("success")) {
                        //Toast.makeText(PhotoActivity.this, "头像 Success!", Toast.LENGTH_LONG).show();
                        Toasty.success(PhotoActivity.this, "头像 Success!", Toast.LENGTH_LONG).show();
                    }else {
                        //Toast.makeText(PhotoActivity.this, "头像 Fail!", Toast.LENGTH_LONG).show();
                        Toasty.error(PhotoActivity.this, "头像 Fail!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private Uri savetBitmap(Bitmap bm){
        //将图像转化并保存
        //获取要保存图像的路径
        File temDir=new File(Environment.getExternalStorageDirectory()+"com.example.yyz.news121");
        //判断路径是否存在
        if(!temDir.exists()){
            temDir.mkdir();//创建一个可用的零时文件夹

        }
        File image=new File(temDir.getAbsolutePath()+"yyz.png");
        try {
            FileOutputStream fos=new FileOutputStream(image);
            bm.compress(Bitmap.CompressFormat.PNG,85,fos);//格式，质量，文件名
            fos.flush();
            fos.close();
            return Uri.fromFile(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Uri convertUri(Uri uri){
        //转换uri
        InputStream is=null;
        try {
            is=getContentResolver().openInputStream(uri);
            Bitmap bitmap= BitmapFactory.decodeStream(is);
            is.close();
           return savetBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tuku:
                Intent tu=new Intent(Intent.ACTION_GET_CONTENT);//新建内容选择界面
                tu.setType("image/*");//设置类型
                startActivityForResult(tu,GALLERY_REQUEST_CODE);
                break;
            case R.id.shexiangji:
                Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
                break;
        }
    }
}
