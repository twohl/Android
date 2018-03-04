package com.script.muhelp.util;

import android.os.Bundle;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.script.muhelp.ResultCode;
import com.script.muhelp.entity.Chat;
import com.script.muhelp.entity.ChatId;
import com.script.muhelp.entity.NotWork;
import com.script.muhelp.entity.Share;
import com.script.muhelp.entity.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.script.muhelp.VarPool.*;

/**
 * Created by hongl on 2018/2/24.
 */

public class ResultUtil {


    public static Message resulthandle(Map map, Message message){
        Bundle bundle = new Bundle();
        if(map == null){
            message.what = LINK_OUT;
            bundle.putString("message","连接服务器失败");
            message.setData(bundle);
            return message;
        }
        Object code = map.get("code");
        if(code == null ||"".equals(code)){
            message.what = LINK_OUT;
            bundle.putString("message","连接服务器失败");
            message.setData(bundle);
            return message;
        }
        ResultCode resultCode = ResultCode.valueOf((String)map.get("code"));
        switch (resultCode){
            case PURVIEW_CER_EXCEPTION:
                message.what = LOG_OUT;
                break;

            case DATAUPDATE_SUCCESS:
            case NWACC_SUCCESS:
            case SHARE_SUCCESS:
            case NWPUBLISH_SUCCESS:
                bundle.putString("message", (String) map.get("message"));
                message.setData(bundle);
                return message;

            case GETDATA_SUCCESS:
                switch (message.what){
                    case USER_LIST:
                        JSONArray userArray = JSON.parseArray(map.get("data").toString());
                        User[] userlist = JSON.toJavaObject(userArray, User[].class);
                        List<User> list =  Arrays.asList(userlist);
                        ArrayList arrayList = new ArrayList(list);
                        bundle.putSerializable("userList",arrayList);
                        bundle.putString("message",(String)map.get("message"));
                        message.setData(bundle);
                        return message;
                    case NW_LIST:
                        JSONArray nwArray = JSON.parseArray(map.get("data").toString());
                        NotWork[] nwlist = JSON.toJavaObject(nwArray, NotWork[].class);
                        List<NotWork> list1 = Arrays.asList(nwlist);
                        ArrayList arrayList1 = new ArrayList(list1);
                        bundle.putSerializable("nwList",arrayList1);
                        bundle.putString("message",(String)map.get("message"));
                        message.setData(bundle);
                        return message;
                    case SHARE_LIST:
                        JSONArray shareArray = JSON.parseArray(map.get("data").toString());
                        Share[] sharelist = JSON.toJavaObject(shareArray, Share[].class);
                        List<Share> list2 = Arrays.asList(sharelist);
                        ArrayList arrayList2 = new ArrayList(list2);
                        bundle.putSerializable("shareList",arrayList2);
                        bundle.putString("message",(String)map.get("message"));
                        message.setData(bundle);
                        return message;
                    case USER_INFO:
                        User user = JSON.toJavaObject((JSONObject)map.get("data"),User.class);
                        bundle.putSerializable("userInfo",user);
                        bundle.putString("message",(String)map.get("message"));
                        message.setData(bundle);
                        return message;
                    case MY_NW:
                        JSONArray mnwArray = JSON.parseArray(map.get("data").toString());
                        NotWork[] mnwList = JSON.toJavaObject(mnwArray,NotWork[].class);
                        List<NotWork> list3 = Arrays.asList(mnwList);
                        ArrayList arrayList3 = new ArrayList(list3);
                        bundle.putSerializable("mNWList",arrayList3);
                        bundle.putString("message",(String)map.get("message"));
                        message.setData(bundle);
                        return message;
                    case CHAT_ID:
                        ChatId chatId = JSON.toJavaObject((JSONObject) map.get("data"), ChatId.class);
                        bundle.putSerializable("chat_id",chatId);
                        bundle.putString("message",(String)map.get("message"));
                        message.setData(bundle);
                        return message;

                    case CHAT_LIST:
                        JSONArray messageArray = JSON.parseArray(map.get("data").toString());
                        Chat[] chats = JSON.toJavaObject(messageArray,Chat[].class);
                        List list4 = Arrays.asList(chats);
                        ArrayList<Chat> arrayList4 = new ArrayList(list4);
                        bundle.putSerializable("chat_list",arrayList4);
                        bundle.putString("message",(String)map.get("message"));
                        message.setData(bundle);
                        return message;
                }
                break;

            default:
                message.what =ERROR;
                bundle.putString("message",(String)map.get("message"));
                message.setData(bundle);
                return message;

        }

        bundle.putString("message", (String) map.get("message"));
        message.setData(bundle);
        return message;
    }
}
