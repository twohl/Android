package com.script.muhelp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.adapter.MuPersonRecyclerViewAdapter;
import com.script.muhelp.listener.OnItemClickListener;
import com.script.muhelp.listener.OnMyClickListener;

import java.util.ArrayList;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuPersonFragment extends Fragment {


    private LinearLayoutManager mLayoutManager;
    private MuPersonRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private OnMyClickListener onMyClickListener;
    private TextView chatRoom;


    public MuPersonFragment(){
        mAdapter = new MuPersonRecyclerViewAdapter();
    }

    public MuPersonRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }

    public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
        this.onMyClickListener = onMyClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment2,container,false);
        mLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView = rootView.findViewById(R.id.personList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        chatRoom = rootView.findViewById(R.id.chatRoom);
        chatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMyClickListener!=null){
                    onMyClickListener.onClick(v);
                }
            }
        });
        return rootView;
    }
}