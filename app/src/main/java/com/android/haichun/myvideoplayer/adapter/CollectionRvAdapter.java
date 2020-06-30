package com.android.haichun.myvideoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.model.Video;
import com.android.haichun.myvideoplayer.ui.VideoActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionRvAdapter extends RecyclerView.Adapter<CollectionRvAdapter.MyHolder> {

    List<Video.ResultBean.DataBeanX.ContentBean.DataBean> mVideos;
    Context mContext;

    public CollectionRvAdapter(List<Video.ResultBean.DataBeanX.ContentBean.DataBean> videos, Context context) {
        mVideos = videos;
        mContext = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_rv,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(mContext).load(mVideos.get(position).getCover().getFeed()).into(holder.cover);
        holder.setDuration(mVideos.get(position).getDuration());
        holder.title.setText(mVideos.get(position).getTitle());
        holder.category.setText(mVideos.get(position).getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra("video",mVideos.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView duration,title,category;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_video_image);
            duration = itemView.findViewById(R.id.item_video_dur);
            title = itemView.findViewById(R.id.item_video_title);
            category = itemView.findViewById(R.id.item_video_category);
        }
        public void setDuration(int dur){
            int mm =  dur / 60;
            int ss = dur % 60;
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
