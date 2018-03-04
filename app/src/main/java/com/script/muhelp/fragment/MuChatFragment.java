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
import com.script.muhelp.entity.ChatId;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuChatFragment extends Fragment {

    private LinearLayoutManager mLayoutManager;
    private MuChatRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    public MuChatFragment(){
        mAdapter = new MuChatRecyclerViewAdapter();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment1,container,false);
        mLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView = rootView.findViewById(R.id.chatList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    public MuChatRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }

}