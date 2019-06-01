package com.sarahan.popular_movies_stage2;

public class VideoItem {

    private String videoName;
    private String videoLink;

    public VideoItem(String videoName, String videoLink) {
        this.videoName = videoName;
        this.videoLink = videoLink;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
