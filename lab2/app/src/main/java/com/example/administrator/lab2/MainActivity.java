package com.example.administrator.lab2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    RadioGroup Rg;
    Boolean isStudent;
    Button loginBt;
    Button signinBt;
    TextInputLayout textInputLayout_studentid;
    TextInputLayout textInputLayout_studentpassword;
    EditText studentid_Et;
    EditText studentpassword_Et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=(ImageView) findViewById(R.id.imagOfSysu); /*中山大学图片*/
        Rg=(RadioGroup) findViewById(R.id.studentNature);/*RadioGroup组*/
        isStudent=true;		/*判定Rg选定的是否为学生*/
        loginBt=(Button) findViewById(R.id.loginButton); /*登录按键*/
        signinBt=(Button) findViewById(R.id.signinButton); /*注册按键*/
        textInputLayout_studentid=(TextInputLayout) findViewById(R.id.studentID_EditText_Wrapper); /*TextInputLayout类型的学号框*/
        textInputLayout_studentpassword=(TextInputLayout) findViewById(R.id.studentPassword_EditText_Wrapper);/*TextInputLayout类型的密码框*/
        studentid_Et=(EditText) findViewById(R.id.studentID_EditText); /*学号文本输入框*/
        studentpassword_Et=(EditText) findViewById(R.id.studentPassword_EditText); /*密码文本输入框*/


        final String content[]=new String[]{"拍摄","从相册中选取"};
        img.setOnClickListener(new View.OnClickListener(){/*点击的监听器*/
                                   @Override
                                   public void onClick(View view)
                                   {
                                       AlertDialog.Builder bd=new AlertDialog.Builder(MainActivity.this);/*新建对话框*/
                                       bd.setTitle("上传头像");/*设置标题*/

                /*设置拍摄，从相册中选取以及取消按键还有点击后弹出的Toast指示*/
                                       bd.setItems(content, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface,int choice) {
                                                       Toast.makeText(getApplicationContext(), "您选择了[" + content[choice] + "]", Toast.LENGTH_SHORT)
                                                               .show();
                                                   }
                                               }
                                       ).setNegativeButton("取消",new DialogInterface.OnClickListener()
                                       {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface1,int check1){
                                               Toast.makeText(getApplicationContext(),"您选择了取消",Toast.LENGTH_SHORT)
                                                       .show();
                                           }
                                       });
                                       bd.create().show();/*创建对话框*/
                                   }
                               }
        );



        Rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                          @Override/*检查按钮*/
                                          public void onCheckedChanged(RadioGroup mgroup, @IdRes int checkedId) {
                                              String student_or_teacherMessage;
                                              if(checkedId==R.id.studentButton) /*选择的为学生时*/{student_or_teacherMessage="您选择了学生"; isStudent=true;}
                                              else {student_or_teacherMessage="您选择了教职工"; isStudent=false;}
                                              Snackbar.make(mgroup.getRootView(),student_or_teacherMessage,Snackbar.LENGTH_SHORT)/*弹出snackbar*/
                                                      .setAction("确定", new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              Toast.makeText(getApplicationContext(),"Snackbar 的确定按钮被点击了",Toast.LENGTH_SHORT)
                                                                      .show();/*snackbar的确定被点击时候*/
                                                          }
                                                      })
                                                      .show();
                                          }
                                      }
        );



        loginBt.setOnClickListener(new View.OnClickListener(){/*登录按键的点击监听器*/
                                       @Override
                                       public void onClick(View v)
                                       {
                                           String studentId=studentid_Et.getText().toString(); /*获取学号*/
                                           String studentPassword=studentpassword_Et.getText().toString(); /*获取密码*/
                                           if(studentId.length()==0&&studentPassword.length()!=0) /*学号长度为0即为空*/
                                           {
                                               textInputLayout_studentpassword.setErrorEnabled(false);
                                               textInputLayout_studentid.setError("学号不能为空");
                                               textInputLayout_studentid.setErrorEnabled(true);
                                           }
                                           else if(studentPassword.length()==0&&studentId.length()!=0)/*密码长度为0即为空*/
                                           {
                                               textInputLayout_studentid.setErrorEnabled(false);
                                               textInputLayout_studentpassword.setError("密码不能为空");
                                               textInputLayout_studentpassword.setErrorEnabled(true);
                                           }
                                           else if(studentId.length()==0&&studentPassword.length()==0)
                                           {
                                               textInputLayout_studentid.setError("学号不能为空");
                                               textInputLayout_studentid.setErrorEnabled(true);
                                               textInputLayout_studentpassword.setError("密码不能为空");
                                               textInputLayout_studentpassword.setErrorEnabled(true);
                                           }
                                           else if(studentId.equals("123456")&&studentPassword.equals("666666"))/*输入正确的账号密码*/
                                           {
                                               textInputLayout_studentid.setErrorEnabled(false);
                                               textInputLayout_studentpassword.setErrorEnabled(false);
                                               Snackbar.make(v.getRootView(),"登录成功",Snackbar.LENGTH_SHORT)/*弹出Snackbar*/
                                                       .setAction("确定", new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(getApplicationContext(),"Snackbar 的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                                                           }
                                                       })
                                                       .show();
                                           }
                                           else/*密码错误的情况*/
                                           {
                                               textInputLayout_studentid.setErrorEnabled(false);
                                               textInputLayout_studentpassword.setErrorEnabled(false);
                                               Snackbar.make(v.getRootView(),"学号或密码错误",Snackbar.LENGTH_SHORT)
                                                       .setAction("确定", new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(getApplicationContext(),"Snackbar 的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                                                           }
                                                       })
                                                       .show();
                                           }
                                       }
                                   }

        );

        signinBt.setOnClickListener(new View.OnClickListener() {/*注册按钮*/
            @Override
            public void onClick(View v) {
                if (isStudent == true) {/*如果RadioGroup选中的是学生，那么就弹出学生注册功能未启用的Snackbar*/
                    Snackbar.make(v.getRootView(), "学生注册功能尚未启用", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "Snackbar 的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
                else { /*否则弹出教职工注册功能未启用的Toast*/
                    Toast.makeText(getApplicationContext(), "教职工注册功能尚未启用", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

