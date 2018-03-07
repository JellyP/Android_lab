package com.example.administrator.meetingtaste;

import android.app.ActivityOptions;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.meetingtaste.model.User;
import com.example.administrator.meetingtaste.service.DBFactory;
import com.example.administrator.meetingtaste.service.DBService;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Login extends AppCompatActivity {

//    final private static int USER_CORRECT = 0;//正确输入用户信息
//    final private static int USER_EMPTY_NAME_ERROR = 1;//输入用户名为空
//    final private static int USER_EMPTY_PASSWORD_ERROE = 2;//输入的密码为空
//    final private static int USER_WRONG = 3;//用户名不存在或者密码错误
//    final private static int USER_EMPTY_NAME_PASSWORD_ERROR=4;//用户名和密码都为空
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.login_layout_anonymous)
    Button login_layout_anonymous;
    @InjectView(R.id.username_wrapper)
    TextInputLayout username_wrapper;
    @InjectView(R.id.password_wrapper)
    TextInputLayout password_wrapper;
    @InjectView(R.id.et_username)
    EditText username;
    @InjectView(R.id.et_password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Permission.verifyStoragePermissions(this);

        //初始化DBManager
//        mgr = new DBManager(this,admin,true);

        ButterKnife.inject(this);
    }

    @OnClick({R.id.bt_go, R.id.fab, R.id.login_layout_anonymous})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, Register.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, Register.class));
                }
                break;
            case R.id.bt_go://判断用户是否存在于数据库

                if(username.getText().toString().isEmpty())
                {
                    username_wrapper.setErrorEnabled(true);
                    username_wrapper.setError("用户名不能为空");
                    password_wrapper.setErrorEnabled(false);

//                    Toast.makeText(Login.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().isEmpty())
                {
                    password_wrapper.setErrorEnabled(true);
                    password_wrapper.setError("密码不能为空");
                    username_wrapper.setErrorEnabled(false);
//                    Toast.makeText(Login.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    checkUser();
                }
                break;
            case R.id.login_layout_anonymous:
                Intent intent_1 = new Intent(Login.this, Homepage.class);//跳转至人物列表people_list_layout界面
                Bundle bundle = new Bundle();
                User admin = new User();
                admin.setUser_id("admin");
                admin.setUser_password("admin");
                bundle.putSerializable("LOGIN_USER",admin);
                intent_1.putExtras(bundle);
                startActivity(intent_1);
                break;
        }
    }
    private void checkUser()
    {
        final User queryResult = new User();
        DBService dbService = DBFactory.createDBService();
        dbService.loginUser(username.getText().toString(), password.getText().toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        if(queryResult.getUser_name() == null)
                        {
                            username_wrapper.setErrorEnabled(false);
                            password_wrapper.setErrorEnabled(true);
                            password_wrapper.setError("用户名不存在或者密码错误");
                            // Toast.makeText(Login.this,"用户名不存在或者密码错误",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            username_wrapper.setErrorEnabled(false);
                            password_wrapper.setErrorEnabled(false);
                            Toast.makeText(Login.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Explode explode = new Explode();
                            explode.setDuration(500);
                            //getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(Login.this);
                            Intent i2 = new Intent(Login.this, Homepage.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("LOGIN_USER",queryResult);
                            i2.putExtras(bundle);
                            startActivity(i2, oc2.toBundle());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        if(user != null)
                            queryResult.setUser(user);
                    }
                });

    }
}
