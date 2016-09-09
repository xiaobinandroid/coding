package com.xks.musicplayer;

import android.app.Application;

import com.xks.musicplayer.bean.MusicBean;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends Application {
    //播放列表音乐集合,MusicBean实体类中存放歌曲名称和歌曲所在目录
    public static List<MusicBean> musicBeanList;

    @Override
    public void onCreate() {
        super.onCreate();
        musicBeanList = new ArrayList<MusicBean>();
    }
}
