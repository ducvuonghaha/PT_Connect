package com.tanxe.supple_online.model;

public class Lesson {
    String IDVideo;
    String title;
    int thumbnail;

    public Lesson(String IDVideo, String title, int thumbnail) {
        this.IDVideo = IDVideo;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getIDVideo() {
        return IDVideo;
    }

    public void setIDVideo(String IDVideo) {
        this.IDVideo = IDVideo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
