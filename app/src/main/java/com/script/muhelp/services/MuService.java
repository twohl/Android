package com.script.muhelp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import com.script.muhelp.ResultCode;
import com.script.muhelp.activity.LoginActivity;
import com.script.muhelp.binder.MuBinder;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.PositionUtil;
import com.script.muhelp.util.ResultUtil;
import com.script.muhelp.util.SignUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MuService extends Service {

    private String username;
    private String uuid;
    private MuBinder binder;
    private Service context = this;
    public MuService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        username = (String)intent.getStringExtra("username");

        Log.i("texst", "onStartCommand: "+username);
        uuid = SignUtil.getMyUUID(this);
        binder = new MuBinder(username,uuid);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;

    }
}
