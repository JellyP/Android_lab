package com.example.administrator.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.IOException;

public class MusicService extends Service {


    private final static int PLAY_OR_PAUSE_STATE=101;
    private final static int STOP_STATE=102;
    private final static int GET_LENGTH_STATE=103;
    private final static int REFLASH_STATE=104;
    private final static int DRAG_STATE=105;

    private final static int PLAYING_STATE=106;
    private final static int PAUSE_STATE=107;
    private final static int EXIT=-1;

    private static MediaPlayer mp=new MediaPlayer();
    private IBinder iBinder;
    public MusicService() {
        iBinder=new MyBinder();
        try
        {
            mp.reset();
            mp.setDataSource(Environment.getExternalStorageDirectory()+"/melt.mp3");
            mp.prepare();
            mp.setLooping(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public class MyBinder extends Binder
    {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException
        {
            switch (code)
            {
                //播放或者暂停被点击
                case PLAY_OR_PAUSE_STATE:
                    if(mp.isPlaying())
                    {
                        mp.pause();
                        reply.writeInt(PAUSE_STATE);
                    }
                    else
                    {
                        mp.start();
                        reply.writeInt(PLAYING_STATE);
                    }
                    break;
                //停止被点击
                case STOP_STATE:
                    try
                    {
                        mp.stop();
                        mp.prepare();
                        mp.seekTo(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    reply.writeInt(STOP_STATE);
                    break;
                //更新状态
                case REFLASH_STATE:
                    reply.writeInt(mp.getCurrentPosition());
                    break;
                //拖动状态
                case DRAG_STATE:
                    mp.seekTo(data.readInt());
                    break;
                //获取音乐长度
                case GET_LENGTH_STATE:
                    reply.writeInt(mp.getDuration());
                    break;
                //退出被按下
                case EXIT:
                    onDestroy();
                    break;
            }

            return super.onTransact(code,data,reply,flags);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return iBinder;
    }


    @Override
    public void onDestroy()
    {
        if(mp!=null)
        {
            mp.stop();
            mp.release();
            mp=null;
        }
        super.onDestroy();
    }
}
