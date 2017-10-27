package com.example.administrator.lab3;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/10/21 0021.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mListView;
    public ViewHolder(Context context, View itemView, ViewGroup parent)
    {//默认构造函数

        super(itemView);
        mListView=itemView;
        mViews=new SparseArray<View>();
    }

    public static ViewHolder get(Context context,ViewGroup parent,int layoutid)
    {
        View itemView= LayoutInflater.from(context).inflate(layoutid,parent
        ,false);//inflate可以将一个xml中定义的布局控件找出来
        ViewHolder holder=new ViewHolder(context,itemView,parent);
        return holder;
    }
    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if(view==null)
        {
            view=mListView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }


}
