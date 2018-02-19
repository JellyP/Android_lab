package com.example.administrator.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileEdit extends AppCompatActivity {
    EditText file_name;
    EditText file_content;
    Button save_button;
    Button load_button;
    Button clear_button;
    Button delete_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
        init();
        setSave();
        setLoad();
        setClear();
        setDelete();
    }

    private void init()
    {
        file_name=(EditText)findViewById(R.id.file_name);
        file_content=(EditText)findViewById(R.id.file_content);
        save_button=(Button)findViewById(R.id.save_button);
        load_button=(Button)findViewById(R.id.load_button);
        clear_button=(Button)findViewById(R.id.clear_button1);
        delete_button=(Button)findViewById(R.id.delete_button);

    }

    private void setSave()
    {
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename=file_name.getText().toString();
                String filecontent=file_content.getText().toString();
                if(filename.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"文件名不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(filename.indexOf('/')!=-1)
                {
                    Toast.makeText(getApplicationContext(),"文件名不能含有 / ",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    try
                    {
                        FileOutputStream fileOutputStream=getApplicationContext().openFileOutput(filename,MODE_PRIVATE);
                        fileOutputStream.write(filecontent.getBytes());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        Toast.makeText(getApplicationContext(),"存储成功",Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private void setLoad()
    {
        load_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename=file_name.getText().toString();
                String filecontent;
                try
                {
                    FileInputStream fileInputStream=openFileInput(filename);
                    //打开filename
                    byte[] readBytes=new byte[fileInputStream.available()];
                    fileInputStream.read(readBytes);
                    //读取
                    filecontent=new String (readBytes);
                    //设置内容
                    file_content.setText(filecontent);
                    Toast.makeText(getApplicationContext(),"加载成功",Toast.LENGTH_SHORT).show();

                }catch (FileNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setClear()
    {
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_content.setText("");
            }
        });
    }

    private void setDelete()
    {
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename=file_name.getText().toString();
                if(deleteFile(filename))
                {
                    Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"未找到该文件",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


        //跳转到主界面
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            finish();
        }
        onDestroy();
        return super.onKeyDown(keyCode, event);
    }
}
