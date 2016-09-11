package com.xks.musicplayer.bean;

public class MusicBean {
    private String musicName;
    private String musicPath;

    public MusicBean() {
    }

    public MusicBean(String musicName, String musicPath) {
        this.musicName = musicName;
        this.musicPath = musicPath;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "musicName='" + musicName + '\'' +
                ", musicPath='" + musicPath + '\'' +
                '}';
    }
}
