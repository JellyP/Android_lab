package com.example.administrator.lab3;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodDetail extends AppCompatActivity {

    ImageView goodsimage;//图片
    TextView detailname;//名字
    ImageView star;//收藏星星
    ImageView backbutton;//返回按键
    TextView detailprice;//价格
    TextView detailtype;//类型
    TextView detailinfo;//相应的参数
    ImageView detail_shoppingcar;//购物车按键
    TextView detailmoredetails;//更多
    ListView mListView;//列表用于显示"一键下单","分享商品","不感兴趣","查看更多商品信息"
    SimpleAdapter simpleAdapter;
    List<Map<String,Object>> othersList;
    Goods goods;
    String others[]=new String[]{"一键下单","分享商品","不感兴趣","查看更多商品信息",""};

    Bundle data_back =new Bundle();

    protected void initial()//初始化
    {
        goodsimage=(ImageView)findViewById(R.id.goodsImage);
        detailname=(TextView)findViewById(R.id.detail_name);
        star=(ImageView)findViewById(R.id.star);
        backbutton=(ImageView)findViewById(R.id.backbutton);
        detailprice=(TextView)findViewById(R.id.detail_price);
        detailtype=(TextView)findViewById(R.id.detail_type);
        detailinfo=(TextView)findViewById(R.id.detail_info);
        detail_shoppingcar=(ImageView)findViewById(R.id.detail_shoppingcar);
        detailmoredetails=(TextView)findViewById(R.id.detail_moredetails);
        mListView=(ListView)findViewById(R.id.othersList);
        othersList=new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_details);

        initial();

        Bundle tmp = getIntent().getExtras();
        //获取从商品列表中发送的数据
        if(tmp != null) goods = (Goods) tmp.get("goods");
        goods.resetMarkShopcar();//初始化为未加入购物车
        goods.resetMarkStar();//初始化为未加入收藏

        if(goods!=null)
        {
            detailname.setText(goods.getName());
            detailtype.setText(goods.getType());
            detailinfo.setText(goods.getInfo());
            detailprice.setText(goods.getPrice());
            goodsimage.setImageResource(goods.getImageid());
        }

        /*star设置星星被点击的动作设定*/
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goods.getMarkStar()==true)
                {
                    goods.resetMarkStar();
                    star.setImageResource(R.mipmap.empty_star);
                }
                else
                {
                    goods.setMarkStar();
                    star.setImageResource(R.mipmap.full_star);
                }
            }
        });

        /*shoppingcar购物车的动作设定*/
        detail_shoppingcar.setClickable(true);
        detail_shoppingcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data_back.putSerializable("shoppingcar",goods);
                goods.setMarkShopcar();
                Toast.makeText(getApplicationContext(),"商品已添加到购物车",Toast.LENGTH_SHORT).show();
            }
        });

        /*back返回操作*/
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail2goodlist=new Intent();
                detail2goodlist.putExtra("shoppingcar",goods);
                setResult(2,detail2goodlist);
                finish();
            }
        });




        /*List的设置，放置"一键下单","分享商品","不感兴趣","查看更多商品信息"*/
        for(int i=0;i<others.length;i++)
        {
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("others", others[i]);
            othersList.add(temp);
        }
        simpleAdapter=new SimpleAdapter(this,othersList,R.layout.goods_details_other,
                new String[]{"others"},new int[]{R.id.others});
        mListView.setAdapter(simpleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override/*附加部分*/
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    data_back.putSerializable("shoppingcar",goods);
                    goods.setMarkShopcar();
                    Toast.makeText(getApplicationContext(),"商品已添加到购物车",Toast.LENGTH_SHORT).show();
                    Intent detail2goodlist=new Intent();
                    detail2goodlist.putExtra("shoppingcar",goods);
                    setResult(2,detail2goodlist);
                    finish();
                }
                if(position==1)
                {
                    Toast.makeText(getApplicationContext(),"暂未实现该功能",Toast.LENGTH_SHORT).show();
                }
                if(position==2)
                {
                    goods.setMarkRemove();
                    data_back.putSerializable("shoppingcar",goods);
                    Toast.makeText(getApplicationContext(),"商品已被删除",Toast.LENGTH_SHORT).show();
                    Intent detail2goodlist=new Intent();
                    detail2goodlist.putExtra("shoppingcar",goods);
                    setResult(2,detail2goodlist);
                    finish();
                }
                if(position==3)
                {
                    Intent detail2goodlist=new Intent();
                    detail2goodlist.putExtra("shoppingcar",goods);
                    setResult(2,detail2goodlist);
                    finish();
                }
            }
        });



    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent keyEvent)
    {/**/
        if(KeyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent(GoodDetail.this,MainActivity.class);
            //intent.putExtra("shoppingcar",goods);
            intent.putExtras(data_back);
            setResult(2,intent);
            finish();
        }
        return true;
    }
}
