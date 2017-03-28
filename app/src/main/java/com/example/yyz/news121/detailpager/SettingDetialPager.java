package com.example.yyz.news121.detailpager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.HomeActivity;
import com.example.yyz.news121.activity.ManagerLoginActivity;
import com.example.yyz.news121.activity.PersonalAvtivity;
import com.example.yyz.news121.activity.PhotoActivity;
import com.example.yyz.news121.activity.RegisterActivity;
import com.example.yyz.news121.activity.TukuActivity;
import com.example.yyz.news121.base.DetailBasePager;
import com.example.yyz.news121.chat.ChatActivity;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.view.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import es.dmoral.toasty.Toasty;

/**
 * Created by yyz on 2017/2/19.
 */
public class SettingDetialPager extends DetailBasePager implements View.OnClickListener{
    private static int CAMERA_REQUIRE_CODE=1;

    private View yonghu,manager,personal,quit,linelogin,set,chat,picture;
    private boolean login;
    private TextView yonghuming,registertv,backtv;
    private Button btn_login;
    private EditText et_username, et_password;
    private RoundImageView usericon;

    public SettingDetialPager(Context mcontext) {
        super(mcontext);
    }

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.setting, null);
        yonghu=view.findViewById(R.id.yonghu);
        manager=view.findViewById(R.id.jingliren);
        personal=view.findViewById(R.id.personal);
        yonghuming= (TextView) view.findViewById(R.id.yonghuming);
        linelogin=view.findViewById(R.id.linelogin);
        quit=view.findViewById(R.id.quit);
        set=view.findViewById(R.id.set);
        chat=  view.findViewById(R.id.chat);
        registertv= (TextView) view.findViewById(R.id.registertv);
        backtv= (TextView) view.findViewById(R.id.backtv);
        btn_login = (Button) view.findViewById(R.id.linebtn);
        et_username = (EditText) view.findViewById(R.id.linename);
        et_password = (EditText)view. findViewById(R.id.linepsd);
        usericon= (RoundImageView) view.findViewById(R.id.usericon);
        picture=view.findViewById(R.id.picture);

        isLogin();

        yonghu.setOnClickListener(this);
        manager.setOnClickListener(this);
        personal.setOnClickListener(this);
        quit.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        registertv.setOnClickListener(this);
        backtv.setOnClickListener(this);
        usericon.setOnClickListener(this);
        chat.setOnClickListener(this);
        picture.setOnClickListener(this);
        return view;
    }

    private void isLogin() {
       login =NBAApplication.isLogin;
        if(login){
            yonghuming.setText(NBAApplication.username+"已登陆");
            yonghu.setVisibility(View.GONE);
            manager.setVisibility(View.GONE);
            personal.setVisibility(View.VISIBLE);
            quit.setVisibility(View.VISIBLE);
            chat.setVisibility(View.VISIBLE);
            picture.setVisibility(View.VISIBLE);

        }else {
            yonghuming.setText("未登录，请登陆");
            yonghu.setVisibility(View.VISIBLE);
            manager.setVisibility(View.VISIBLE);
            personal.setVisibility(View.GONE);
            quit.setVisibility(View.GONE);
            chat.setVisibility(View.GONE);
            picture.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        super.initData();
        if(NBAApplication.bitmap!=null){
            usericon.setImageBitmap(NBAApplication.bitmap);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backtv:
                linelogin.setVisibility(View.GONE);
                set.setVisibility(View.VISIBLE);
                break;
            case R.id.yonghu:
                linelogin.setVisibility(View.VISIBLE);
                set.setVisibility(View.GONE);
                break;
            case R.id.jingliren:
                Intent mange=new Intent(mcontext,ManagerLoginActivity.class);
                mcontext.startActivity(mange);
                break;
            case R.id.personal:
                Intent intent=new Intent(mcontext,PersonalAvtivity.class);
                mcontext.startActivity(intent);
                break;
            case R.id.quit:
               NBAApplication.isLogin=false;
               // Toast.makeText(mcontext, NBAApplication.username+"已成功退出登陆", Toast.LENGTH_SHORT).show();
                Toasty.info(mcontext, NBAApplication.username+"已成功退出登陆", Toast.LENGTH_SHORT,true).show();
                NBAApplication.username="";
                isLogin();
                break;
            case R.id.linebtn:
                clicklogin();
                break;
            case R.id.registertv:
                Intent intent1 = new Intent(mcontext, RegisterActivity.class);
                mcontext.startActivity(intent1);
                break;
            case  R.id.usericon:
                if(NBAApplication.isLogin==false){
                    //Toast.makeText(mcontext, "请先登陆再上传头像", Toast.LENGTH_SHORT).show();
                    Toasty.warning(mcontext, "请先登陆再上传头像", Toast.LENGTH_SHORT,true).show();
                }else {
                  Intent image=new Intent(mcontext, PhotoActivity.class);
                    mcontext.startActivity(image);
                }
                break;
            case R.id.chat:
                Intent chat1=new Intent(mcontext,ChatActivity.class);
                mcontext.startActivity(chat1);
                break;
            case  R.id.picture:
                Intent tuku=new Intent(mcontext, TukuActivity.class);
                mcontext.startActivity(tuku);
                break;
            default:
                break;
        }

    }

    private void clicklogin() {
        final String username = et_username.getText().toString();
        final String password = et_password.getText().toString();
        if(username.equals("") || password.equals("")) {
            //Toast.makeText(mcontext, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
            Toasty.warning(mcontext, "用户名或密码不能为空！", Toast.LENGTH_SHORT,true).show();
        }
        else {
            RequestParams requestParams = new RequestParams(Constants.login_url);
            requestParams.addBodyParameter("username", username);
            requestParams.addBodyParameter("password", password);
            x.http().post(requestParams, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                       // LogUtil.e("登陆"+result);
                        String status=result.getString("status");
                        String id=result.getString("id");
                        if (status.equals("success")) {
                            String token = result.getString("token");
                            NBAApplication.token=token;
                            NBAApplication.isLogin=true;
                            NBAApplication.username=username;
                            NBAApplication.userid=id;
                            //Toast.makeText(mcontext, username+",欢迎使用NBA客户端", Toast.LENGTH_SHORT).show();
                            Toasty.success(mcontext, username+",欢迎使用NBA客户端", Toast.LENGTH_SHORT,true).show();
                            Intent intent=new Intent(mcontext,HomeActivity.class);
                            mcontext.startActivity(intent);
                        }else {
                           //Toast.makeText(mcontext, "用户名或密码错误，请重试", Toast.LENGTH_LONG).show();
                            Toasty.error(mcontext, "用户名或密码错误，请重试", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    //Toast.makeText(mcontext, "网络请求失败，检查网络", Toast.LENGTH_SHORT).show();
                    Toasty.error(mcontext, "网络请求失败，检查网络", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                  //  LogUtil.e("失败"+cex.getMessage());
                }

                @Override
                public void onFinished() {
                    //LogUtil.e("结束");
                }
            });
        }
    }
}
