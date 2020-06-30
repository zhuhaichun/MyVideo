package com.android.haichun.myvideoplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.model.LocalVideo;
import com.android.haichun.myvideoplayer.ui.CustomJzvdStd;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.JzvdStd;

public class LocalRvAdapter extends RecyclerView.Adapter<LocalRvAdapter.MyHolder> {

    Context mContext;
    List<LocalVideo> mVideos;

    public LocalRvAdapter(Context context, List<LocalVideo> videos) {
        mContext = context;
        mVideos = videos;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_rv,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(mContext).load(mVideos.get(position).getImg()).into(holder.cover);
        holder.setDuration(mVideos.get(position).getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JzvdStd.startFullscreenDirectly(mContext, CustomJzvdStd.class,mVideos.get(position).getPath(),"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView duration;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_local_cover_iv);
            duration = itemView.findViewById(R.id.item_local_dur_tv);
        }
        public void setDuration(long dur){
            int mm = (int) (dur / 1000 / 60);
            int ss = (int) (dur / 1000 % 60);
            String s;
            if (mm < 10){
                s = "0"+mm+":";
            }else{
                s = mm+":";
            }
            if (ss < 10){
                s += "0"+ss;
            }else{
                s += ss;
            }
            duration.setText(s);
        }
    }
}
