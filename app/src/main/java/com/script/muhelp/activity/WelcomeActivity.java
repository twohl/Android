package com.script.muhelp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.script.muhelp.R;
import com.script.muhelp.services.MuService;
import com.script.muhelp.util.String2File;
import com.script.muhelp.util.StyleUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.script.muhelp.VarPool.*;

public class WelcomeActivity extends AppCompatActivity {

    private WelcomeActivity context = this;
    private boolean logined = true;
    private Intent intent;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        moveTo();
        setStyle();
    }

    private void setStyle(){
        StyleUtil.setzhuangtailan(this);
    }
    private void moveTo(){
        Timer timer = new Timer();
        File filepath = new File(this.getFilesDir(),"users//");
        String2File.setFilePath(filepath.getPath());
        if(filepath.exists() &&
                filepath.listFiles() != null &&
                filepath.listFiles().length>0){
            username = filepath.listFiles()[0].getName();
            logined = true;
        }else{
            filepath.mkdirs();
            logined = false;
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(logined){
                    //开启一个service 用于获取数据
                    intent = new Intent(context, MuService.class);
                    intent.putExtra("username",username);
                    startService(intent);

                    intent = new Intent(context,MainActivity.class);
                    intent.putExtra("username",username);

                }else{
                    intent = new Intent(context,LoginActivity.class);
                    intent.putExtra("from",NORMAL_FROM);
                }
                context.startActivity(intent);
                context.finish();
            }
        };
        timer.schedule(task,3000);
    }
}
