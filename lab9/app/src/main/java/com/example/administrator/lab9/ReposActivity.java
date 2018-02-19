package com.example.administrator.lab9;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lab9.adapter.CardAdapter;
import com.example.administrator.lab9.adapter.MyViewHolder;
import com.example.administrator.lab9.factory.ServiceFactory;
import com.example.administrator.lab9.model.Github;
import com.example.administrator.lab9.model.Repos;
import com.example.administrator.lab9.service.GithubService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReposActivity extends AppCompatActivity {

    List<Repos> reposList;
    CardAdapter<Repos> reposCardAdapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        init();
        getData();
        RecyclerView();
    }

    /**
     * 初始化
     */
    public void init()
    {
        reposList=new ArrayList<>();
        progressBar=(ProgressBar)findViewById(R.id.repos_progressbar);
        recyclerView=(RecyclerView)findViewById(R.id.repos_recycler_view);

    }

    /**
     * 获取数据
     */
    public void getData()
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit= ServiceFactory.createRetrofit("https://api.github.com");
        GithubService service=retrofit.create(GithubService.class);
        String User=getIntent().getExtras().get("login").toString();
        service.getRepos(User)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),"get_error",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<Repos> reposes) {
                        for(Repos item:reposes)
                        {
                            reposList.add(item);

                        }
                        reposCardAdapter.notifyDataSetChanged();
                    }

                }
    );

    }

    /**
     * 设置recyclerView的显示
     */
    public void RecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(ReposActivity.this));
        reposCardAdapter=new CardAdapter<Repos>(this,R.layout.repos_item,reposList) {
            @Override
            public void convert(MyViewHolder viewHolder, Repos data) {
                TextView name=viewHolder.getView(R.id.item_name);
                TextView language=viewHolder.getView(R.id.item_language);
                TextView description=viewHolder.getView(R.id.item_description);
                name.setText(data.getName());
                language.setText(data.getLanguage());
                description.setText(data.getDescription());
            }
        };
        recyclerView.setAdapter(reposCardAdapter);
    }


}
