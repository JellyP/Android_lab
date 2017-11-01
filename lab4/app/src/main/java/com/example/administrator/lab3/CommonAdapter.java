package com.example.administrator.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import java.util.List;
import com.example.administrator.lab3.ViewHolder;


/**
 * Created by Administrator on 2017/10/21 0021.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    private OnItemClickListener mOnItemClickListener=null;
    public CommonAdapter(Context context,int layoutId,List<T> datas)
    {//构造函数
        mContext=context;
        mLayoutId=layoutId;
        mDatas=datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {//创建ViewHolder
        ViewHolder viewHolder=ViewHolder.get(mContext,parent,mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,int position)
    {//这个方法主要用于适配渲染数据到View中。方法提供给你了一个viewHolder，而不是原来的convertView。
        convert(holder,mDatas.get(position));
        if(mOnItemClickListener!=null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }
    public abstract void convert(ViewHolder viewHolder,T data);//需要重写的abstract函数convert
    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }
    public void removeItem(int position)//删除该位置的数据
    {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener
    {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener=onItemClickListener;
    }

}

