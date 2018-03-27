package com.hn.huy.bedtimemusicver2.application;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.hn.huy.bedtimemusicver2.service.PlayerService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by huy on 16/02/2017.
 */

public class MusicApplication extends Application {

    public static Intent playerService;
    private boolean mRepeat = false;
    private boolean mShuffle = false;
    private static RealmConfiguration configuration = new RealmConfiguration.Builder()
            .name("music.realm")
            .schemaVersion(1)
            .build();
    private static Realm myRealm  = Realm.getInstance(configuration);

    public enum SingletonApplication {
        INSTANCE;

        private MusicApplication application = new MusicApplication();

        public MusicApplication getInstance() {
            return application;
        }
    }

    public MusicApplication() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        playerService = new Intent(getApplicationContext(), PlayerService.class);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Intent getPlayerService() {
        return playerService;
    }

    public static void setPlayerService(Intent playerService) {
        MusicApplication.playerService = playerService;
    }

    public boolean ismRepeat() {
        return mRepeat;
    }

    public void setmRepeat(boolean mRepeat) {
        this.mRepeat = mRepeat;
    }

    public boolean ismShuffle() {
        return mShuffle;
    }

    public void setmShuffle(boolean mShuffle) {
        this.mShuffle = mShuffle;
    }

    public static Realm getMyRealm() {
        return myRealm;
    }

}
