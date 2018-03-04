package com.script.muhelp.fragment;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.script.muhelp.R;
import com.script.muhelp.adapter.MuChatRecyclerViewAdapter;
import com.script.muhelp.adapter.MuFindRecyclerViewAdapter;
import com.script.muhelp.entity.User;
import com.script.muhelp.listener.OnMyClickListener;
import com.script.muhelp.util.String2File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuMineFragment extends Fragment {


    private View rootView;
    private EditText nickname;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    private RadioButton secret;
    private EditText sign;
    private EditText address;
    private EditText hobby;
    private ImageView imageView;
    private OnMyClickListener onMyClickListener;

    public MuMineFragment(){
    }

    public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
        this.onMyClickListener = onMyClickListener;
    }

    public View getRootView() {
        return rootView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment5,container,false);
        imageView = rootView.findViewById(R.id.imageview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMyClickListener.onClick(v);
            }
        });
        nickname = rootView.findViewById(R.id.nickname);
        male = rootView.findViewById(R.id.male);
        female = rootView.findViewById(R.id.female);
        secret = rootView.findViewById(R.id.secret);
        gender = rootView.findViewById(R.id.gender);
        sign = rootView.findViewById(R.id.sign);
        address = rootView.findViewById(R.id.address);
        hobby = rootView.findViewById(R.id.hobby);
        return rootView;
    }
    public void setEditable(boolean flag){
        nickname.setEnabled(flag);
        male.setEnabled(flag);
        female.setEnabled(flag);
        secret.setEnabled(flag);
        sign.setEnabled(flag);
        address.setEnabled(flag);
        hobby.setEnabled(flag);
    }

    public Map getUserData(){
        Map map = new HashMap();
        Map smap = new HashMap();
        map.put("nickname",nickname.getText().toString());
        switch (gender.getCheckedRadioButtonId()){
            case R.id.male:
                map.put("gender",0);
                break;

            case R.id.female:
                map.put("gender",1);
                break;
            case R.id.secret:
                map.put("gender",2);
                break;
        }
        smap.put("sign",sign.getText().toString());
        smap.put("address",address.getText().toString());
        smap.put("hobby",hobby.getText().toString());
        map.put("userinfo",smap);
        return map;
    }


    public void setData(User user){
        if(user != null){
            nickname.setText(user.getNickname());
            String signt = user.getUserInfo().getSign()==null?"":user.getUserInfo().getSign();
            String addresst = user.getUserInfo().getAddress()==null?"":user.getUserInfo().getAddress();
            String hobbyt = user.getUserInfo().getHobby()==null?"":user.getUserInfo().getHobby();
            switch (user.getGender()){
                case 0:
                    gender.check(R.id.male);
                    break;
                case 1:
                    gender.check(R.id.female);
                    break;
                case 2:
                    gender.check(R.id.secret);
                    break;
            }
            sign.setText(signt);
            address.setText(addresst);
            hobby.setText(hobbyt);
            String2File.setImage(imageView,"/icons/"+user.getId()+"_icon");

        }
    }
}