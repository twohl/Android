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

import com.script.muhelp.R;
import com.script.muhelp.adapter.MuChatRecyclerViewAdapter;
import com.script.muhelp.adapter.MuFindRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuFindFragment extends Fragment {

    private LinearLayoutManager mLayoutManager;
    private MuFindRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public MuFindFragment(){
        mAdapter = new MuFindRecyclerViewAdapter(null);
    }

    public MuFindRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment4,container,false);
        mLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        Log.i("debug", "onCreateView: "+getData());
        mRecyclerView = rootView.findViewById(R.id.findList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
    private ArrayList<String> getData(){

        ArrayList<String> data = new ArrayList<>();
        String temp = " find";
        for(int i = 0; i < 20; i++) {
            data.add(i + temp);
        }

        return data;
    }
}