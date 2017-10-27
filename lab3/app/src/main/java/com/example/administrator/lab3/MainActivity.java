package com.example.administrator.lab3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {

    /*---商品信息------*/
    private String[] goodsName = new String[]
            {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis"
                    , "waitrose 早餐麦片", "Mcvities's 饼干", "Ferrero Rocher", "Maltesers"
                    , "Lindt", "Borggreve"};
    private int[] imageid = new int[]{R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,
            R.mipmap.maltesers,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};

    private String[] goodsName_Firstletter = new String[]
            {"E", "A", "D", "K"
                    , "w", "M", "F", "M"
                    , "L", "B"};
    private String[] goodsPrice = new String[]
            {"￥5.00", "￥59.00", "￥79.00", "￥2399.00",
                    "￥179.00", "￥14.90", "￥132.59", "￥141.43", "￥139.43", "￥28.90"};
    private String[] goodsType = new String[]
            {"作者", "产地", "产地", "版本", "重量", "产地", "重量", "重量", "重量", "重量"
            };
    private String[] goodsInfo = new String[]
            {
                    "Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg", "英国", "300g", "118g", "249g",
                    "640g"
            };

    /*数据的准备*/
    RecyclerView mRecyclerView;//商品列表的RecyclerView
    List<Goods> goodsDetail;//储存商品信息的一个列表
    List<Map<String,Object>> goodsList;//商品列表
    List<Map<String,Object>> shoppingcarList;//购物车列表
    ListView mListView;//购物车列表的ListView
    boolean exchange;//判断当前为购物车还是主页
    FloatingActionButton floatingActionButton;//浮动按钮
    CommonAdapter commonAdapter;//RecyclerView使用的commonAdapter
    SimpleAdapter simpleAdapter;//ListView使用的simpleAdapter


    public void initial()//初始化
    {
        mRecyclerView=(RecyclerView)findViewById(R.id.mrecyclerview);
        mRecyclerView.setVisibility(View.VISIBLE);//显示商品列表
        goodsDetail=new ArrayList<>();
        floatingActionButton=(FloatingActionButton)findViewById(R.id.shoppingcar);
        goodsList=new ArrayList<>();
        shoppingcarList=new ArrayList<>();
        mListView=(ListView)findViewById(R.id.shoppinglist);
        mListView.setVisibility(View.INVISIBLE);//不显示购物车列表
        exchange=false;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();


        /*goods detail*/
        /*分别向商品列表和购物车列表中加入初始化数据*/
        for(int i=0;i<10;i++)
        {
            Map<String,Object> temp=new HashMap<String,Object>();
            temp.put("goods_Firstletter",goodsName_Firstletter[i]);
            temp.put("goodsname",goodsName[i]);
            temp.put("index",i);
            goodsList.add(temp);
        }
        Map<String,Object> temp1=new HashMap<String,Object>();
        temp1.put("goods_Firstletter","*");
        temp1.put("goodsname","购物车");
        temp1.put("price","价格");
        shoppingcarList.add(temp1);

        /*goods information*/
        /*往goodsDetail里面添加Goods类型的数据*/
        for(int i=0;i<10;i++)
        {
            goodsDetail.add(new Goods(goodsName[i],goodsPrice[i],goodsType[i],goodsInfo[i],goodsName_Firstletter[i],imageid[i],i));
        }
        /*Recylerview goodsList*/
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonAdapter=new CommonAdapter<Map<String,Object>>(this,R.layout.shoppinglist_item,goodsList) {
            @Override
            /*viewholder */
            public void convert(ViewHolder viewHolder, Map<String, Object> data) {
                TextView first=viewHolder.getView(R.id.goodsFirst_letter);
                first.setText(data.get("goods_Firstletter").toString());
                TextView name=viewHolder.getView(R.id.goodsName);
                name.setText(data.get("goodsname").toString());
            }
        };
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position)
            {/*设置点击后进入详情界面*/
                Intent goodlist2details=new Intent(MainActivity.this,GoodDetail.class);
                Bundle bundle1=new Bundle();
                int i=Integer.parseInt(goodsList.get(position).get("index").toString());
                bundle1.putSerializable("goods", goodsDetail.get(i));
                goodlist2details.putExtras(bundle1);
                startActivityForResult(goodlist2details,1);
            }
            @Override
            public void onLongClick(int position)
            {/*长按删除*/
                commonAdapter.removeItem(position);
                Toast.makeText(getApplicationContext(),"移除第"+position+"个商品",Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(commonAdapter);//为RecyclerView设置Adapter

        /*购物车*/
        simpleAdapter=new SimpleAdapter(this,shoppingcarList,R.layout.shoppinglist_item,
                new String[] {"goods_Firstletter","goodsname","price"},new int[] {R.id.goodsFirst_letter,R.id.goodsName,R.id.price});
        mListView.setAdapter(simpleAdapter);
        /*----按一下跳转--------*/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)
                {
                    Intent shoppingcar2details=new Intent(MainActivity.this,GoodDetail.class);
                    Bundle bundle2=new Bundle();
                    int i=Integer.parseInt(shoppingcarList.get(position).get("index").toString());
                    bundle2.putSerializable("goods",goodsDetail.get(i));
                    shoppingcar2details.putExtras(bundle2);
                    startActivityForResult(shoppingcar2details,1);

                }
            }

        });
        /*-----长按--------------------*/
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position>0)
                {
                    String tempString=shoppingcarList.get(position).get("goodsname").toString();

                    final AlertDialog.Builder altert=new AlertDialog.Builder(MainActivity.this);
                    altert.setTitle("移除商品");
                    altert.setMessage("从购物车中移除" + tempString + "?");
                    altert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            shoppingcarList.remove(position);

                            simpleAdapter.notifyDataSetChanged();
                        }
                    })
                         .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 /*nothing happened*/
                                 dialog.cancel();
                             }
                         })
                         .create()
                         .show();
                }
                return true;
            }
        });
        /*----floatingActionButton悬浮按钮，切换购物车和商品列表界面----------*/
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                if(exchange==false)//显示购物车列表
                {
                    mListView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    floatingActionButton.setImageResource(R.mipmap.shoplist);
                    exchange=!exchange;
                }
                else//显示商品列表
                {
                    mListView.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageResource(R.mipmap.mainpage);
                    exchange=!exchange;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {//得到新打开商品详情关闭后返回的数据
        //requestCode提供给onActivityResult，是以便确认返回的数据是从哪个Activity返回的。
        //这个requestCode和startActivityForResult中的requestCode相对应。
        //这整数resultCode是由子Activity通过其setResult()方法返回。
        if(requestCode==1&&resultCode==2)
        {
            Bundle bundle=intent.getExtras();
            if(bundle!=null)
            {
                Goods goods=(Goods)bundle.get("shoppingcar");
                if(goods != null)
                {
                    if(goods.getMarkShopcar()==true)
                    {
                        Map<String,Object> temp=new HashMap<String,Object>();
                        temp.put("goods_Firstletter",goods.getGoods_firstletter());
                        temp.put("goodsname",goods.getName());
                        temp.put("price",goods.getPrice());
                        temp.put("index",goods.getIndex());
                        shoppingcarList.add(temp);
                        simpleAdapter.notifyDataSetChanged();
                    }
                    if(goods.getMarkRemove()==true)
                    {
                        commonAdapter.removeItem(goods.getIndex());
                    }
                }
            }
        }
    }

}
