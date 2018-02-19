package com.example.administrator.lab9.adapter;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
/**
 * Created by Administrator on 2017/12/21 0021.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mListView;
    public MyViewHolder(Context context, View itemView, ViewGroup parent)
    {//默认构造函数

        super(itemView);
        mListView=itemView;
        mViews=new SparseArray<View>();
    }

    public static MyViewHolder get(Context context,ViewGroup parent,int layoutid)
    {
        View itemView= LayoutInflater.from(context).inflate(layoutid,parent
                ,false);//inflate可以将一个xml中定义的布局控件找出来
        MyViewHolder holder=new MyViewHolder(context,itemView,parent);
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
