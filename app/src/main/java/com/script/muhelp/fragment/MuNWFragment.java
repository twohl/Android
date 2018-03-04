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
import com.script.muhelp.adapter.MuChatRecyclerViewAdapter;
import com.script.muhelp.adapter.MuNWRecyclerViewAdapter;
import com.script.muhelp.listener.OnMyClickListener;

import java.util.ArrayList;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuNWFragment extends Fragment {

    private LinearLayoutManager mLayoutManager;
    private MuNWRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private OnMyClickListener onMyClickListener;

    public MuNWRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }

    public MuNWFragment(){
        mAdapter = new MuNWRecyclerViewAdapter();

    }

    public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
        this.onMyClickListener = onMyClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment3,container,false);
        mLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView = rootView.findViewById(R.id.nwList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        TextView myNW = rootView.findViewById(R.id.myNW);
        myNW.setOnClickListener(new View.OnClickListener() {
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