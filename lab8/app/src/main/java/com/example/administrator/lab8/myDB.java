package com.example.administrator.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class myDB extends SQLiteOpenHelper {

    private static final String DB_NAME="Contacts.db";
    private static final String TABLE_NAME="Contacts";
    private static final int DB_VERSION=1;
    public myDB(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }
    //数据库第一次创建的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="create table "+TABLE_NAME
                +" (_id integer primary key, "
                +"name text , "
                +"birth text , "
                +"gift text);";
        db.execSQL(CREATE_TABLE);

    }


    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *
     * @param name
     * @param birth
     * @param gift
     */
    public void insert(String name,String birth,String gift)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    /**
     *
     * @param name
     * @param birth
     * @param gift
     */
    public void update(String name,String birth,String gift)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        String whereClause="name=? ";
        String[] whereArgs={name};
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.update(TABLE_NAME,values,whereClause,whereArgs);
        db.close();
    }

    /**
     *
     * @param name
     */
    public void delete(String name)
    {
        SQLiteDatabase db=getWritableDatabase();
        String whereClause="name=? ";
        String[] whereArgs={name};
        db.delete(TABLE_NAME,whereClause,whereArgs);
        db.close();
    }

}
