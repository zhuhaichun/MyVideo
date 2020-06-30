package com.android.haichun.myvideoplayer.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.model.User;
import com.android.haichun.myvideoplayer.retrofitUtils.RetrofitRequest;
import com.android.haichun.myvideoplayer.retrofitUtils.UserApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;

public class InfoActivity extends AppCompatActivity {

    public static final int PICK_PHOTO = 102;
    ImageView exitIv,headIv;
    TextView saveTv,nameTv;
    EditText nickEt,phoneEt,emailEt,signatureEt;
    User mUser;
    String headImg;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_info);
        mUser = (User) getIntent().getSerializableExtra("user");
        init();
    }

    private void init(){
        exitIv = findViewById(R.id.info_exit_iv);
        headIv = findViewById(R.id.info_head_iv);
        saveTv = findViewById(R.id.info_save_tv);
        nameTv = findViewById(R.id.info_name_tv);
        nickEt = findViewById(R.id.info_nick_et);
        phoneEt = findViewById(R.id.info_phone_et);
        emailEt = findViewById(R.id.info_email_et);
        signatureEt = findViewById(R.id.info_signature_et);
        if (mUser != null){
            nameTv.setText("用户名："+mUser.getResult().getName());
            phoneEt.setText(mUser.getResult().getPhone());
            emailEt.setText(mUser.getResult().getEmail());
            signatureEt.setText(mUser.getResult().getAutograph());
            nickEt.setText(mUser.getResult().getNikeName());
            if (!"".equals(mUser.getResult().getHeaderImg())){
                headImg = mUser.getResult().getHeaderImg();
                Glide.with(this).load(headImg)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(headIv);
            }
        }
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //动态申请获取访问 读写磁盘的权限
                if (ContextCompat.checkSelfPermission(InfoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                } else {
                    //打开相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_PHOTO); // 打开相册
                }
            }
        });
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = nickEt.getText().toString();
                String phone = phoneEt.getText().toString();
                String email = emailEt.getText().toString();
                String signature = signatureEt.getText().toString();
                saveUpdate(nick,phone,email,signature);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    headImg = data.getData().toString();
                    Glide.with(this).load(headImg)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(headIv);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //打开相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PHOTO); // 打开相册
            }else{
                Toast.makeText(InfoActivity.this,"权限申请被拒绝，无法选择图片",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("CheckResult")
    private void saveUpdate(String nick, String phone, String email, String signature){
        String password = mUser.getPassword();
        RetrofitRequest.getRetrofit().create(UserApi.class)
                .updateUser(RegisterActivity.API_KEY,
                        mUser.getResult().getName(),
                        password,nick,headImg,phone,email,signature,"")
                .filter(new Predicate<Result<User>>() {
                    @Override
                    public boolean test(Result<User> userResult) throws Exception {
                        return !userResult.isError() && userResult.response().isSuccessful();
                    }
                })
                .map(new Function<Result<User>, User>() {
                    @Override
                    public User apply(Result<User> userResult) throws Exception {
                        return userResult.response().body();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        mUser = user;
                        Log.i("InfoActivity", "accept: "+mUser);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if (mUser.getCode() == 200) {
                            mUser.setPassword(password);
                            Intent intent = new Intent();
                            intent.putExtra("user", mUser);
                            InfoActivity.this.setResult(RESULT_OK, intent);
                            InfoActivity.this.finish();
                        }else{
                            Toast.makeText(InfoActivity.this,"保存失败！"+mUser.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
