package com.example.administrator.lab8;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_layout extends AppCompatActivity {

    EditText name_edit;
    EditText birth_edit;
    EditText gift_edit;
    Button add_button;

    myDB db;
    static final String TABLE_NAME="Contacts";
    static final int RESULT_CODE=321;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layout);
        init();
        setAdd_button();
    }

    private void init()
    {
        name_edit=(EditText)findViewById(R.id.name_edit);
        birth_edit=(EditText)findViewById(R.id.birth_edit);
        gift_edit=(EditText)findViewById(R.id.gift_edit);
        add_button=(Button) findViewById(R.id.add_button);
        db=new myDB(add_layout.this);
    }

    private void setAdd_button()
    {
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_temp=name_edit.getText().toString();
                if(name_temp==null||name_temp.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"名字不能为空",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SQLiteDatabase sqLiteDatabase=db.getWritableDatabase();
                    Cursor cursor=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" where name =? ",new String[]{name_temp});
                    if(cursor.getCount()!=0)//表中存在
                    {
                        Toast.makeText(getApplicationContext(),"表中已经存在该姓名的联系人",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        String birth_temp=birth_edit.getText().toString();
                        String gift_temp=gift_edit.getText().toString();
                        db.insert(name_temp,birth_temp,gift_temp);
                        Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        setResult(RESULT_CODE,intent);
                        finish();
                    }
                }


            }
        });
    }
}
