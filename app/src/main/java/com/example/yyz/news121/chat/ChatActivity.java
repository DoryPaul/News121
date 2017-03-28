package com.example.yyz.news121.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yyz.news121.NBAApplication;
import com.example.yyz.news121.R;
import com.example.yyz.news121.activity.ManagerLoginActivity;
import com.example.yyz.news121.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


public class ChatActivity extends AppCompatActivity {
    BaseAdapter adapter=null;
    List<String> list=new ArrayList<String>();//存放好友信息

    @Override
    protected void onPostResume() {
        refreshFriendList();
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_friend) {
            Intent intent = new Intent(ChatActivity.this, AddFriendActivity.class);
            startActivity(intent);
            return true;
        }else if(id==R.id.refresh_friend){
            refreshFriendList();
        }else if(id==R.id.conversationlist){
            RongIM.getInstance().startConversationList(ChatActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RongIM.init(this);
        if(NBAApplication.isLogin) {
            RongIM.connect(NBAApplication.token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Toast.makeText(ChatActivity.this, "Token incorrect", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(String s) {
                    Toast.makeText(ChatActivity.this, "Login Success " + s, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Intent intent = new Intent(ChatActivity.this, ManagerLoginActivity.class);
            startActivity(intent);
            this.finish();
        }


        final ListView ls= (ListView) findViewById(R.id.friendlistView);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String target=(String)ls.getAdapter().getItem(position);
                RongIM.getInstance().startPrivateChat(ChatActivity.this,target,null);
            }
        });

    }


    private  void refreshFriendList(){
        org.xutils.http.RequestParams params = new org.xutils.http.RequestParams(Constants.getFriendList_url);
        params.addBodyParameter("username", NBAApplication.username);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array=new JSONArray(result);
                    list.clear();
                    for(int j=0;j<array.length();j++){
                        list.add((String)array.get(j));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ChatActivity.this,"网络错误，请重试！",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(ChatActivity.this,"网络错误，请重试！",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                //Toast.makeText(ChatActivity.this,"网络错误，请重试！",Toast.LENGTH_LONG).show();
            }
        });
    }
}