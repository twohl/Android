package com.script.muhelp.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.entity.Chat;
import com.script.muhelp.entity.User;
import com.script.muhelp.util.String2File;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hongl on 2018/2/24.
 */

public class MuChatMessageAdapter extends RecyclerView.Adapter {

    private List<Chat> mData;
    private final int MINE = 1;
    private final int OTHER = 2;
    private User my;
    private List<Integer> types = new ArrayList<>();

    public MuChatMessageAdapter() {

    }

    public void setMy(User my) {
        this.my = my;
    }

    private void addType(List<Chat> data){
        if(data ==null)
            return;
        for(int i=0;i<data.size();i++){
            if(data.get(i).getUser().getId() == my.getId()){
                types.add(MINE);
            }else{
                types.add(OTHER);
            }
        }
    }



    public void updateData(List<Chat> data){
        if(mData == null)
            mData = new ArrayList<>();
        if(data != null && data.size()>0){
            mData.addAll(data);
            types.clear();
            addType(mData);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case MINE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_mine,parent,false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            case OTHER:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_other,parent,false);
                ViewHolder viewHolder1 = new ViewHolder(view1);
                return viewHolder1;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).content.setText(mData.get(position).getContent());
        String2File.setImage(((ViewHolder) holder).imageView,"/icons/"+mData.get(position).getUser().getId()+"_icon");
    }

    @Override
    public int getItemViewType(int position) {
        return types.get(position);
    }

    @Override
    public int getItemCount() {
        return mData == null?0:mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView content;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }
}
