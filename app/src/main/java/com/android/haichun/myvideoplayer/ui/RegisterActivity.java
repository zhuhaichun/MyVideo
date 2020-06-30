package com.android.haichun.myvideoplayer.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.model.User;
import com.android.haichun.myvideoplayer.retrofitUtils.RetrofitRequest;
import com.android.haichun.myvideoplayer.retrofitUtils.UserApi;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;

public class RegisterActivity extends AppCompatActivity {

    public static final int LOGIN = 0;
    public static final int REGISTER = 1;
    public static final String API_KEY = "5903327fd8ff69eaa8af48bd6a1aa938";
    private static final String TAG = "RegisterActivity";
    TextView changeTxt;
    ImageView exitIv;
    EditText loginName, loginPw, registerName, registerPw, registerPhone, registerEmail;
    Button loginBt, registerBt;
    View registerContainer, loginContainer;
    int currScreen = LOGIN;
    User mUser;
    Action completeAction = new Action() {
        @Override
        public void run() throws Exception {
            if (mUser.getCode() == 200) {
                Intent intent = new Intent();
                mUser.setPassword(currScreen == LOGIN?loginPw.getText().toString():registerPw.getText().toString());
                intent.putExtra("user", mUser);
                RegisterActivity.this.setResult(RESULT_OK, intent);
                RegisterActivity.this.finish();
            }else{
                Toast.makeText(RegisterActivity.this,"登录/注册失败！"+mUser.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    };
    Consumer<User> nextConsumer = new Consumer<User>() {
        @Override
        public void accept(User user) throws Exception {
            mUser = user;
            Log.i(TAG, "accept: "+mUser.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        registerContainer = findViewById(R.id.register_container);
        loginContainer = findViewById(R.id.login_container);
        changeTxt = findViewById(R.id.change_txt);
        loginName = findViewById(R.id.login_name_et);
        loginPw = findViewById(R.id.login_pw_et);
        registerName = findViewById(R.id.register_name_et);
        registerPw = findViewById(R.id.register_pw_et);
        registerPhone = findViewById(R.id.register_mobile_et);
        registerEmail = findViewById(R.id.register_email_et);
        registerBt = findViewById(R.id.register_login_bt);
        loginBt = findViewById(R.id.login_bt);
        exitIv = findViewById(R.id.register_exit_iv);
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currScreen == REGISTER) {
                    registerContainer.setVisibility(View.GONE);
                    loginContainer.setVisibility(View.VISIBLE);
                    currScreen = LOGIN;
                } else if (currScreen == LOGIN) {
                    registerContainer.setVisibility(View.VISIBLE);
                    loginContainer.setVisibility(View.GONE);
                    currScreen = REGISTER;
                }
            }
        });
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = loginName.getText().toString();
                String password = loginPw.getText().toString();
                if ("".equals(name) || "".equals(password)){
                    Toast.makeText(RegisterActivity.this,"用户名或密码为空",Toast.LENGTH_SHORT).show();
                }else{
                    login(name,password);
                }
            }
        });
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerName.getText().toString();
                String password = registerPw.getText().toString();
                String phone = registerPhone.getText().toString();
                String email = registerEmail.getText().toString();
                if ("".equals(name) || "".equals(password) || "".equals(phone) || "".equals(email)){
                    Toast.makeText(RegisterActivity.this,"所有信息都不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    register(name,password,phone,email);
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void login(String name, String password) {
        RetrofitRequest.getRetrofit().create(UserApi.class)
                .login(API_KEY, name, password)
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
                .subscribe(nextConsumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, completeAction);
    }

    @SuppressLint("CheckResult")
    private void register(String name, String password, String phone, String email) {
        RetrofitRequest.getRetrofit().create(UserApi.class)
                .regUser(API_KEY, name, password, name, "", phone, email)
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
                .subscribe(nextConsumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                },completeAction);
    }
}
