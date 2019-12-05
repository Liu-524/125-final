package com.example.a125_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView name = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        TextView rpw = findViewById(R.id.rPassword);
        Button login = findViewById(R.id.login);
        Button signup = findViewById(R.id.signup);
        login.setOnClickListener(v -> {
            String userName = name.getText().toString();
            String pwd = password.getText().toString();
            AVUser.logIn(userName, pwd).subscribe((new Observer<AVUser>() {
                public void onSubscribe(Disposable disposable) { }
                public void onNext(AVUser user) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("login_info", true);
                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("password", user.getPassword());
                    startActivity(intent);
                    finish();
                }
                public void onError(Throwable throwable) {
                    password.clearComposingText();
                }
                public void onComplete() { }
            }));
        });
        signup.setOnClickListener(v -> {
            System.out.println("signup!");
            String userName = name.getText().toString();
            String pwd = password.getText().toString();
            String pwd2 = rpw.getText().toString();
            if (!pwd.equals(pwd2)) {
                System.out.println("wait!");
                rpw.clearComposingText();
            } else {
                AVUser user = new AVUser();
                user.setUsername(userName);
                user.setPassword(pwd);
                user.signUpInBackground().subscribe(new Observer<AVUser>() {
                    public void onSubscribe(Disposable disposable) {}
                    public void onNext(AVUser user) {
                        System.out.println("here");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("login_info", true);
                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("password", user.getPassword());
                        startActivity(intent);
                        finish();
                    }
                    public void onError(Throwable throwable) {
                        name.clearComposingText();
                    }
                    public void onComplete() {}
                });
            }
        });



    }
}
