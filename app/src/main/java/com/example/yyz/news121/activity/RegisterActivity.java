package com.example.yyz.news121.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends Activity {
    private EditText etName,etPwd,etEnsure;
    private ActionProcessButton bt,bt1;
    private String iden="yonghu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName=(EditText)findViewById(R.id.etname);
        etPwd=(EditText)findViewById(R.id.etpwd);
        etEnsure=(EditText)findViewById(R.id.etpwd2);

        bt= (ActionProcessButton) findViewById(R.id.zhucebt);
        bt1= (ActionProcessButton) findViewById(R.id.backbt);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etName.getText().toString();
                final String password = etPwd.getText().toString();
                final String password1 = etEnsure.getText().toString();

                if (username.equals("") || password.equals("")) {
                   // Toast.makeText(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                    Toasty.info(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                } else if (!password.equals(password1)) {
                  //  Toast.makeText(RegisterActivity.this, "两次密码不相同，请重新输入", Toast.LENGTH_LONG).show();
                    Toasty.warning(RegisterActivity.this, "两次密码不相同，请重新输入", Toast.LENGTH_LONG).show();
                    etPwd.setText("");
                    etEnsure.setText("");
                } else {
                    RequestParams params=new RequestParams(Constants.register_url);
                    params.addBodyParameter("username", username);
                    params.addBodyParameter("password", password);
                    params.addBodyParameter("identity",iden);
                    x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String status=result.getString("status");
                                String token=result.getString("token");
                                NBAApplication.token=token;
                                if (status.equals("exists")) {
                                   // Toast.makeText(RegisterActivity.this, "用户名已存在，请更换", Toast.LENGTH_LONG).show();
                                    Toasty.warning(RegisterActivity.this, "用户名已存在，请更换", Toast.LENGTH_LONG).show();
                                } else if (status.equals("error")) {
                                   // Toast.makeText(RegisterActivity.this, "出现错误，请稍后重试", Toast.LENGTH_LONG).show();
                                    Toasty.warning(RegisterActivity.this, "出现错误，请稍后重试", Toast.LENGTH_LONG).show();
                                } else if (status.equals("success")) {
                                   // Toast.makeText(RegisterActivity.this, username+"欢迎成为NBA的一员！", Toast.LENGTH_SHORT).show();
                                    Toasty.success(RegisterActivity.this, username+"欢迎成为NBA的一员！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    RegisterActivity.this.finish();
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
            }
        });
    }
}
