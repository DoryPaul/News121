package com.example.yyz.news121.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import es.dmoral.toasty.Toasty;

public class PersonalAvtivity extends Activity implements View.OnClickListener {
    private EditText changeetName, changeetPwd, changeetEnsure;
    private TextView personname;
    private Button changebt;
    private String status;
    String result1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_avtivity);
        initView();
        initData();

    }


    private void initView() {
        personname = (TextView) findViewById(R.id.personname);
        changeetName = (EditText) findViewById(R.id.changeetname);
        changeetPwd= (EditText) findViewById(R.id.changeetpwd);
        changeetEnsure= (EditText) findViewById(R.id.changeetpwd2);
        changebt= (Button) findViewById(R.id.chnagebt);

        personname.setText(NBAApplication.username);
        changeetName.setText(NBAApplication.username);
    }

    private void initData() {
        changebt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chnagebt:
                getDataFromNet();
                break;
            default:
                break;
        }

    }

    private void getDataFromNet() {
        final String password = changeetPwd.getText().toString();
        final String password1 = changeetEnsure.getText().toString();

        if (password1.equals("") || password.equals("")) {
          //  Toast.makeText(PersonalAvtivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
            Toasty.warning(PersonalAvtivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
        }else if (password.equals(password1)) {
           // Toast.makeText(PersonalAvtivity.this, "两次密码相同，请重新输入", Toast.LENGTH_LONG).show();
            Toasty.warning(PersonalAvtivity.this, "两次密码相同，请重新输入", Toast.LENGTH_LONG).show();
            changeetPwd.setText("");
            changeetEnsure.setText("");
        }else {
            RequestParams params=new RequestParams(Constants.change_url);
            params.addBodyParameter("username", NBAApplication.username.toString());
            params.addBodyParameter("password", password);
            params.addBodyParameter("password1", password1);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String json = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
                    processData(json);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    LogUtil.e(result1);
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

    private void processData(String result) {
        try {
            result1=result;
            JSONObject object = new JSONObject(result);
              status=object.getString("status");
            if (status.equals("success")) {
                LogUtil.e(result);
                //Toast.makeText(PersonalAvtivity.this, "更改密码成功！", Toast.LENGTH_LONG).show();
                Toasty.success(PersonalAvtivity.this, "更改密码成功！", Toast.LENGTH_LONG).show();
            }else{
                LogUtil.e(result);
                //Toast.makeText(PersonalAvtivity.this, "更改密码失败！", Toast.LENGTH_LONG).show();
                Toasty.error(PersonalAvtivity.this, "更改密码失败！", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
