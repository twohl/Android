package com.script.muhelp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.script.muhelp.R;
import com.script.muhelp.adapter.MuChatMessageAdapter;
import com.script.muhelp.binder.MuBinder;
import com.script.muhelp.entity.Chat;
import com.script.muhelp.entity.ChatId;
import com.script.muhelp.entity.ChatRoomMessage;
import com.script.muhelp.entity.User;
import com.script.muhelp.services.MuService;
import com.script.muhelp.util.EditUtil;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.PositionUtil;
import com.script.muhelp.util.ResultUtil;
import com.script.muhelp.util.SignUtil;
import com.script.muhelp.util.StyleUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static com.script.muhelp.VarPool.CHAT_LIST;
import static com.script.muhelp.VarPool.OK;
import static com.script.muhelp.VarPool.SEND;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private User my;
    private User other;
    private Context context = this;
    private ChatId chatId;
    private Button send;
    private MuChatMessageAdapter adapter;
    private EditText message;
    private String username;
    private boolean isChatRomm = false;
    private String lastTime;
    private Timer timer;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CHAT_LIST:
                    List list = (List) msg.getData().getSerializable("chat_list");
                    adapter.updateData(list);
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    break;
                case SEND:
                    message.setText("");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setDataS();
        setListener();
        setStyle();
    }


    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }

    private void setStyle(){
        StyleUtil.setzhuangtailan(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if(isChatRomm) {
            toolbar.setTitle("聊天室");
        }else{
            toolbar.setTitle(other.getNickname());
        }
        setSupportActionBar(toolbar);
    }

    private void setDataS(){
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        timer = new Timer();
        my = (User) getIntent().getSerializableExtra("my");
        username = getIntent().getStringExtra("username");
        isChatRomm = getIntent().getBooleanExtra("isChatRoom",false);
        if(!isChatRomm)
            lastTime = getIntent().getStringExtra("lastTime");
        if(!isChatRomm){
            chatId = (ChatId) getIntent().getSerializableExtra("chat");
            if(my.getId() == chatId.getUser1().getId()){
                other = chatId.getUser2();
            }else{
                other = chatId.getUser1();
            }
        }
        recyclerView = findViewById(R.id.chatMessage);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new MuChatMessageAdapter();
        adapter.setMy(my);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        TimerTask task;
        if(isChatRomm){
            task = new TimerTask() {
                @Override
                public void run() {
                    Map map = new HashMap();
                    map.put("username",username);
                    map.put("lastLogin",SignUtil.getMyUUID(context));
                    lastTime = sdf.format(new Date());
                    Location location = PositionUtil.getLocation();
                    map.put("lng",location.getLongitude());
                    map.put("lat",location.getLatitude());
                    map.put("date",lastTime);
                    Message message1 = new Message();
                    message1.what = CHAT_LIST;
                    Map result = HttpUtil.jsonRequset("POST","/chat/getChatNear",map);
                    message1 = ResultUtil.resulthandle(result,message1);
                    List<Chat> list = (List) message1.getData().getSerializable("chat_list");
                    if(list!=null && list.size()>0){
                        lastTime = sdf.format(list.get(list.size()-1).getDate());
                        handler.sendMessage(message1);
                    }
                }
            };
        }else{
            task = new TimerTask() {
                @Override
                public void run() {
                    Map map = new HashMap();
                    map.put("username",username);
                    map.put("lastLogin",SignUtil.getMyUUID(context));
                    map.put("chat_id",chatId.getId());
                    map.put("date",lastTime);
                    Message message1 = new Message();
                    message1.what = CHAT_LIST;
                    Map result = HttpUtil.jsonRequset("POST","/chat/getChat",map);
                    message1 = ResultUtil.resulthandle(result,message1);
                    List<Chat> list = (List) message1.getData().getSerializable("chat_list");
                    if(list!=null && list.size()>0){
                        lastTime = sdf.format(list.get(list.size()-1).getDate());
                        handler.sendMessage(message1);
                    }
                }
            };
        }
        timer.schedule(task,0,1000);

    }

    private void setListener(){
        send = findViewById(R.id.sendMessage);
        message = findViewById(R.id.content);
        EditUtil.setEditTextInhibitInput(message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String content = message.getText().toString();
                        Map map = new HashMap<>();
                        map.put("content",content);
                        String uuid = SignUtil.getMyUUID(context);
                        map.put("username",username);
                        map.put("lastLogin",uuid);
                        if(isChatRomm){
                            Location location = PositionUtil.getLocation();
                            map.put("lng",location.getLongitude());
                            map.put("lat",location.getLatitude());
                            HttpUtil.jsonRequset("PUT","/chat/chatNearBy",map);
                        }else{
                            map.put("chat_id",chatId.getId());
                            HttpUtil.jsonRequset("PUT","/chat/chatUser",map);
                        }
                        Message message1 = new Message();
                        message1.what = SEND;
                        handler.sendMessage(message1);
                    }
                }).start();

            }
        });
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
