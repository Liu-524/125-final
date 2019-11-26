package com.example.a125_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVUser;

public class LeaderboardActivity extends AppCompatActivity {

    private int score;

    private final String appid = "4MyF250OP26euKGPTFQxc0pE-MdYXbMMI";

    private final String appkey = "Qb17eoMfxfi4LpjinPUTCFS1";

    private AVUser user = new AVUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        user.setUsername(intent.getStringExtra("username"));
        user.setPassword(intent.getStringExtra("password"));

    }
}
