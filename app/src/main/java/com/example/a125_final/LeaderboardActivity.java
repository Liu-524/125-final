package com.example.a125_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVUser;

public class LeaderboardActivity extends AppCompatActivity {

    private int score;

    private final String appid = "4MyF250OP26euKGPTFQxc0pE-MdYXbMMI";

    private final String appkey = "Qb17eoMfxfi4LpjinPUTCFS1";

    private AVUser user = new AVUser();

    private String token;

    private final String url = "https://Qb17eoMf.api.lncldglobal.com/1.1/leaderboard/users/self/statistics";

    private RequestQueue queue =  Volley.newRequestQueue(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        user.setUsername(intent.getStringExtra("username"));
        user.setPassword(intent.getStringExtra("password"));
        token = user.getSessionToken();
    }

    public void updateScore(int score) {
        JSONArray ja = new JSONArray();
        JSONObject js = new JSONObject();
        try {
            js.put("statisticName", "Flip125_Leaderboard");
            js.put("statisticValue", Integer.toString(score));
            ja.put(js);
            js = ja.toJSONObject(ja);
        } catch (Exception e) { }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, js, result -> { },
                error -> { }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-LC-Id", "4MyF250OP26euKGPTFQxc0pE-MdYXbMMI");
                params.put("X-LC-Key", "Qb17eoMfxfi4LpjinPUTCFS1");
                params.put("X-LC-Session", token);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
    }
}
