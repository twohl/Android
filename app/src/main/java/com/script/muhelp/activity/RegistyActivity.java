package com.script.muhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.script.muhelp.R;
import com.script.muhelp.ResultCode;
import com.script.muhelp.util.EditUtil;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.ResultUtil;
import com.script.muhelp.util.StyleUtil;
import com.script.muhelp.util.SnackbatUtil;

import java.util.HashMap;
import java.util.Map;

import static com.script.muhelp.VarPool.*;


public class RegistyActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{

    private Context context =this;
    private View container;
    private Button regist;
    private Button back;
    private String username;
    private String password;
    private String repassword;
    private String nickname;
    private int gender = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registy);
        setListener();
        setStyle();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setStyle(){
        StyleUtil.setzhuangtailan(this);
    }

    private void setListener(){
        container = findViewById(R.id.container);
        regist = findViewById(R.id.registy);
        back = findViewById(R.id.registy_back);
        regist.setOnClickListener(this);
        container.setOnTouchListener(this);
        regist.setOnTouchListener(this);
        back.setOnClickListener(this);
        EditText rusername = findViewById(R.id.registy_usename);
        EditText rpassword = findViewById(R.id.registy_password);
        EditText rrepassword = findViewById(R.id.registy_repassword);
        EditText rnickname = findViewById(R.id.registy_nickname);

        EditUtil.setEditTextInhibitInput(rnickname,rpassword,rrepassword,rusername);
        RadioGroup group = findViewById(R.id.gender);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male:
                        gender = 0;
                        break;

                    case R.id.female:
                        gender = 1;
                        break;

                    case R.id.secret:
                        gender = 2;
                        break;
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        regist.setFocusable(true);
        regist.setFocusableInTouchMode(true);
        regist.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.registy_nickname)).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.registy_usename)).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.registy_password)).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.registy_repassword)).getWindowToken(), 0);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registy:
                registy();
                break;
            case R.id.registy_back:
                setResult(3);
                finish();
                break;
        }
    }

    private void registy(){
        if(validate()){
            RegistTask task = new RegistTask();
            task.execute();
        }
    }

    private boolean validate(){

        EditText rusername = findViewById(R.id.registy_usename);
        EditText rpassword = findViewById(R.id.registy_password);
        EditText rrepassword = findViewById(R.id.registy_repassword);
        EditText rnickname = findViewById(R.id.registy_nickname);
        username = rusername.getText().toString();
        password = rpassword.getText().toString();
        repassword = rrepassword.getText().toString();
        nickname = rnickname.getText().toString();
        if(username == null || "".equals(username)){
            SnackbatUtil.showErrorLong(container,"用户名为空");
            return false;
        }
        if(password == null || "".equals(password)){
            SnackbatUtil.showErrorLong(container,"密码为空");
            return false;
        }
        if(password.length()<6){
            SnackbatUtil.showErrorLong(container,"密码小于6位数");
            return false;
        }
        if(repassword == null || "".equals(repassword)){
            SnackbatUtil.showErrorLong(container,"确认密码为空");
            return false;
        }
        if(!repassword.equals(password)){
            SnackbatUtil.showErrorLong(container,"两次输入密码不一致");
            return false;
        }
        if(nickname == null || "".equals(nickname)){
            SnackbatUtil.showErrorLong(container,"昵称为空");
            return false;
        }
        if(gender == 0){
            SnackbatUtil.showErrorLong(container,"请选择性别");
            return false;
        }
        return true;

    }

    public class RegistTask extends AsyncTask {
        private Map map;
        private boolean flag = false;

        public RegistTask() {

        }

        @Override
        protected void onPreExecute() {
            map = new HashMap();
            map.put("username",username);
            map.put("password",password);
            map.put("nickname",nickname);
            map.put("gender",gender);
            StyleUtil.showProgress(container,true);

            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            map = HttpUtil.jsonRequset("PUT", "/user/regist", map);
            if(map !=null &&ResultCode.valueOf((String)map.get("code")).equals(ResultCode.REGIST_SUCCESS)){
                flag = true;
            }else{
                flag = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(flag){
                Intent intent = new Intent();
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                setResult(REGISTYSUCCESS,intent);
                finish();
            }else{
                StyleUtil.showProgress(container,false);
                if(map !=null)
                    SnackbatUtil.showErrorLong(container,(String)map.get("message"));
                else
                    SnackbatUtil.showErrorLong(container,"连接服务器失败,请检查网络");
            }
        }
    }
}