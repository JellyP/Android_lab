package com.example.administrator.meetingtaste;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.meetingtaste.model.Artical;
import com.example.administrator.meetingtaste.model.ArticalAndUser;
import com.example.administrator.meetingtaste.model.ArticalComment;
import com.example.administrator.meetingtaste.model.ArticalCommentAndUser;
import com.example.administrator.meetingtaste.model.User;
import com.example.administrator.meetingtaste.service.DBFactory;
import com.example.administrator.meetingtaste.service.DBService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArticalCommentList extends AppCompatActivity {

    /**
     * 文章传至评论列表，需要传输User，和文章ID
     */
    private static final String LOGIN_USER="LOGIN_USER";//跳转的时候传输用户ID的key
    private static final String ARTICLE="ARTICLE";
    private static final String ARTICLE_ID="ARTICLE_ID";

    private User loginUser=new User();
    private Bundle mBundle;
    private Button articalCommenback;
//    private Button editCommentButton;
    private Button sendCommentButton;
    private TextView editComment;
    private RecyclerView articalCommentRecyclerView;
    private CommonAdapter<ArticalCommentAndUser> articalCommentCommonAdapter;
    private List<ArticalCommentAndUser> articalCommentList;
    private TextView newEditComment;
    private DBService dbService= DBFactory.createDBService();

    //recyclerView ITEM
    private ImageView userIcon;
    private TextView userName;
    private TextView userComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artical_comment);
        init();
        setArticalCommenback();
        setArticalCommentRecyclerView();
        setEditCommentButton();
        setSendCommentButton();
    }
    private void init()
    {
        articalCommenback=(Button)findViewById(R.id.artical_comment_back);
//        editCommentButton=(Button)findViewById(R.id.artical_comment_edit_comment);
        sendCommentButton=(Button)findViewById(R.id.artical_comment_submit_comment);
        editComment=(TextView)findViewById(R.id.artical_comment_add_comment);
        articalCommentRecyclerView=(RecyclerView)findViewById(R.id.artical_comment_recycler_view);
        articalCommentList=new ArrayList<>();
        mBundle = getIntent().getExtras();

        dbService.queryUserById(((User)(mBundle.get(LOGIN_USER))).getUser_id())
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
    }
    private void setArticalCommenback()
    {
        articalCommenback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void setArticalCommentRecyclerView()
    {
        //数据处理
        dbService.queryArticalCommentAndUser(((long)(mBundle.get(ARTICLE_ID))),0,10)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<ArticalCommentAndUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<ArticalCommentAndUser> articalCommentAndUsers) {
                        articalCommentList.clear();
                        articalCommentList.addAll(articalCommentAndUsers);
                        articalCommentCommonAdapter.notifyDataSetChanged();
                    }
                });
        articalCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        articalCommentCommonAdapter=new CommonAdapter<ArticalCommentAndUser>(ArticalCommentList.this,R.layout.artical_comment_item,articalCommentList) {
            @Override
            protected void convert(MyViewHolder holder, ArticalCommentAndUser articalComment, int position) {
                userIcon=holder.getView(R.id.artical_comment_item_user_icon);//用户头像
                userName=holder.getView(R.id.artical_comment_user_name);//用户名字
                userComment=holder.getView(R.id.artical_comment_artical_comment);//用户评论
                /**
                 * 设置
                 */
                ImageLoader.getInstance().displayImage(articalComment.getUser().getUser_icon(),userIcon);
                userName.setText(articalComment.getUser().getUser_name());
                userComment.setText(articalComment.getArticalComment().getArtical_comment_content());
            }

        };

        articalCommentRecyclerView.setAdapter(articalCommentCommonAdapter);
    }
    private void setEditCommentButton()
    {
        editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factor = LayoutInflater.from(ArticalCommentList.this);//创建LayoutInflater从MainActivity
                View view_in = factor.inflate(R.layout.comment_edit, null);//绑定设定的对话框layout
                newEditComment=(EditText)view_in.findViewById(R.id.comment_edit);
                newEditComment.setText(editComment.getText().toString());
                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(ArticalCommentList.this);
                alertDialog1
                        .setView(view_in)
                        .setNegativeButton("放弃评论", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("保存评论", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newComment=newEditComment.getText().toString();
                                editComment.setText(newComment);
                            }
                        })

                        .create()
                        .show();

            }
        });
    }
    private void setSendCommentButton()
    {

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=editComment.getText().toString();
                ArticalComment temp=new ArticalComment((long)mBundle.get(ARTICLE_ID),loginUser.getUser_id(),comment);//构建新的评论具体参数待填
                ArticalCommentAndUser articalCommentAndUserTemp=new ArticalCommentAndUser();
                articalCommentAndUserTemp.setArticalComment(temp);
                articalCommentAndUserTemp.setUser(loginUser);

                dbService.addArticalComment(temp)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if(aBoolean.booleanValue())
                                {
                                    setArticalCommentRecyclerView();
                                    editComment.setText("");
                                    Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

    }
}
