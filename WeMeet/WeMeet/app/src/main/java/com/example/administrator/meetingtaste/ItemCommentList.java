package com.example.administrator.meetingtaste;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.meetingtaste.model.ArticalComment;
import com.example.administrator.meetingtaste.model.ItemComment;
import com.example.administrator.meetingtaste.model.ItemCommentAndUser;
import com.example.administrator.meetingtaste.model.Items;
import com.example.administrator.meetingtaste.model.ShoppingCar;
import com.example.administrator.meetingtaste.model.User;
import com.example.administrator.meetingtaste.service.DBFactory;
import com.example.administrator.meetingtaste.service.DBService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ItemCommentList extends AppCompatActivity {
    private static final String STATICACTION = "com.example.administrator.meetingtaste.MyStaticFilter";

    private static final String DIANPU_ITEM="DIANPU_ITEM";//跳转到店铺物品评论的时候的key
    private static final String LOGIN_USER="LOGIN_USER";//跳转的时候传输用户ID的key

    private Button submitComment;
    private Button itemCommentBack;
    private TextView comment;
    private ImageView itemImageView;
    private TextView itemName;
    private TextView itemPrice;
//    private Button editButton;
    private Button addToShoppingCar;
    private RecyclerView itemCommentRecyclerView;
    private CommonAdapter<ItemCommentAndUser> itemCommentCommonAdapter;
    private List<ItemCommentAndUser> itemCommentList;
    private EditText newEditComment;
    private DBService dbService= DBFactory.createDBService();
    private Bundle commentBundle;

    private User loginUser=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_comment);
        init();
        setItemCommentBack();
        setItemMessage();
        setAddToShoppingCar();
        setRecyclerView();
        setEditButton();
        setSubmitComment();
    }
    //初始化
    private void init()
    {
        submitComment=(Button)findViewById(R.id.itemt_comment_submit_comment);
        itemCommentBack=(Button)findViewById(R.id.item_comment_back);
        comment=(TextView) findViewById(R.id.item_comment_add_comment);
        itemImageView=(ImageView)findViewById(R.id.item_comment_item_image);
        itemName=(TextView)findViewById(R.id.item_comment_name);
        itemPrice=(TextView)findViewById(R.id.item_comment_price);
        itemCommentRecyclerView =(RecyclerView)findViewById(R.id.item_comment_recyclerview);
//        editButton=(Button)findViewById(R.id.item_comment_edit_comment);
        addToShoppingCar=(Button)findViewById(R.id.item_comment_shoppingcar);
        itemCommentList = new ArrayList<>();
        commentBundle=getIntent().getExtras();
        dbService.queryUserById(((User)commentBundle.get(LOGIN_USER)).getUser_id())
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
                });;
    }

    private void setItemCommentBack() {
        itemCommentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    /**
     * 添加购物车
     */
    private void setAddToShoppingCar()
    {
        addToShoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCar car=new ShoppingCar(loginUser.getUser_id(),(long)commentBundle.get(DIANPU_ITEM),1);
                //新建一个购物车的类
                dbService.addItemToShoppingcar(car)//加入购物车
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
                                if (aBoolean.booleanValue())
                                {
                                    //发送广播
                                    Intent intent = new Intent(STATICACTION);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("ItemID", (long)commentBundle.get(DIANPU_ITEM));
                                    intent.putExtras(bundle);
                                    sendBroadcast(intent);
                                    Toast.makeText(getApplicationContext(),"添加购物车成功",Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"添加购物车失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    /**
     * 设置item的信息，itemImageView,itemName,itemPrice
     */

    private void setItemMessage()//设置物品信息
    {
//        Log.d("test",Long.toString((long)commentBundle.get(DIANPU_ITEM)));
        dbService.queryItemByItemId((long)commentBundle.get(DIANPU_ITEM))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Items>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Items items) {
                        ImageLoader.getInstance().displayImage(items.getItem_picture_url(),itemImageView);
                        itemName.setText(items.getItem_name());
                        itemPrice.setText(items.getItem_price().toString());
                    }
                });
    }
    /**
     *
     */
    private void setRecyclerView()//设置评论区显示
    {
        /**
         * 设置list
         */
        dbService.queryItemCommentAndUser((long)commentBundle.get(DIANPU_ITEM),0,10)
                //显示10条
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<ItemCommentAndUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<ItemCommentAndUser> itemCommentAndUsers) {
                        itemCommentList.clear();
                        itemCommentList.addAll(itemCommentAndUsers);
                        itemCommentCommonAdapter.notifyDataSetChanged();
                    }
                });
        itemCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemCommentCommonAdapter=new CommonAdapter<ItemCommentAndUser>(ItemCommentList.this,R.layout.item_comment_item, itemCommentList) {
            @Override
            protected void convert(MyViewHolder holder, ItemCommentAndUser itemComment, int position) {
                ImageView userIcon=holder.getView(R.id.item_comment_item_user_icon);
                TextView userName=holder.getView(R.id.item_comment_user_name);
                TextView userComment=holder.getView(R.id.item_comment_item_comment);
                //设置用户的头像和名字和评论内容
                ImageLoader.getInstance().displayImage(itemComment.getUser().getUser_icon(),userIcon);
                userName.setText(itemComment.getUser().getUser_name());
                userComment.setText(itemComment.getItemComment().getItem_comment_content());

            }
        };
        itemCommentRecyclerView.setAdapter(itemCommentCommonAdapter);
    }
    private void setEditButton()
    {
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factor = LayoutInflater.from(ItemCommentList.this);//创建LayoutInflater从MainActivity
                View view_in = factor.inflate(R.layout.comment_edit, null);//绑定设定的对话框layout
                newEditComment=(EditText)view_in.findViewById(R.id.comment_edit);
                newEditComment.setText(comment.getText().toString());


                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(ItemCommentList.this);
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
                                comment.setText(newComment);
                            }
                        })

                        .create()
                        .show();
            }
        });
    }
    private void setSubmitComment()//发送按钮点击后
    {
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editComment=comment.getText().toString();//发送的评论内容
                ItemCommentAndUser temp=new ItemCommentAndUser();//ItemCommentAndUser来保存用户和评论
                ItemComment itemCommenttemp=new ItemComment((long)commentBundle.get(DIANPU_ITEM),loginUser.getUser_id(),editComment);
                //设置内容
                dbService.addItemComment(itemCommenttemp)//添加评论
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
                                    setRecyclerView();//刷新评论区
                                    comment.setText("");
                                    Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                temp.setItemComment(itemCommenttemp);
                temp.setUser(loginUser);

               // final User userTemp=new User();


            }
        });
    }

}
