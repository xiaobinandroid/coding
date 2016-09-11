package com.xks.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xks.musicplayer.MyApp;
import com.xks.musicplayer.R;
import com.xks.musicplayer.bean.MusicBean;

/**
 * Created by Administrator on 2016/9/6 0006  下午 7:44.
 */
public class MyMusicAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return MyApp.musicBeanList == null ? 0 : MyApp.musicBeanList.size();
    }

    @Override
    public MusicBean getItem(int position) {
        return MyApp.musicBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        MusicBean musicBean =  MyApp.musicBeanList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_music);
            viewHolder.mTextView.setText(musicBean.getMusicName());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mTextView.setText(musicBean.getMusicName());
        }
        return convertView;
    }

    class ViewHolder {
        private TextView mTextView;
    }
}
