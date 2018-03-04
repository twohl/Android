package com.script.muhelp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.script.muhelp.R;
import com.script.muhelp.entity.NotWork;
import com.script.muhelp.entity.User;
import com.script.muhelp.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuNWRecyclerViewAdapter extends RecyclerView.Adapter {

    private User my;

    private ArrayList<NotWork> mData;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setMy(User my) {
        this.my = my;
    }

    public MuNWRecyclerViewAdapter() {
    }

    public void updateData(ArrayList<NotWork> data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment3_item,parent,false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotWork notWork = mData.get(position);
        holder.itemView.setTag(notWork);
        ((ViewHolder)holder).id.setText(notWork.getId()+"");
        ((ViewHolder)holder).title.setText(notWork.getName());
        ((ViewHolder)holder).content.setText(notWork.getContent());

    }

    @Override
    public int getItemCount() {
        return mData ==null?0:mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView title;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.nw_id);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
