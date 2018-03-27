package com.hn.huy.bedtimemusicver2.model.business;

import com.hn.huy.bedtimemusicver2.application.MusicApplication;
import com.hn.huy.bedtimemusicver2.model.entity.Music;

import io.realm.Realm;

/**
 * Created by huy on 7/1/17.
 */

public class MusicRealmAccess implements Connecting<Music> {


    @Override
    public void find(Music object) {

    }

    @Override
    public void insert(Music m) {
        Music music = music();
        realm().beginTransaction();
        music.setSongArtist(m.getSongArtist());
        music.setSongPath(m.getSongPath());
        music.setSongTitle(m.getSongTitle());
        realm().commitTransaction();
    }

    @Override
    public void delete(Music object) {

    }

    @Override
    public void update(Music objecct) {

    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteAll() {

    }

    private Realm realm() {
        return MusicApplication.getMyRealm();
    }

    private Music music() {
        return realm().createObject(Music.class);
    }

    private Music copy(Music m) {
        return realm().copyToRealm(m);
    }
}
