package com.example.yyz.news121.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Button btn_add = (Button) findViewById(R.id.bt_add);
        final EditText et_username = (EditText) findViewById(R.id.friendeditText);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                if (!username.equals("")) {
                    org.xutils.http.RequestParams params = new org.xutils.http.RequestParams(Constants.addfriend_url);
                    params.addBodyParameter("username", NBAApplication.username);
                    params.addBodyParameter("target", username);
                    x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String status = result.getString("status");
                                if (status.equals("success")) {
                                    Toast.makeText(AddFriendActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                                    AddFriendActivity.this.finish();
                                } else {
                                    Toast.makeText(AddFriendActivity.this, "用户名不存在", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(AddFriendActivity.this, "网络错误，请稍后重试", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            Toast.makeText(AddFriendActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
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

