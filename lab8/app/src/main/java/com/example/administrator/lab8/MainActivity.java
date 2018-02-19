package com.example.administrator.lab8;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button add_item;
    ListView item_list;
    List<Map<String,Object>> data;
    SimpleAdapter simpleAdapter;
    myDB db;

    TextView dialog_name;
    EditText dialog_birth;
    EditText dialog_gift;
    TextView dialog_number;

    static final String TABLE_NAME="Contacts";
    static final int REQUEST_CODE=123;
    static final int RESULT_CODE=321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        list_update();
        list_delete();

        item_update();
        setAdd_item();
    }

    private void init()
    {
        add_item=(Button)findViewById(R.id.add_item_button);
        item_list=(ListView)findViewById(R.id.item_list);
        db=new myDB(MainActivity.this);
        data=new ArrayList<>();
    }
    private void list_update()
    {
        data.clear();//列表显示的内容
        SQLiteDatabase sqLiteDatabase=db.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        //读取数据库中的值
        if(cursor!=null)
        {
            while(cursor.moveToNext())
            {
                int name_index=cursor.getColumnIndex("name");
                int birth_index=cursor.getColumnIndex("birth");
                int gift_index=cursor.getColumnIndex("gift");
                Map<String,Object> map=new LinkedHashMap<>();
                String name=cursor.getString(name_index);
                String birth=cursor.getString(birth_index);
                String gift=cursor.getString(gift_index);
                map.put("name",name);
                map.put("birth",birth);
                map.put("gift",gift);
                data.add(map);
            }
        }

        simpleAdapter=new SimpleAdapter(this,data,R.layout.list_item,
                new String[]{"name","birth","gift"},
                new int[]{R.id.name,R.id.birth,R.id.gift});
        item_list.setAdapter(simpleAdapter);
    }

    private void list_delete()
    {
        item_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("是否删除")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.delete(data.get(position).get("name").toString());
                                data.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();

                return true;//如果这个长按事件被消耗掉了，那么就返回true,如果没有被消耗掉，那么返回false.
            }
        });
    }
    private void item_update()
    {
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
                }//检查权限

                LayoutInflater factor = LayoutInflater.from(MainActivity.this);//创建LayoutInflater从MainActivity
                View view_in = factor.inflate(R.layout.dialog_layout, null);//绑定设定的对话框layout

                //绑定相应的控件
                dialog_name=(TextView)view_in.findViewById(R.id.dialog_name);
                dialog_birth=(EditText)view_in.findViewById(R.id.dialog_birth);
                dialog_gift=(EditText)view_in.findViewById(R.id.dialog_gift);
                dialog_number=(TextView)view_in.findViewById(R.id.dialog_number);
                //设置显示内容
                dialog_name.setText((CharSequence)data.get(position).get("name"));
                dialog_birth.setText((CharSequence)data.get(position).get("birth"));
                dialog_gift.setText((CharSequence)data.get(position).get("gift"));

                Cursor cursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                cursor.moveToFirst();//移动到第一行
                int isHas=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                String number="";
                if(isHas>0)
                {
                    Cursor phone=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "=?" , new String[] {data.get(position).get("name").toString()}, null);
                    //查询联系人的名字
                    number="";
                    if(phone.getCount()>0) //有这个人的存在
                    {
                        phone.moveToFirst();//显示第一个人的名字
                        number+=phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    else
                    {
                        number="无";
                    }
                }
                else
                {
                    number="无";
                }
                dialog_number.setText((CharSequence)number);
                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(MainActivity.this);
                alertDialog1.setTitle("发起进攻(　 ´-ω ･)▄︻┻┳══━一")
                        .setView(view_in)
                        .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name=data.get(position).get("name").toString();
                                String birth=dialog_birth.getText().toString();
                                String gift=dialog_gift.getText().toString();
                                db.update(name,birth,gift);
                                list_update();
                            }
                        })
                        .setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void setAdd_item()
    {
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,add_layout.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode==REQUEST_CODE&&resultCode==RESULT_CODE)
        {
            list_update();
        }
    }


}
