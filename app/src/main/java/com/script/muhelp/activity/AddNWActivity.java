package com.script.muhelp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.script.muhelp.R;
import com.script.muhelp.binder.MuBinder;
import com.script.muhelp.services.MuService;
import com.script.muhelp.util.EditUtil;
import com.script.muhelp.util.StyleUtil;

import java.util.HashMap;
import java.util.Map;

import static com.script.muhelp.VarPool.CANLE;
import static com.script.muhelp.VarPool.OK;

public class AddNWActivity extends AppCompatActivity implements View.OnClickListener{

    private View container;
    private EditText title;
    private EditText content;
    private EditText price;
    private Button back;
    private Button publish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nw);
        setListener();
        setStyle();
    }

    private void setStyle(){
        StyleUtil.setzhuangtailan(this);

    }

    private void setListener(){
        container = findViewById(R.id.container);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        price = findViewById(R.id.price);
        EditUtil.setEditTextInhibitInput(title,content,price);
        back = findViewById(R.id.back);
        publish = findViewById(R.id.publish);
        back.setOnClickListener(this);
        publish.setOnClickListener(this);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                container.setFocusable(true);
                container.setFocusableInTouchMode(true);
                container.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(price.getWindowToken(), 0);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish:
                publishNW();
                break;
            case R.id.back:
                setResult(CANLE);
                finish();
                break;
        }
    }
    private void publishNW(){
        HashMap map = new HashMap();
        map.put("name",title.getText().toString());
        map.put("content",content.getText().toString());
        map.put("price",price.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("map",map);
        setResult(OK,intent);
        finish();
    }
}
