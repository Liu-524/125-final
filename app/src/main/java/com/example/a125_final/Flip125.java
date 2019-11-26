package com.example.a125_final;

import android.app.Application;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;

public class Flip125 extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        AVOSCloud.initialize(this, "4MyF250OP26euKGPTFQxc0pE-MdYXbMMI"
                , "Qb17eoMfxfi4LpjinPUTCFS1");
        System.out.println(AVOSCloud.getApplicationId() + "--------------------------------------------------------------" );
    }
}
