package com.example.a125_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

    private final String boardUrl = "https://Qb17eoMf.api.lncldglobal.com/1.1/leaderboard/leaderboards/" +
            "Flip125_Leaderboard/ranks?maxResultsCount=50";

    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        user.setUsername(intent.getStringExtra("username"));
        user.setPassword(intent.getStringExtra("password"));
        token = AVUser.getCurrentUser().getSessionToken();
        System.out.println(token);
        updateScore();
    }

    private void updateScore() {
        JSONArray ja = new JSONArray();
        JSONObject js = new JSONObject();
        try {
            js.put("statisticName", "Flip125_Leaderboard");
            js.put("statisticValue", 1000);
            ja.put(js);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, ja, result -> {
            System.out.println("hahahahahahahahahhahahahahahahahahahhahahahahahha");
        },
                error -> {
            System.out.println(error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-LC-Id", "4MyF250OP26euKGPTFQxc0pE-MdYXbMMI");
                params.put("X-LC-Key", "Qb17eoMfxfi4LpjinPUTCFS1");
                params.put("X-LC-Session", token);
                params.put("Content-Type", "application/json");
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(request);
    }

    private JSONObject getLeaderBoard() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, boardUrl, null, r -> {
            System.out.println("success getting board");
            /* do some thing*/
            /* Only care about the username, score and their rank, I didn't do the rest when signing in.
            *sample result:
            * {
                "results": [
                {
                  "user": {
                    "objectId": "5ab9f63910b03545c0f529df",
                    "__type": "Pointer",
                    "username": "Superman",
                    "avatar_url": "http://example.com/avatar.png",
                    "className": "_User"
                  },
                  "rank": 0,
                  "statisticName": "wins",
                  "statisticValue": 42,
                  "statistics": [{
                    "statisticName": "world",
                    "statisticValue": null,
                    "version": null,
                  }]
                }, {
                  "user": {},
                  "rank": 1,
                } ...
                ]
              }
             */
        }, error -> {
            System.out.println("error in getboard");
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-LC-Id", "4MyF250OP26euKGPTFQxc0pE-MdYXbMMI");
                params.put("X-LC-Key", "Qb17eoMfxfi4LpjinPUTCFS1");
                return params;
            }
        };
        queue.add(request);
        return null;
    }
}
