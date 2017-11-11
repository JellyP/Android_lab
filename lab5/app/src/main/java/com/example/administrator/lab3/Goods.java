package com.example.administrator.lab3;
import java.io.Serializable;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class Goods implements Serializable {
    String name;//名字
    String price;//价格
    String type;//类型，例如产地、重量
    String info;//相应的参数
    String goods_firstletter;//名字的首字母
    int imageid;//对应的图片
    boolean markShopcar;//是否存在于购物车
    boolean markStar;//是否被收藏
    int index;//每个商品唯一的下标，用于区分
    boolean markRemove;//判断是否要移除
    public Goods(String name1,String price1,String type1,String info1,String goods_firstletter1,int imageid1,int i)
    {//构建函数
        name=name1;
        price=price1;
        type=type1;
        info=info1;
        goods_firstletter=goods_firstletter1;
        imageid=imageid1;
        markShopcar=false;
        markStar=false;
        index=i;
        markRemove=false;
    }

    public void setMarkShopcar()
    {
        markShopcar=true;
    }//设置购物车
    public void resetMarkShopcar()
    {
        markShopcar=false;
    }//取消购物车
    public void setMarkStar()
    {
        markStar=true;
    }//设置收藏
    public void resetMarkStar()
    {
        markStar=false;
    }//取消收藏
    public void setMarkRemove() {markRemove=true;}
    public void resetMarkRemove() {markRemove=false;}

    public String getName()
    {
        return name;
    }//获取名字
    public String getPrice()
    {
        return price;
    }//获取价格
    public String getType()
    {
        return type;
    }//获取类型
    public String getInfo()
    {
        return info;
    }//获取相应的参数
    public String getGoods_firstletter() {return goods_firstletter;}//获取首字母
    public int getImageid() {return imageid;}//获取图片id
    public int getIndex() {return index;}//获取id
    public boolean getMarkShopcar() {return markShopcar;}//获取购物车的状态
    public boolean getMarkStar() {return markStar;}//获取收藏的状态
    public boolean getMarkRemove() {return markRemove;}

}
