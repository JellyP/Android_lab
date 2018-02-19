package com.example.administrator.lab9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lab9.adapter.CardAdapter;
import com.example.administrator.lab9.adapter.MyViewHolder;
import com.example.administrator.lab9.factory.ServiceFactory;
import com.example.administrator.lab9.model.Github;
import com.example.administrator.lab9.service.GithubService;



import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import retrofit2.Retrofit;


    public class MainActivity extends AppCompatActivity {

    EditText search_edit;
    Button clear_button;
    RecyclerView recyclerView;
    CardAdapter<Github> githubCardAdapter;
    List<Github> githubList;
    ProgressBar progressBar;
    Button fetch_button;
    public static int MAIN2REPOS=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        RecyclerView();
        setFetch_button();
        setClear_button();
    }

    /**
     * 初始化
     */
    public void init()
    {
        search_edit=(EditText)findViewById(R.id.search_user);
        clear_button=(Button)findViewById(R.id.clear_button);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        progressBar=(ProgressBar)findViewById(R.id.activity_progressbar);
        fetch_button=(Button)findViewById(R.id.fetch_button);
        githubList=new ArrayList<>();
    }

    /**
     * 设置显示recyclerView的内容
     */
    public void RecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        githubCardAdapter=new CardAdapter<Github>(this,R.layout.user_item,githubList) {
            @Override
            public void convert(MyViewHolder viewHolder, Github data) {
                TextView login=viewHolder.getView(R.id.item_login);
                TextView id=viewHolder.getView(R.id.item_id);
                TextView blog=viewHolder.getView(R.id.item_blog);
                login.setText(data.getLogin());
                id.setText("id:"+data.getId());
                blog.setText("blog:"+data.getBlog());
            }
        };
        githubCardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(MainActivity.this,ReposActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("login",githubList.get(position).getLogin());
                intent.putExtras(bundle);
                startActivity(intent);
                //startActivityForResult(intent,MAIN2REPOS);
            }
            @Override
            public void onLongClick(int position) {
                githubCardAdapter.removeItem(position);
                githubCardAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"移除成功",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(githubCardAdapter);
    }

    /**
     * 设置点击fetch按钮后进行捕捉操作
     */
    public void setFetch_button()
    {
        fetch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = ServiceFactory.createRetrofit("https://api.github.com");
                GithubService service = retrofit.create(GithubService.class);
                String User = search_edit.getText().toString();
                recyclerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                service.getUser(User)//被订阅者订阅订阅者
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Github>() {
                            @Override
                            public void onCompleted() {
                                progressBar.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }
                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getApplicationContext(),"fetch wrong",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }
                            @Override
                            public void onNext(Github github) {
                                githubList.add(github);
                                githubCardAdapter.notifyDataSetChanged();
                            }
                        });

            }
        });
    }

    /**
     * 清除输入框和用户列表
     */
    public void setClear_button()
    {
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edit.setText("");
                githubList.clear();
                githubCardAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"清除成功",Toast.LENGTH_SHORT).show();

            }
        });
    }

}
