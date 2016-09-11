package com.xks.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.xks.musicplayer.bean.MusicBean;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/6 0006  上午 8:57.
 */
public class MusicService extends Service {
    MediaPlayer mediaPlayer;//播放歌曲的类
    private int currentPlaying = 0;//正在播放的歌曲
    private static final String TAG = "MusicService";
    public static boolean isPlaying = false;//是否处于播放状态
    public static boolean isPause = false;//是否处于暂停状态

    /**
     * 服务Create,此时应当生成播放列表并初始化MediaPlay类
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "方法名>>onBind()");
        final MyBinder myBinder = new MyBinder();
        if (MyApp.musicBeanList.size() == 0) {
            myBinder.createPlayList();
            Log.e(TAG, "方法名>>onBind()" + MyApp.musicBeanList.toString());
        }

        return myBinder;
    }

    /**
     * 服务与activity进行交互的方法
     */
    public class MyBinder extends Binder {

        /**
         * 播放指定位置的歌曲
         *
         * @param music
         */
        public void playMusic(MusicBean music, int position) {

            try {
                /*
                 * Resets the MediaPlayer to its uninitialized state. After calling this method,
                 * you will have to initialize it again by setting the data source and calling prepare().
                 */
                if (isPlaying&&isPause) {//播放状态但是被暂停了
                    if (currentPlaying != position) {//正在播放的与点击的歌不是同一首
                        setMusic(music);
                    } else if (currentPlaying == position) {//是同一首歌
                        mediaPlayer.start();//=>无需prepare,直接start就行
                        isPause = false;
                    }
                }else{//没有播放
                    setMusic(music);
                }
                currentPlaying = position;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void setMusic(MusicBean music) throws IOException {
            mediaPlayer.reset();//reset后需要重新确定要播放的音乐所在路径
            mediaPlayer.setDataSource(music.getMusicPath());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPause = false;
                    isPlaying = true;
                }
            });
        }

        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mediaPlayer.isPlaying()) {//正在播放时
                mediaPlayer.pause();
                isPlaying = true;
                isPause = true;
            }
        }

        /**
         * 子线程中扫描歌曲>生成播放列表
         * 遍历SD卡下的Music文件夹(mnt>shell>emulated>0>MUSIC),获取以.mp3结尾的文件并装入集合
         */
        public void createPlayList() {
            new Thread() {
                @Override
                public void run() {
                    MyApp.musicBeanList.clear();
                    File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                    File[] files = musicDirectory.listFiles();
                    for (File file : files) {
                        if (file.getName().endsWith(".mp3")) {
                            MusicBean musicBean = new MusicBean();
                            musicBean.setMusicName(file.getName());
                            musicBean.setMusicPath(file.getAbsolutePath());
                            MyApp.musicBeanList.add(musicBean);
                        }
                    }
                }
            }.start();
        }

    }


    @Override
    public void onDestroy() {
        //在服务中的onDestroy方法中对mediaPlayer资源进行释放
        mediaPlayer.release();
        //将mediaPlayer赋空便于垃圾回收器进行回收
        mediaPlayer = null;
        super.onDestroy();
    }
}
