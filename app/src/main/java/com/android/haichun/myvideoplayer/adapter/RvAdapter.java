package com.android.haichun.myvideoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.model.Video;
import com.android.haichun.myvideoplayer.ui.CustomJzvdStd;
import com.android.haichun.myvideoplayer.ui.VideoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.Jzvd;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {

    private static final String TAG = "RvAdapter";
    List<Video.ResultBean> mDataList;
    Context mContext;

    public RvAdapter(List<Video.ResultBean> dataList, Context context) {
        mDataList = dataList;
        mContext = context;
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_rv, parent, false);
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
        Video.ResultBean bean = mDataList.get(position);
        Log.i(TAG, "onBindViewHolder: "+ bean.getData().getHeader().getIcon());
        if (bean.getData().getHeader().getIcon() != null)
            Glide.with(mContext)
                    .load(bean.getData().getHeader().getIcon())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(holder.headIv);
        holder.heartTv.setText(bean.getData().getContent().getData().getConsumption().getCollectionCount()+"");
        holder.commentTv.setText(bean.getData().getContent().getData().getConsumption().getReplyCount()+"");
        holder.nameTv.setText(bean.getData().getHeader().getTitle());
        holder.mJzvdStd.setUp(
                bean.getData().getContent().getData().getPlayUrl(),
                bean.getData().getContent().getData().getTitle(),
                Jzvd.SCREEN_NORMAL);
        Glide.with(mContext)
                .load(bean.getData().getContent().getData().getCover().getFeed())
                .into(holder.mJzvdStd.posterImageView);
        holder.bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra("video",bean.getData().getContent().getData());
                long currPlayPosition =  holder.mJzvdStd.getCurrentPositionWhenPlaying();
                intent.putExtra("playPosition",currPlayPosition);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv,heartTv,commentTv;
        CustomJzvdStd mJzvdStd;
        ImageView headIv;
        View bottom;
        public RvViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.item_rv_name);
            heartTv = itemView.findViewById(R.id.item_rv_heart);
            commentTv = itemView.findViewById(R.id.item_rv_comment);
            headIv = itemView.findViewById(R.id.item_rv_head_iv);
            mJzvdStd = itemView.findViewById(R.id.item_jzvd_std);
            bottom = itemView.findViewById(R.id.item_rv_bottom);
            mJzvdStd.posterImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
