package com.script.muhelp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.entity.User;
import com.script.muhelp.util.String2File;
import com.script.muhelp.util.StyleUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by hongl on 2018/2/23.
 */

public class UserInfoActivity extends AppCompatActivity {
    private Button sendMessage;
    private Context context = this;
    private TextView nickname;
    private TextView gender;
    private TextView sign;
    private TextView address;
    private TextView hobby;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setListener();
        setDataS();
        setSytle();
    }

    private void setDataS(){
        User user = (User) getIntent().getSerializableExtra("user");
        nickname = findViewById(R.id.nickname);
        gender = findViewById(R.id.gender);
        sign = findViewById(R.id.sign);
        address = findViewById(R.id.address);
        hobby = findViewById(R.id.hobby);
        imageView = findViewById(R.id.imageview);
        String2File.setImage(imageView,"/icons/"+user.getId()+"_icon");
        nickname.setText(user.getNickname());
        String gen = "";
        switch (user.getGender()){
            case 0:
                gen = "男";
                break;
            case 1:
                gen = "女";
                break;
            case 2:
                gen = "保密";
                break;
        }
        gender.setText(gen);
        sign.setText(user.getUserInfo().getSign());
        address.setText(user.getUserInfo().getAddress());
        hobby.setText(user.getUserInfo().getHobby());
    }

    private void setListener(){
//        sendMessage = findViewById(R.id.sendMessage);
//        sendMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,ChatActivity.class);
//
//                startActivity(intent);
//            }
//        });
    }

    private void setSytle(){
        StyleUtil.setzhuangtailan(this);

    }
}
