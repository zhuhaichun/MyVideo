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
import com.android.haichun.myvideoplayer.model.RecommendVideo;
import com.android.haichun.myvideoplayer.model.Video;
import com.android.haichun.myvideoplayer.ui.VideoActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0; //说明是带有Header的
    public static final int TYPE_FOOTER = 1; //说明是带有Footer的
    public static final int TYPE_NORMAL = 2; //说明是不带有header和footer的

    //获取从Activity中传递过来每个item的数据集合
    private List<RecommendVideo.ResultBean> mDatas;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private Context mContext;

    //构造函数
    public VideoRvAdapter(List<RecommendVideo.ResultBean> list,Context context) {
        this.mDatas = list;
        this.mContext = context;
    }


    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view *
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if ( mHeaderView!=null && position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (mFooterView!=null && position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new MyHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new MyHolder(mFooterView);
        }
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_video_rv, parent, false);
        return new MyHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的， HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("VideoRv", "onBindViewHolder: "+getItemViewType(position));
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof MyHolder) {
                Video.ResultBean.DataBeanX.ContentBean.DataBean dataBean = mDatas.get(getRealPosition(holder)).getData();
                Glide.with(mContext)
                        .load(dataBean.getCover().getFeed())
                        .into(((MyHolder) holder).videoImage);
                ((MyHolder) holder).setDurTxt(dataBean.getDuration());
                ((MyHolder) holder).title.setText(dataBean.getTitle());
                ((MyHolder) holder).category.setText(dataBean.getCategory());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, VideoActivity.class);
                        intent.putExtra("video",mDatas.get(getRealPosition(holder)).getData());
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return mDatas.size();
        } else if (mHeaderView == null && mFooterView != null) {
            return mDatas.size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return mDatas.size() + 1;
        } else {
            return mDatas.size() + 2;
        }
    }
    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        return mHeaderView == null ? position : position - 1;
    }

    //在这里面加载ListView中的每个item的布局
    class MyHolder extends RecyclerView.ViewHolder {
        ImageView videoImage;
        TextView dur, title, category;

        MyHolder(View itemView) {
            super(itemView);
            //如果是headerView或者是footerView,直接返回
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            videoImage = itemView.findViewById(R.id.item_video_image);
            dur = itemView.findViewById(R.id.item_video_dur);
            title = itemView.findViewById(R.id.item_video_title);
            category = itemView.findViewById(R.id.item_video_category);
        }
        public void setDurTxt(int duration){
            int m = duration / 60;
            int s = duration % 60;
            String text;
            if (m < 10){
                text = "0"+m+":";
            }else{
                text = m+":";
            }
            if (s < 10){
                text += "0"+s;
            }else{
                text += s;
            }
            dur.setText(text);
        }
    }


}
