package com.android.haichun.myvideoplayer.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.adapter.VpAdapter;
import com.android.haichun.myvideoplayer.fragment.CategoryListFragment;
import com.android.haichun.myvideoplayer.fragment.TodayFragment;
import com.android.haichun.myvideoplayer.model.Category;
import com.android.haichun.myvideoplayer.model.User;
import com.android.haichun.myvideoplayer.retrofitUtils.CategoryApi;
import com.android.haichun.myvideoplayer.retrofitUtils.RetrofitRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import cn.jzvd.Jzvd;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int RESULT_LOGIN = 111;
    public static final int RESULT_UPDATE = 222;
    List<String> mCategories;
    List<Fragment> mFragments;
    VpAdapter mVpAdapter;
    ImageView drawerHead;
    TextView promptTv,updateTv,exitTv,localTv,collectionTv,heartTv;
    static User mUser;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.main_view_pager);
        PagerTabStrip tabStrip = findViewById(R.id.main_pager_tab);
        mCategories = new ArrayList<>();
        mFragments = new ArrayList<>();
        mVpAdapter = new VpAdapter(getSupportFragmentManager(),mFragments,mCategories);
        viewPager.setAdapter(mVpAdapter);
        SlidingRootNav bav = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.left_drawer)
                .inject();
        drawerHead =  bav.getLayout().findViewById(R.id.drawer_head_iv);
        promptTv = bav.getLayout().findViewById(R.id.login_prompt);
        updateTv = bav.getLayout().findViewById(R.id.drawer_set_info_tv);
        exitTv = bav.getLayout().findViewById(R.id.drawer_change_tv);
        localTv = bav.getLayout().findViewById(R.id.drawer_local_tv);
        collectionTv = bav.getLayout().findViewById(R.id.drawer_collection_tv);
        heartTv = bav.getLayout().findViewById(R.id.drawer_heart_tv);
        Glide.with(this).load(R.drawable.no_head).into(drawerHead);
        drawerHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser == null) {
                    startLoginFotResult();
                    bav.closeMenu();
                }
            }
        });
        updateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra("user",mUser);
                    startActivityForResult(intent,RESULT_LOGIN);
                }else{
                    startLoginFotResult();
                    bav.closeMenu();
                }
            }
        });
        exitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginFotResult();
                finish();
            }
        });
        localTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LocalActivity.class);
                startActivity(intent);
            }
        });
        collectionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser == null){
                    Toast.makeText(MainActivity.this,"请先登录再查看收藏",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this,CollectionActivity.class);
                    startActivity(intent);
                    bav.closeMenu();
                }
            }
        });
        heartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser == null){
                    Toast.makeText(MainActivity.this,"请先登录再查看我喜欢",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this,HeartActivity.class);
                    startActivity(intent);
                    bav.closeMenu();
                }
            }
        });
        getCategory();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Jzvd.releaseAllVideos();
        Jzvd.goOnPlayOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Jzvd.goOnPlayOnResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOGIN){
                assert data != null;
                mUser = (User) data.getSerializableExtra("user");
                if (mUser != null) {
                    if (!"".equals(mUser.getResult().getHeaderImg())) {
                        Glide.with(this).load(mUser.getResult().getHeaderImg())
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(drawerHead);
                    }
                    if ("".equals(mUser.getResult().getNikeName())){
                        promptTv.setVisibility(View.GONE);
                    }else{
                        promptTv.setText(mUser.getResult().getNikeName());
                    }
                }
            }
        }
    }

    private void startLoginFotResult(){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivityForResult(intent,RESULT_LOGIN);
    }

    @SuppressLint("CheckResult")
    private void getCategory() {
        Observable<Result<Category>> observable = RetrofitRequest.getRetrofit()
                .create(CategoryApi.class).getCategory();
        observable
                .filter(categoryResult -> !categoryResult.isError() && categoryResult.response().isSuccessful())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result<Category>>() {
                    @Override
                    public void accept(Result<Category> categoryResult) throws Exception {
                        List<Category.ResultBean.ItemListBean> itemList = categoryResult.response().body().getResult().getItemList();
                        mFragments.add(new TodayFragment());
                        mCategories.add("#推荐");
                        for (Category.ResultBean.ItemListBean bean : itemList){
                            mFragments.add(new CategoryListFragment(bean.getData().getId()));
                            mCategories.add(bean.getData().getTitle());
                        }
                        mVpAdapter.notifyDataSetChanged();
                    }
                });
    }

}
