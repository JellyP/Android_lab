package com.example.administrator.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static Boolean flag;//判断是否存在密码
    static String curpassword;//判断当前密码是什么
    EditText new_password;
    EditText confirm_password;
    EditText password;
    Button ok_button;
    Button clear_button;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    final static String PREFERENCE_NAME="SAVE_PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        if(flag==false)
        {
            password.setVisibility(EditText.INVISIBLE);
            new_password.setVisibility(EditText.VISIBLE);
            confirm_password.setVisibility(EditText.VISIBLE);
        }
        else
        {
            password.setVisibility(EditText.VISIBLE);
            new_password.setVisibility(EditText.INVISIBLE);
            confirm_password.setVisibility(EditText.INVISIBLE);
        }
        pressEvent();
    }

    private void init()
    {

        new_password=(EditText)findViewById(R.id.new_password);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        password=(EditText)findViewById(R.id.password);
        ok_button=(Button)findViewById(R.id.ok_button);
        clear_button=(Button)findViewById(R.id.clear_button);
        sharedPref=getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);
        editor=sharedPref.edit();
        flag=sharedPref.getBoolean("flag",false);//每次读取flag
        curpassword=sharedPref.getString("current_password",null);//每次读取密码
    }

    private void pressEvent()
    {
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==false)//密码不存在
                {
                    String np=new_password.getText().toString();
                    String cp=confirm_password.getText().toString();
                    if(np.equals(cp)&&np.length()!=0)
                    {
                        editor.putBoolean("flag",true);//存储密码标志
                        editor.putString("current_password",cp);//存储密码
                        editor.commit();

                        Toast.makeText(getApplicationContext(), "密码正确", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(MainActivity.this,FileEdit.class);
                        startActivity(intent);
                    }
                    else if(np.length()==0||cp.length()==0)
                    {
                        Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                    }
                }
                else//密码存在
                {
                    String pass=password.getText().toString();
                    if(pass.equals(curpassword))
                    {
                        Toast.makeText(getApplicationContext(), "密码正确", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(MainActivity.this,FileEdit.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new_password.setText("");
                    confirm_password.setText("");
                    password.setText("");
            }
        });
    }





}
