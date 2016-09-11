package com.xks.musicplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.xks.musicplayer.adapter.MyMusicAdapter;
import com.xks.musicplayer.bean.MusicBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";

    private Button playButton;
    private Button pauseButton;

    private ListView mListView;
    private MyMusicAdapter myMusicAdapter;

    private MusicService.MyBinder myBinder;//Binder对象
    private MyServiceConnected myServiceConnected;
    private int currentPlaying = 0;//正在播放的是第几首歌
    private Button mRefreshPlayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = (Button) findViewById(R.id.btn_play);
        pauseButton = (Button) findViewById(R.id.btn_pause);
        mRefreshPlayList = (Button) findViewById(R.id.btn_flush_playlist);
        mListView = (ListView) findViewById(R.id.lv_listview);

        //通过绑定的方式开启服务,该方式可以使activity与服务进行通信
        Intent intent = new Intent(this,MusicService.class);
        myServiceConnected = new MyServiceConnected();
        bindService(intent, myServiceConnected,BIND_AUTO_CREATE);
        startService(intent);

        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        mRefreshPlayList.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    /**
     * 退出activity时要解除与服务的绑定
     */
    @Override
    protected void onDestroy() {
        unbindService(myServiceConnected);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play://播放音乐
                Log.d(TAG, "myBinder ==null? " + (myBinder == null));
                myBinder.playMusic(MyApp.musicBeanList.get(currentPlaying));
                break;
            case R.id.btn_pause://暂停音乐
                myBinder.pauseMusic();
                break;
            case R.id.btn_flush_playlist:
                myBinder.createPlayList();
                myMusicAdapter.notifyDataSetChanged();

                break;
        }
    }

    private class MyServiceConnected implements ServiceConnection{
        /**
         * 绑定服务成功回调
         * @param componentName
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.e(TAG, "类名>>MainActivity>>方法名>>onServiceConnected()");
            myBinder = (MusicService.MyBinder) service;
            myMusicAdapter = new MyMusicAdapter();
            mListView.setAdapter(myMusicAdapter);

        }
        /**
         * 服务意外挂掉回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MusicBean musicBean = myMusicAdapter.getItem(i);
        currentPlaying = (int) myMusicAdapter.getItemId(i);
        Log.e(TAG, "类名>>MainActivity>>方法名>>onItemClick()"+musicBean.getMusicName());
        myBinder.playMusic(musicBean);
    }
}
