package com.script.muhelp.adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.entity.User;
import com.script.muhelp.listener.OnItemClickListener;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.String2File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuPersonRecyclerViewAdapter extends RecyclerView.Adapter {

    private User my;
    private ArrayList<User> mData;
    private OnItemClickListener onItemClickListener;

    public void setMy(User my) {
        this.my = my;
    }

    public MuPersonRecyclerViewAdapter() {

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateData(ArrayList<User> data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment2_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,v.getTag());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        User user = mData.get(position);
        ((ViewHolder)holder).nickname.setText(user.getNickname());
        String sign = user.getUserInfo().getSign()==null?"这个人很懒，什么也没留下":user.getUserInfo().getSign();
        ((ViewHolder)holder).sign.setText(sign);
        ((ViewHolder)holder).user_id.setText(user.getId()+"");
        String2File.setImage(((ViewHolder) holder).imageView,"/icons/"+user.getId()+"_icon");
        holder.itemView.setTag(user);

    }

    @Override
    public int getItemCount() {
        return mData ==null?0:mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nickname;

        TextView sign;

        TextView user_id;

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.nickname);
            sign = itemView.findViewById(R.id.sign);
            user_id = itemView.findViewById(R.id.user_id);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }
}
