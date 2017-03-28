package com.example.yyz.news121.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yyz.news121.R;

public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setTitle("聊天中");
    }
}
