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
import com.script.muhelp.entity.Share;
import com.script.muhelp.listener.OnItemClickListener;
import com.script.muhelp.util.HttpUtil;
import com.script.muhelp.util.String2File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuFindRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Share> mData;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MuFindRecyclerViewAdapter(ArrayList<Share> mData) {
        this.mData = mData;
    }

    public void updateData(ArrayList<Share> data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment4_item,parent,false);
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

        Share share = mData.get(position);

        holder.itemView.setTag(share);

        ((ViewHolder)holder).content.setText(share.getContent());
        ((ViewHolder)holder).title.setText(share.getTitle());
        ((ViewHolder)holder).nickname.setText(share.getUser().getNickname());
        String2File.setImage(((ViewHolder) holder).imageview,"/images/"+share.getId()+"_image");
        String2File.setImage(((ViewHolder) holder).icon,"/icons/"+share.getUser().getId()+"_icon");
    }

    @Override
    public int getItemCount() {
        return mData ==null?0:mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView content;

        TextView title;

        TextView nickname;

        ImageView imageview;

        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);
            nickname = itemView.findViewById(R.id.nickname);
            imageview = itemView.findViewById(R.id.imageview);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
