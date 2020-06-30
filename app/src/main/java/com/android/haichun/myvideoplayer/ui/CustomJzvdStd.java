package com.android.haichun.myvideoplayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.android.haichun.myvideoplayer.R;

import cn.jzvd.JzvdStd;

public class CustomJzvdStd extends JzvdStd {
    Context mContext;
    public CustomJzvdStd(Context context) {
        super(context);
        mContext = context;
    }

    public CustomJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        SeekBar seekBar = findViewById(R.id.bottom_seek_progress);
        ProgressBar progressBar =findViewById(R.id.bottom_progress);
        seekBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.custom_seek_bar,mContext.getTheme()));
        progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.custom_bottom_progress,mContext.getTheme()));
    }
}
