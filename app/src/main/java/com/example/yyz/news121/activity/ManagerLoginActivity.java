package com.example.yyz.news121.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.utils.Constants;
import com.example.yyz.news121.utils.ProgressGenerator;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import es.dmoral.toasty.Toasty;

public class ManagerLoginActivity extends Activity implements ProgressGenerator.OnCompleteListener {
    private ActionProcessButton bt;
    private TextView mtx1, mtx2;
    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        final ProgressGenerator progressGenerator = new ProgressGenerator(this);
        bt = (ActionProcessButton) findViewById(R.id.managerlogin);
        mtx1 = (TextView) findViewById(R.id.manager_name);
        mtx2 = (TextView) findViewById(R.id.manager_pwd);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getBoolean(EXTRAS_ENDLESS_MODE)) {
            bt.setMode(ActionProcessButton.Mode.ENDLESS);
        } else {
            bt.setMode(ActionProcessButton.Mode.PROGRESS);
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = mtx1.getText().toString();
                final String password = mtx2.getText().toString();
                if (username.equals("") || password.equals("")) {
                    //Toast.makeText(ManagerLoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                    Toasty.info(ManagerLoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    RequestParams params = new RequestParams(Constants.manlogin_url);
                    params.addBodyParameter("username", username);
                    params.addBodyParameter("password", password);

                    x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String status = result.getString("status");

                                if (status.equals("success")) {
                                    String token = result.getString("token");
                                    NBAApplication.token=token;
                                    NBAApplication.isLogin=true;
                                    progressGenerator.start(bt);

                                } else {
                                    //Toast.makeText(ManagerLoginActivity.this, "用户名或密码错误，请重试", Toast.LENGTH_LONG).show();
                                    Toasty.warning(ManagerLoginActivity.this, "用户名或密码错误，请重试", Toast.LENGTH_LONG).show();
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

    @Override
    public void onComplete() {
        Intent intent = new Intent(ManagerLoginActivity.this, ManagerMainActivity.class);
        startActivity(intent);
        finish();
    }
}
