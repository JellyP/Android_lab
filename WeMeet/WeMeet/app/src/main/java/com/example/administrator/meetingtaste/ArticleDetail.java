package com.example.administrator.meetingtaste;

/**
 * 文章详情 Webview
 */

import android.content.Intent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.meetingtaste.model.Artical;
import com.example.administrator.meetingtaste.model.ArticalComment;
import com.example.administrator.meetingtaste.model.User;
import com.example.administrator.meetingtaste.service.DBFactory;
import com.example.administrator.meetingtaste.service.DBService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ArticleDetail extends AppCompatActivity
{
    private static final String LOGIN_USER="LOGIN_USER";//跳转的时候传输用户ID的key
    private static final String ARTICAL="ARTICAL";
    private static final String ARTICLE_ID="ARTICLE_ID";

    private WebView webview;
    private FloatingActionButton floatingbutton;
    private final User loginUser = new User();

    @SuppressLint("SetJavaScriptEnabled")

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        webview = (WebView) findViewById(R.id.webview);
        floatingbutton = (FloatingActionButton) findViewById(R.id.floatingbutton);
        Intent intent = getIntent();

        Bundle mBundle = getIntent().getExtras();
        DBService dbService = DBFactory.createDBService();
        dbService.queryUserById(((User)mBundle.get(LOGIN_USER)).getUser_id())
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
                    }
                });

        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webview.loadUrl(((Artical)intent.getSerializableExtra(ARTICAL)).getArtical_url());
        final long a = ((Artical)intent.getSerializableExtra(ARTICAL)).getArtical_id();
//        Log.e("e", String.valueOf(a));
        //设置Web视图
        webview.setWebViewClient(new webViewClient ());

        // 悬浮按钮点击事件
        floatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ArticleDetail.this, ArticalCommentList.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(LOGIN_USER, loginUser);
                bundle.putSerializable(ARTICLE_ID, a);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();//结束退出程序
        return false;
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
