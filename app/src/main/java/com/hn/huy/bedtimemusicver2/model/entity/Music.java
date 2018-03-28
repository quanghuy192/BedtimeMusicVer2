package com.hn.huy.bedtimemusicver2.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Music extends RealmObject {

    @PrimaryKey
    private String songTitle;

    @Required
    private String songArtist;

    @Required
    private String songPath;

    public Music() {
    }

    public Music(String songTitle, String songArtist, String songPath) {
        this.songArtist = songArtist;
        this.songPath = songPath;
        this.songTitle = songTitle;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongPath() {
        return songPath;
    }
}
