package com.example.administrator.meetingtaste;

/**
 * Created by Administrator on 2017/12/31 0031.
 */

public class HaowuItem {
    public String name;
    public int Icon;
    public int likeIcon;
    public int likeNum;
    public HaowuItem()
    {
        name="鱼香肉丝";
        Icon= R.drawable.best_thing;
        likeIcon= R.drawable.dianzan;
        likeNum=100;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return Icon;
    }

    public int getLikeIcon() {
        return likeIcon;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public void setLikeIcon(int likeIcon) {
        this.likeIcon = likeIcon;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public void setName(String name) {
        this.name = name;
    }

}
