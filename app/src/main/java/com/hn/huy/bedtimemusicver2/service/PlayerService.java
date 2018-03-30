package com.hn.huy.bedtimemusicver2.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.hn.huy.bedtimemusicver2.R;
import com.hn.huy.bedtimemusicver2.application.MusicApplication;
import com.hn.huy.bedtimemusicver2.utilities.Utilities;
import com.hn.huy.bedtimemusicver2.view.activity.MainActivity;
import com.hn.huy.bedtimemusicver2.view.fragment.MusicListFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.hn.huy.bedtimemusicver2.utilities.Utilities.milliSecondsToTimer;

public class PlayerService extends Service implements
        OnCompletionListener {

    private List<HashMap<String, String>> songsListingSD = new ArrayList<HashMap<String, String>>();
    public static int currentSongIndex = -1;
    public static List<HashMap<String, String>> songsListFavorite = new ArrayList<HashMap<String, String>>();
    public static MediaPlayer mediaPlayer;
    public static TextView songCurrentDurationLabel;
    public static TextView songTotalDurationLabel;
    public static int key = 1;
    public static PlayerService mPlayerService;
    public static Handler progressBarHandler = new Handler();
    private static final String CURRENT_SONG_MESG = "com.vn.hn.quanghuy.currentsong.msg";

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayerService = this;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initUI();
        int songIndex;
        if (intent == null) {
            songIndex = 0;
        } else {
            songIndex = intent.getIntExtra("songIndex", 0);
        }

        key = intent.getIntExtra("Key", 1);
        setCurrentListSong(key);

        if (songIndex != currentSongIndex) {
            currentSongIndex = songIndex;
            playSong(currentSongIndex);

        } else if (currentSongIndex != -1) {
            /*
            textSong.setText(songsListingSD.get(songIndex).get("songTitle"));
            playSong(currentSongIndex);
            if (mediaPlayer.isPlaying()) {
                play.setImageResource(R.mipmap.outline_pause_64);
            } else {
                play.setImageResource(R.mipmap.outline_play_64);
            }
            */
            playSong(currentSongIndex);
        }
        initNotification(songIndex);

        return START_NOT_STICKY;
    }

    public int getSizeOfMusicList() {
        return songsListingSD.size();
    }

    private void setCurrentListSong(int keyId) {
        if (keyId == 1) {
            songsListingSD = MusicListFragment.listSong;
        } else {
            songsListingSD = songsListFavorite;
        }
    }

    public void playSong(int songIndex) {
        try {

            mediaPlayer.reset();
            mediaPlayer.setDataSource(songsListingSD.get(songIndex).get(
                    "songPath"));
            mediaPlayer.prepare();
            mediaPlayer.start();
/*            play.setImageResource(R.mipmap.outline_pause_64
            );
            textSong.setText(songsListingSD.get(songIndex).get("songTitle"));*/
            updateProgressBar();
            sendStatusCurrentSong(songIndex);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendStatusCurrentSong(int songIndex) {
        String songTitle = songsListingSD.get(songIndex).get("songTitle");
        long totalDuration = 0;
        try {
            totalDuration = mediaPlayer.getDuration();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        long currentDuration = 0;
        try {
            currentDuration = mediaPlayer.getCurrentPosition();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // Displaying Total Duration time
        String songTotalDuration =
                milliSecondsToTimer(totalDuration);
        // Displaying time completed playing
        String songCurrentDuration = Utilities.milliSecondsToTimer(currentDuration);
        // Updating progress bar
        int mProgress = Utilities.getProgressPercentage(
                currentDuration, totalDuration);

        Intent intent = new Intent(CURRENT_SONG_MESG);
        intent.putExtra("songTitle", songTitle);
        intent.putExtra("songTotalDuration", songTotalDuration);
        intent.putExtra("songCurrentDuration", songCurrentDuration);
        intent.putExtra("songProgress", mProgress);
        intent.putExtra("changeIcon", 1);

        sendBroadcast(intent);
    }

    private void sendStatusCurrentSong(long currentDuration, long totalDuration) {
        String songTitle = songsListingSD.get(currentSongIndex).get("songTitle");

        // Updating progress bar
        int mProgress = Utilities.getProgressPercentage(
                currentDuration, totalDuration);

        Intent intent = new Intent(CURRENT_SONG_MESG);
        intent.putExtra("songTitle", songTitle);
        intent.putExtra("songTotalDuration", milliSecondsToTimer(totalDuration));
        intent.putExtra("songCurrentDuration", milliSecondsToTimer(currentDuration));
        intent.putExtra("songProgress", mProgress);
        intent.putExtra("changeIcon", 1);

        sendBroadcast(intent);
    }

    private void initUI() {

        try {
            /*MusicAccessDatabase readList = new MusicAccessDatabase(this);
            readList.openToRead();

            songsListFavorite = readList.getList();
            readList.close();*/

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", e.toString());
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        boolean mRepeat = MusicApplication.SingletonApplication.INSTANCE.getInstance().ismRepeat();
        boolean mShuffle = MusicApplication.SingletonApplication.INSTANCE.getInstance().ismShuffle();

        if (mRepeat) {
            playSong(currentSongIndex);
        } else if (mShuffle) {
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songsListingSD.size() - 1) + 1);
        } else if (currentSongIndex == songsListingSD.size() - 1)
            currentSongIndex = 0;
        else
            currentSongIndex++;
        playSong(currentSongIndex);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void updateProgressBar() {
        progressBarHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = 0;
            try {
                totalDuration = mediaPlayer.getDuration();

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            long currentDuration = 0;
            try {
                currentDuration = mediaPlayer.getCurrentPosition();

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

            // Displaying Total Duration time
            // songTotalDurationLabel.setText(""
            //        + milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            // songCurrentDurationLabel.setText(""
            //        + milliSecondsToTimer(currentDuration));
            // Updating progress bar
            int mProgress = (int) (Utilities.getProgressPercentage(
                    currentDuration, totalDuration));

            // progress.setProgress(mProgress);
            sendStatusCurrentSong(currentDuration, totalDuration);
            // Running this thread after 100 milliseconds
            progressBarHandler.postDelayed(this, 100);

        }
    };

    public Runnable getmUpdateTimeTask() {
        return mUpdateTimeTask;
    }

    public void onDestroy() {
        super.onDestroy();
        cancelService();
    }

    public static void cancelService() {
        currentSongIndex = -1;

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // --------------------Push Notification
    // Set up the notification ID
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    // Create Notification
    private void initNotification(int songIndex) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.mipmap.outline_play_64;
        CharSequence tickerText = "Music";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        Context context = getApplicationContext();
        CharSequence songName = songsListingSD.get(songIndex).get("songTitle");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        /*
         * PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
		 * notificationIntent, 0);
		 */
        // notification.setLatestEventInfo(context, songName, null, null);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

}
