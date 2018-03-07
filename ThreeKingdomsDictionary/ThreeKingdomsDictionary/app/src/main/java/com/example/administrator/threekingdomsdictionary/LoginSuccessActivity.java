package com.example.administrator.threekingdomsdictionary;

/**
 * Created by renardbebe on 2017/11/18.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Visibility;

public class LoginSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 进入动画
        Slide slideIn = new Slide();
        slideIn.setDuration(500);
        // 设置为进入
        slideIn.setMode(Visibility.MODE_IN);
        // 设置从右边进入
        slideIn.setSlideEdge(android.view.Gravity.RIGHT);
        getWindow().setEnterTransition(slideIn);

        // 退出动画
        Slide slideReturn = new Slide();
        slideReturn.setDuration(500);
        slideReturn.setSlideEdge(android.view.Gravity.RIGHT);
        slideReturn.setMode(Visibility.MODE_OUT);
        getWindow().setReturnTransition (slideReturn );

        setContentView(R.layout.activity_login_success);

//        Explode explode = new Explode();
//        explode.setDuration(1);
//        getWindow().setExitTransition(explode);
//        getWindow().setEnterTransition(explode);
    }
    public void onBackPressed() {  // 返回键被按下
        Intent intent = new Intent(LoginSuccessActivity.this, PeopleList.class);//跳转至人物列表people_list_layout界面
        startActivity(intent);
    }
}
