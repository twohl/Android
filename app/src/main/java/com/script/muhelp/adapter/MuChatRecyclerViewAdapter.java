package com.script.muhelp.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.entity.Chat;
import com.script.muhelp.entity.ChatId;
import com.script.muhelp.entity.User;
import com.script.muhelp.listener.OnItemClickListener;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.ResultUtil;
import com.script.muhelp.util.String2File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuChatRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<ChatId> mData;
    private User my;
    private OnItemClickListener onItemClickListener = null;

    public MuChatRecyclerViewAdapter() {
        mData = new ArrayList<>();
    }

    public void setMy(User my) {
        this.my = my;
    }

    public void updateData(ChatId chatId){
        for(int i=0;i<mData.size();i++){
            if(mData.get(i).getId() == chatId.getId()){
                if(chatId.getCount()>0){
                    mData.get(i).setCount(chatId.getCount());
                    notifyItemChanged(i);
                }
                return;
            }
        }
        mData.add(0,chatId);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment1_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ChatId chatId = mData.get(position);
        holder.itemView.setTag(chatId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,v.getTag());
                    notifyItemMoved(position,0);
                }
            }
        });
        User user;
        if(chatId.getUser1().getId() == my.getId()){
            user = chatId.getUser2();
        }else{
            user = chatId.getUser1();
        }
        ((ViewHolder)holder).user_id.setText(user.getId()+"");
        ((ViewHolder)holder).nickname.setText(user.getNickname());
        int count = chatId.getCount();
        ((ViewHolder) holder).count.setText("新消息");
        if(count>0){
            ((ViewHolder) holder).count.setVisibility(View.VISIBLE);
        }else {
            ((ViewHolder) holder).count.setVisibility(View.GONE);
        }
        String2File.setImage(((ViewHolder) holder).imageView,"/icons/"+user.getId()+"_icon");
    }

    @Override
    public int getItemCount() {
        return mData ==null?0:mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nickname;
        ImageView imageView;
        TextView user_id;
        TextView count;

        public ViewHolder(View itemView) {
            super(itemView);
            nickname =  itemView.findViewById(R.id.nickname);
            imageView = itemView.findViewById(R.id.imageview);
            user_id = itemView.findViewById(R.id.user_id);
            count = itemView.findViewById(R.id.count);
        }
    }

}
