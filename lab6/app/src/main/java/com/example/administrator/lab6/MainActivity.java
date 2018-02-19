package com.example.administrator.lab6;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.spec.ECField;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE ,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    private static boolean hasPermission=false;
    //控件
    private SeekBar seekBar;
    private ImageView imageView;
    private TextView music_hint;
    private TextView music_begin;
    private TextView music_end;
    private Button play_or_pause;
    private Button stop;
    private Button quit;
    //服务
    private IBinder iBinder;
    private ServiceConnection serviceConnection;
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");

    private ObjectAnimator objectAnimator;
    //
    private int state;//判断状态
    private boolean flag;//判断Seekbar是否要启动
    //发送的状态
    private final static int INITIAL_STATE=100;//初始状态
    private final static int PLAY_OR_PAUSE_STATE=101;//播放或者暂停被按下
    private final static int STOP_STATE=102;//停止被按下
    private final static int GET_LENGTH_STATE=103;//获取音乐的长度
    private final static int REFLASH_STATE=104;//更新音乐当前状态
    private final static int DRAG_STATE=105;//拖动
    //接收到的状态
    private final static int PLAYING_STATE=106;//更新为正在播放状态
    private final static int PAUSE_STATE=107;//更新为暂停状态
    private final static int EXIT=-1;//退出状态
    //发送线程
    private final static int THREAD=108;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermission(this);
        if(hasPermission==true)
        {
            init();//初始化按钮和动画
            pressEvent();//设置点击事件
            bindServer();//绑定服务
            initHandle();//刷新时间轴
        }
    }

    private void init()
    {
        seekBar=(SeekBar)findViewById(R.id.seekbar);
        imageView=(ImageView)findViewById(R.id.image);
        music_hint=(TextView)findViewById(R.id.music_hint);
        music_begin=(TextView)findViewById(R.id.music_begin);
        music_end=(TextView)findViewById(R.id.music_end);
        play_or_pause=(Button)findViewById(R.id.play_or_pause);
        stop=(Button)findViewById(R.id.stop);
        quit=(Button)findViewById(R.id.quit);
        state=INITIAL_STATE;
        flag=false;

        //旋转
        objectAnimator=ObjectAnimator.ofFloat(imageView,"rotation",0,359);
        objectAnimator.setDuration(250000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);//无限
        objectAnimator.end();
    }
    private void pressEvent()
    {
        //播放或者暂停被按下
        play_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Parcel data=Parcel.obtain();
                    Parcel reply=Parcel.obtain();
                    iBinder.transact(PLAY_OR_PAUSE_STATE,data,reply,0);
                    state=reply.readInt();
                    Refresh();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        //停止按钮被按下
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Parcel data=Parcel.obtain();
                    Parcel reply=Parcel.obtain();
                    iBinder.transact(STOP_STATE,data,reply,0);
                    state=reply.readInt();
                    Refresh();
                    //shuaxin
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        //停止按钮被按下
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });
        //拖动
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                music_begin.setText(simpleDateFormat.format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                flag=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try
                {
                    Parcel data=Parcel.obtain();
                    Parcel reply=Parcel.obtain();
                    data.writeInt(seekBar.getProgress());
                    iBinder.transact(DRAG_STATE,data,reply,0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                flag=false;
            }
        });

    }

    private void bindServer()
    {
        serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iBinder=service;
                try
                {
                    Parcel data=Parcel.obtain();
                    Parcel reply=Parcel.obtain();
                    iBinder.transact(GET_LENGTH_STATE,data,reply,0);
                    int length=reply.readInt();
                    seekBar.setMax(length);
                    music_end.setText(simpleDateFormat.format(length));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceConnection=null;
            }
        };
        Intent intent=new Intent(this,MusicService.class);
        startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    private void initHandle()
    {
        final Handler handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case THREAD:
                        if(!flag)
                        {
                            try
                            {
                                Parcel data=Parcel.obtain();
                                Parcel reply=Parcel.obtain();
                                iBinder.transact(REFLASH_STATE,data,reply,0);
                                int current=reply.readInt();
                                seekBar.setProgress(current);

                            }
                            catch (RemoteException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                }

            }
        };
        Thread thread=new Thread()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    try
                    {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(serviceConnection!=null)
                    {
                        handler.obtainMessage(THREAD).sendToTarget();//从整个Messge池中返回一个新的Message实例，通过obtainMessage能避免重复Message创建对象。
                    }
                }

            }

        };
        thread.start();
    }


    private void Refresh() throws RemoteException
    {
        switch (state)
        {
            case PLAYING_STATE://设置为开始
                play_or_pause.setText("PAUSED");
                music_hint.setVisibility(View.VISIBLE);
                music_hint.setText("Playing");
                if(objectAnimator.isStarted())
                {
                    objectAnimator.resume();
                }
                else
                {
                    objectAnimator.start();
                }
                break;
            case PAUSE_STATE://设置为暂停
                play_or_pause.setText("PLAY");

                music_hint.setVisibility(View.VISIBLE);
                music_hint.setText("Paused");
                objectAnimator.pause();
                break;
            case STOP_STATE://设置为停止
                play_or_pause.setText("play");
                music_hint.setVisibility(View.VISIBLE);
                music_hint.setText("Stopped");
                objectAnimator.pause();
                objectAnimator.end();
                break;

        }
    }

    @Override
    public void onDestroy()
    {
        try
        {
            iBinder.transact(EXIT,null,null,0);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(serviceConnection);
        serviceConnection=null;
        super.onDestroy();
        finish();
    }


    public static void verifyStoragePermission(Activity activity)
    {
        try
        {
            int permission= ActivityCompat.checkSelfPermission(activity,"android.permission.READ_EXTERNAL_STORAGE");
            if(permission!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            else
            {
                hasPermission=true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permission[],int[] grantResults)
    {
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this,"已授权",Toast.LENGTH_SHORT).show();
        }
        else
        {
            System.exit(0);
        }
    }
    //返回不停止
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
