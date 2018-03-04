package com.script.muhelp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.script.muhelp.R;
import com.script.muhelp.ResultCode;
import com.script.muhelp.entity.User;
import com.script.muhelp.services.MuService;
import com.script.muhelp.util.EditUtil;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.ResultUtil;
import com.script.muhelp.util.StyleUtil;
import com.script.muhelp.util.SignUtil;
import com.script.muhelp.util.SnackbatUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.script.muhelp.VarPool.*;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{

    private View container;
    private Context context = this;
    private String uuid;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setListener();
        setStyle();

    }

    private void setStyle(){
        StyleUtil.setzhuangtailan(this);
        StyleUtil.showProgress(container,false);
        int from = getIntent().getIntExtra("from",0);
        switch (from){
            case NORMAL_FROM:

                break;
            case ERROR_FROM:
                SnackbatUtil.showErrorLong(container,"登陆过起请重新登录");
                break;
        }
    }

    private void setListener(){
        container = findViewById(R.id.container);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditUtil.setEditTextInhibitInput(username,password);

        button = findViewById(R.id.login);
        button.setOnClickListener(this);
        Button registy = findViewById(R.id.registy);
        registy.setOnClickListener(this);
        container.setOnTouchListener(this);
        button.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        button.setFocusable(true);
        button.setFocusableInTouchMode(true);
        button.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.username)).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.password)).getWindowToken(), 0);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                login();
                break;

            case R.id.registy:
                Intent intent = new Intent(this,RegistyActivity.class);
                startActivityForResult(intent,1);
                break;

        }
    }

    private void login(){
        EditText usernameE = findViewById(R.id.username);
        EditText passwordE = findViewById(R.id.password);
        String username = usernameE.getText().toString();
        String password = passwordE.getText().toString();
        if(validateLogin(username,password)) {
            LoginTask loginTask = new LoginTask(username, password);
            loginTask.execute();
        }
    }

    private boolean validateLogin(String username,String password){

        if(username == null||"".equals(username)){
            SnackbatUtil.showErrorLong(container,"用户名不能为空");
            return false;
        }

        if(password == null||"".equals(password)){
            SnackbatUtil.showErrorLong(container,"密码不能为空");

            return false;
        }
        if(password.length()<6){
            SnackbatUtil.showErrorLong(container,"密码最少为6位数");

            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTY){
            switch (resultCode){
                case REGISTYSUCCESS:
                    String username = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    ((EditText)findViewById(R.id.username)).setText(username);
                    ((EditText)findViewById(R.id.password)).setText(password);
                    login();
                    break;
                case CANLE:

                    break;
            }
        }
    }
    public class LoginTask extends AsyncTask {
        private String username;
        private String password;
        private Map map;
        private boolean logined;

        public LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            uuid = SignUtil.getMyUUID((Activity) context);
            map = new HashMap();
            map.put("lastLogin",uuid);
            map.put("username",username);
            map.put("password",password);
            StyleUtil.showProgress(container,true);

            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            map = HttpUtil.jsonRequset("POST", "/user/login", map);
            if(map !=null&&ResultCode.valueOf((String)map.get("code")).equals(ResultCode.LOGIN_SUCCESS)){
                logined = true;
            }else{
                logined = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(logined){
                Intent intent = new Intent(context, MuService.class);
                JSONObject json = (JSONObject) map.get("data");
                intent.putExtra("username",username);
                startService(intent);
                File file = new File(context.getFilesDir(),"users//"+username+"//");
                file.mkdirs();
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                StyleUtil.showProgress(container,false);
                if(map!=null)
                    SnackbatUtil.showErrorLong(container,(String)map.get("message"));
                else
                    SnackbatUtil.showErrorLong(container,"连接服务器失败,请检查网络");
            }
        }
    }
}

