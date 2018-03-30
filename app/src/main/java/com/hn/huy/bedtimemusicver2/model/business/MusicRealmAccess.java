package com.hn.huy.bedtimemusicver2.model.business;

import android.content.res.Resources;

import com.hn.huy.bedtimemusicver2.model.entity.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by huy on 7/1/17.
 */

public class MusicRealmAccess extends BaseAccessDatabase {

    @Override
    public Music find(Music m) {
        RealmResults<Music> mList = realm()
                .where(Music.class)
                .equalTo("songTitle", m.getSongTitle())
                .findAll();

        if (mList.isEmpty()) {
            throw new Resources.NotFoundException();
        }

        return mList.get(0);
    }

    @Override
    public boolean insert(Music m) {
        realm().beginTransaction();
        List<Music> allSongWithTitle = findAll(m.getSongTitle().trim());
        if (allSongWithTitle.size() > 0) {
            return false;
        } else {
            Music newSong = copy(m);
            realm().insert(newSong);
            realm().commitTransaction();
            return true;
        }
    }

    @Override
    public boolean delete(Music m) {
        realm().beginTransaction();
        RealmResults<Music> mList = realm().where(Music.class).equalTo("songTitle", m.getSongTitle()).findAll();
        mList.deleteAllFromRealm();
        realm().commitTransaction();
        return false;
    }

    @Override
    public boolean update(Music m) {
        return false;
    }

    @Override
    public List<Music> findAll() {
        RealmResults<Music> mList = realm().where(Music.class).findAll();
        return mList.subList(0, mList.size());
    }

    @Override
    public List<Music> findAll(String songTitle) {
        RealmResults<Music> mList = realm().where(Music.class).equalTo("songTitle", songTitle).findAll();
        return mList.subList(0, mList.size());
    }

    @Override
    public boolean deleteAll() {
        realm().deleteAll();
        return false;
    }

    public List<Map<String, String>> getList() {
        List<Map<String, String>> songListDB = new ArrayList<>();
        Iterator<Music> results = findAll().iterator();
        Music m;

        while (results.hasNext()) {
            Map<String, String> list = new HashMap<>();
            m = results.next();
            // list.put("songId", String.valueOf(results.next()..getLong(0)));
            list.put("songTitle", m.getSongTitle());
            list.put("songPath", m.getSongPath());
            list.put("songArtist", m.getSongArtist());
            songListDB.add(list);
        }
        return songListDB;
    }
}
