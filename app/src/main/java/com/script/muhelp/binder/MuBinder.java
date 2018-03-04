package com.script.muhelp.binder;

import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.script.muhelp.ResultCode;
import com.script.muhelp.entity.Chat;
import com.script.muhelp.entity.ChatId;
import com.script.muhelp.entity.Share;
import com.script.muhelp.entity.User;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.ResultUtil;
import com.script.muhelp.util.String2File;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.script.muhelp.ResultCode.*;
import static com.script.muhelp.VarPool.*;

/**
 * Created by hongl on 2018/2/21.
 */

public class MuBinder extends Binder {
    private String username;
    private String uuid;
    private Location location;
    private Handler handler;
    private List<User> userList;
    private User my;
    private File path;
    private Lock lock = new ReentrantLock();
    private Lock lock1 = new ReentrantLock();
    private String lastTime;
    private Timer timer;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    public void setPath(File path) {
        this.path = path;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public MuBinder(String username, String uuid){
        this.username = username;
        this.uuid = uuid;
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        lastTime = sdf.format(new Date());
    }

    public void share(final Map map){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = PUBLISH_SHARE;
                addParam(map);
                map.put("lng",location.getLongitude());
                map.put("lat",location.getLatitude());
                Map result = HttpUtil.jsonRequset("PUT","/share/write",map);
                message = ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        }).start();
    }

    public void accNW(final String nw_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = new HashMap();
                Message message = new Message();
                message.what = ACC_NW;
                map.put("nw_id",nw_id);
                addParam(map);
                Map result = HttpUtil.jsonRequset("POST","/nw/acc",map);
                ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        }).start();
    }

    public void compliteNW(final String nw_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = new HashMap();
                Message message = new Message();
                message.what = ACC_NW;
                map.put("nw_id",nw_id);
                addParam(map);
                Map result = HttpUtil.jsonRequset("POST","/nw/complite",map);
                message = ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        }).start();
    }

    public void cancleNW(final String nw_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = new HashMap();
                Message message = new Message();
                message.what = ACC_NW;
                map.put("nw_id",nw_id);
                addParam(map);
                Map result = HttpUtil.jsonRequset("POST","/nw/cancle",map);
                ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        }).start();
    }

    private void addParam(final Map map){
        map.put("username",username);
        map.put("lastLogin",uuid);

    }

    public void publishNW(final Map map){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = PUBLISH_NW;
                addParam(map);
                map.put("lng",location.getLongitude());
                map.put("lat",location.getLatitude());
                Map result = HttpUtil.jsonRequset("PUT","/nw/publish",map);
                message = ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        }).start();
    }

    public void editUserData(final Map map){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = EDITUSER;
                addParam(map);
                Map result = HttpUtil.jsonRequset("POST","/user/edit",map);
                message = ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        }).start();
    }

    public void getMainData(){
        Timer timer = new Timer();
        TimerTask getUserInfo = getUserInfoData();
        timer.schedule(getUserInfo,0,10000);
    }

    public void start(){
        timer = new Timer();
        TimerTask getUser = getUserListData();
        TimerTask getNW = getNWListData();
        TimerTask getShare = getShareListData();
        TimerTask keep = keep();
        TimerTask getMyNW = getMyNW();
        TimerTask getMessage = getChatId();
        timer.schedule(getMessage,0,2000);
        timer.schedule(getMyNW,0,4000);
        timer.schedule(getUser,0,30000);
        timer.schedule(getNW,0,4000);
        timer.schedule(getShare,0,10000);
        timer.schedule(keep,0,20000);
    }

    private TimerTask getUserInfoData(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map map = new HashMap();
                addParam(map);
                Message message = new Message();
                message.what = USER_INFO;
                Map result = HttpUtil.jsonRequset("POST","/user/userInfo",map);
                message = ResultUtil.resulthandle(result,message);
                User temp = (User) message.getData().getSerializable("userInfo");
                if(temp!=null){
                    my = temp;
                    handler.sendMessage(message);
                }
            }
        };

        return task;
    }

    public TimerTask getMyNW(){
        TimerTask task =  new TimerTask() {
            @Override
            public void run() {
                Map map = new HashMap();
                addParam(map);
                Message message = new Message();
                message.what = MY_NW;
                Map result = HttpUtil.jsonRequset("POST","/nw/getUserNW",map);
                message = ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        };
        return task;
    }

    private TimerTask keep(){

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map map = new HashMap();
                Message message = new Message();
                message.what = KEEP;
                addParam(map);
                map.put("lng",location.getLongitude());
                map.put("lat",location.getLatitude());
                Map result = HttpUtil.jsonRequset("POST","/user/keep",map);
                message = ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        };
        return task;
    }

    private TimerTask getUserListData(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map map = new HashMap();
                Message message = new Message();
                message.what = USER_LIST;
                addParam(map);
                map.put("lng",location.getLongitude());
                map.put("lat",location.getLatitude());
                Map result = HttpUtil.jsonRequset("POST", "/user/userList", map);
                message = ResultUtil.resulthandle(result, message);
                userList = (ArrayList<User>) message.getData().getSerializable("userList");
                final List<User> list = userList;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        lock1.lock();
                        for(int i=0;i<list.size();i++){
                            User user = list.get(i);
                            File file = new File(path,"users//icons//"+user.getId()+"_icon");
                            if(!file.getParentFile().exists()){
                                file.mkdirs();
                            }
                            if(!file.exists()){
                                Map result = HttpUtil.jsonRequset("POST","/getIcon/"+user.getId(),null);
                                if(result!=null){
                                    String data = (String)result.get("data");
                                    String2File.toFile(file,data);
                                }

                            }
                        }
                        lock1.unlock();
                    }
                }).start();

                handler.sendMessage(message);

            }
        };
        return task;
    }

    private TimerTask getNWListData(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map map = new HashMap();
                Message message = new Message();
                message.what = NW_LIST;
                addParam(map);
                map.put("lng",location.getLongitude());
                map.put("lat",location.getLatitude());
                Map result = HttpUtil.jsonRequset("POST","/nw/nwList",map);
                message = ResultUtil.resulthandle(result,message);
                handler.sendMessage(message);
            }
        };
        return task;
    }

    private TimerTask getShareListData(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map map = new HashMap();
                Message message = new Message();
                message.what = SHARE_LIST;
                addParam(map);
                map.put("lng",location.getLongitude());
                map.put("lat",location.getLatitude());
                Map result = HttpUtil.jsonRequset("POST","/share/getList",map);
                message = ResultUtil.resulthandle(result,message);
                final ArrayList<Share> list = (ArrayList<Share>) message.getData().getSerializable("shareList");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        lock.lock();
                        for(Share share:list){
                            File file = new File(path,"users//images//"+share.getId()+"_icon");
                            if(!file.getParentFile().exists()){
                                file.mkdirs();
                            }
                            if(!file.exists()){
                                Map result = HttpUtil.jsonRequset("POST","/getImage/"+share.getId(),null);
                                if(result!=null){
                                    String data = (String)result.get("data");
                                    String2File.toFile(file,data);
                                }

                            }
                        }
                        lock.unlock();
                    }
                }).start();
                handler.sendMessage(message);
            }
        };
        return task;
    }

    private TimerTask getChatId(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(userList == null)
                    return;
                final List<User> list = userList;
                for(int i=0;i<list.size();i++){
                    User user = list.get(i);
                    if(user.getId() == my.getId())
                        continue;
                    Message message = new Message();
                    Map map = new HashMap();
                    map.put("user2",user.getId());
                    map.put("date",lastTime);
                    lastTime = sdf.format(new Date());
                    message.what = CHAT_ID;
                    addParam(map);
                    Map result = HttpUtil.jsonRequset("POST","/chat/getChatId",map);
                    message = ResultUtil.resulthandle(result,message);
                    handler.sendMessage(message);
                }
            }
        };
        return task;
    }

}
