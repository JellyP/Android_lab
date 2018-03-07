package com.example.administrator.meetingtaste;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.meetingtaste.model.User;
import com.example.administrator.meetingtaste.service.DBFactory;
import com.example.administrator.meetingtaste.service.DBService;
import com.zaaach.citypicker.CityPickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 编辑个人信息
 */
public class EditMessage extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_CITY = 233;

    private String USER_ID;
    private static final int RESULT_EDIT=666;
    private static final String LOGIN_USER="LOGIN_USER";//跳转的时候传输用户ID的key
    private User loginUser=new User();
    private String picture_name;

    protected Button edit_message_back;
    protected EditText edit_message_name;
    protected RadioGroup edit_message_sex_group;
    protected EditText edit_message_second_name;
    protected Button edit_message_choose_city;
    protected TextView edit_message_city_message;
    protected Button edit_message_choose_picture;
    protected TextView edit_message_picture_message;
    protected EditText edit_message_story;
    protected Button edit_message_submit;
    /**
     * 初始化,获取各个控件实例化对象
     */
    protected void init()
    {
        edit_message_back = (Button) findViewById(R.id.edit_message_back);
        edit_message_name = (EditText) findViewById(R.id.edit_message_name);
        edit_message_sex_group = (RadioGroup) findViewById(R.id.edit_message_sex_group);
        edit_message_second_name = (EditText) findViewById(R.id.edit_message_second_name);
        
        edit_message_choose_city = (Button)findViewById(R.id.edit_message_choose_city);
        edit_message_city_message = (TextView) findViewById(R.id.edit_message_city_message);
        
        edit_message_choose_picture = (Button)findViewById(R.id.edit_message_choose_picture);
        edit_message_picture_message = (TextView)findViewById(R.id.edit_message_picture_message);
       
        edit_message_story = (EditText) findViewById(R.id.edit_message_story);
        edit_message_submit = (Button) findViewById(R.id.edit_message_submit);

        USER_ID = getIntent().getExtras().getString("USER_ID");

        DBService dbService = DBFactory.createDBService();
        dbService.queryUserById(USER_ID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        loginUser.setUser(user);
                        edit_message_name.setText(user.getUser_name());
                        edit_message_second_name.setText(user.getUser_phone());
                        edit_message_story.setText(user.getUser_description());
                        edit_message_city_message.setText(user.getUser_city());
                    }
                });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
//        mgr.closeDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        //获取实例化对象
        this.init();

        //设置完成按钮
        edit_message_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User updateUser = new User();
                updateUser.setUser_id(USER_ID);
                updateUser.setUser_name(edit_message_name.getText().toString());
                updateUser.setUser_gender(edit_message_sex_group.getCheckedRadioButtonId() == R.id.edit_message_male);
                updateUser.setUser_phone(edit_message_second_name.getText().toString());
                updateUser.setUser_city(edit_message_city_message.getText().toString());
                updateUser.setUser_description(edit_message_story.getText().toString());
                DBService dbService = DBFactory.createDBService();
                dbService.updateUser(updateUser)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {
                                Intent intent=getIntent();
                                Bundle bundle=intent.getExtras();
                                bundle.putSerializable(LOGIN_USER,loginUser);
                                setResult(RESULT_EDIT,intent);
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("*****", e.getMessage());
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if(aBoolean)
                                {
                                    Toast.makeText(getApplicationContext(), "设置完成", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        // 选择城市
        edit_message_choose_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditMessage.this, CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
            }
        });
        // 选择头像
        edit_message_choose_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditMessage.this, PickPicture.class);
                startActivityForResult(intent,100);
            }
        });
        // 设置返回按钮
        edit_message_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                Bundle bundle=intent.getExtras();
                bundle.putSerializable(LOGIN_USER,loginUser);
                setResult(RESULT_EDIT,intent);
                finish();
//                EditMessage.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
        if(requestCode==100&&resultCode==101)
        {
            Bundle bundle=intent.getExtras();
            if(bundle!=null)
            {
                String filePath=bundle.get("filePath").toString();
                if(filePath!=null)
                {
                    edit_message_picture_message.setText(filePath);
                    uploadPicture(USER_ID, filePath);
                }
            }
        }
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (intent != null){
                String city = intent.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                edit_message_city_message.setText(city);
            }
        }
    }

    protected void uploadPicture(final String user_id, final String icon_path)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(icon_path);
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(User.USER_ID,user_id);
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                builder.addFormDataPart("icon",file.getName(),imageBody);
                List<MultipartBody.Part> partList = builder.build().parts();
                DBService service = DBFactory.createDBService();
                service.uploadUserIcon(partList)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("errrrrrr",e.getMessage());

                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if(aBoolean.booleanValue())Toast.makeText(getApplicationContext(),"SUCCESS",Toast.LENGTH_SHORT).show();
                                else Toast.makeText(getApplicationContext(),"FAILED",Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            bundle.putSerializable(LOGIN_USER,loginUser);
            setResult(RESULT_EDIT,intent);
            finish();
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode,event);
        }
    }
}
