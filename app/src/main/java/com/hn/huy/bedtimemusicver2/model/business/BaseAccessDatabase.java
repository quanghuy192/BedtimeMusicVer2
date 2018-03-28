package com.hn.huy.bedtimemusicver2.model.business;

import com.hn.huy.bedtimemusicver2.application.MusicApplication;
import com.hn.huy.bedtimemusicver2.model.entity.Music;

import io.realm.Realm;

/**
 * Created by huy_dq on 3/28/18.
 */

public abstract class BaseAccessDatabase implements Connecting<Music> {

    protected Realm realm() {
        return MusicApplication.getMyRealm();
    }

    protected Music music() {
        return realm().createObject(Music.class);
    }

    protected Music copy(Music m) {
        return realm().copyToRealm(m);
    }
}
