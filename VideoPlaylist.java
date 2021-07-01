package com.google;

import java.util.HashMap;
import java.util.Map;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String name;
    private Map<String, Video> videos;

    public VideoPlaylist(String name, Map<String, Video> videos){
        this.name = name;
        videos = this.videos;
    }

    public String getName(){
        return this.name;
    }

    public Map<String, Video> getVideos(){
        return this.videos;
    }
}
