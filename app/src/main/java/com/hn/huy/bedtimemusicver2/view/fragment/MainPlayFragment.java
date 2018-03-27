package com.hn.huy.bedtimemusicver2.view.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hn.huy.bedtimemusicver2.R;
import com.hn.huy.bedtimemusicver2.application.MusicApplication;
import com.hn.huy.bedtimemusicver2.model.entity.TypeFragment;
import com.hn.huy.bedtimemusicver2.service.PlayerService;
import com.hn.huy.bedtimemusicver2.utilities.Utilities;
import com.hn.huy.bedtimemusicver2.utilities.ValidatorUtilities;
import com.hn.huy.bedtimemusicver2.view.activity.MainActivity;

import java.util.Random;

import static com.hn.huy.bedtimemusicver2.service.PlayerService.currentSongIndex;
import static com.hn.huy.bedtimemusicver2.service.PlayerService.mediaPlayer;
import static com.hn.huy.bedtimemusicver2.service.PlayerService.progressBarHandler;


/**
 * Created by Huy on 12/13/2016.
 */

public class MainPlayFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public TextView songTitle;
    public static TextView songCurrentDurationLabel;
    public static TextView songTotalDurationLabel;
    public static SeekBar progress;
    public ImageView play, next, previous, reload, shuffle, btnForward,
            btnBackward, btnList;

    private boolean mRepeat = false;
    private boolean mShuffle = false;
    private int seekForwardTime = 5000;
    private int seekBackwardTime = 5000;


    private static final String CURRENT_SONG_MESG = "com.vn.hn.quanghuy.currentsong.msg";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equalsIgnoreCase(CURRENT_SONG_MESG)) {
                String title = intent.getStringExtra("songTitle");
                String totalDuration = intent.getStringExtra("songTotalDuration");
                String currentDuration = intent.getStringExtra("songCurrentDuration");
                int mProgress = intent.getIntExtra("songProgress", 0);
                int isChangeIcon = intent.getIntExtra("songProgress", 0);

                if (!ValidatorUtilities.isNull(songTitle)) {
                    songTitle.setText(title);
                }
                if (!ValidatorUtilities.isNull(songTotalDurationLabel)) {
                    songTotalDurationLabel.setText(totalDuration);
                }
                if (!ValidatorUtilities.isNull(songCurrentDurationLabel)) {
                    songCurrentDurationLabel.setText(currentDuration);
                }
                if (!ValidatorUtilities.isNull(progress)) {
                    progress.setProgress(mProgress);
                }
                if (!ValidatorUtilities.isNull(play) && isChangeIcon == 1) {
                    play.setImageResource(R.mipmap.outline_pause_64);
                }
            }
        }
    };

    public MainPlayFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public BaseFragment newInstance(int sectionNumber) {
        MainPlayFragment fragment = new MainPlayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_play_fragment, container, false);
        btnList = (ImageView) rootView.findViewById(R.id.btn_list);
        songTitle = (TextView) rootView.findViewById(R.id.textSong);
        songCurrentDurationLabel = (TextView) rootView.findViewById(R.id.current_time_txt);
        songTotalDurationLabel = (TextView) rootView.findViewById(R.id.total_time_txt);
        progress = (SeekBar) rootView.findViewById(R.id.progress);
        play = (ImageView) rootView.findViewById(R.id.btn_play_imageview);
        next = (ImageView) rootView.findViewById(R.id.btn_next_imageview);
        previous = (ImageView) rootView.findViewById(R.id.btn_previous_imageview);
        reload = (ImageView) rootView.findViewById(R.id.btnRepeat);
        shuffle = (ImageView) rootView.findViewById(R.id.btnShuffle);
        btnForward = (ImageView) rootView.findViewById(R.id.btn_forward_imageview);
        btnBackward = (ImageView) rootView.findViewById(R.id.btn_backward_imageview);

        play.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        reload.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnList.setOnClickListener(this);
        progress.setOnSeekBarChangeListener(this);

        progress.setProgress(0);
        progress.setMax(100);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CURRENT_SONG_MESG);
        getActivity().registerReceiver(receiver, intentFilter);

        return rootView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        progressBarHandler.removeCallbacks(PlayerService.mPlayerService.getmUpdateTimeTask());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progressBarHandler.removeCallbacks(PlayerService.mPlayerService.getmUpdateTimeTask());
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = Utilities.progressToTimer(seekBar.getProgress(),
                totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);
        // update timer progress again
        PlayerService.mPlayerService.updateProgressBar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_list:
                MainActivity parentActivity = (MainActivity) getActivity();
                parentActivity.moveToTab(TypeFragment.ALL_LIST);
                break;

            case R.id.btnRepeat: {
                if (!mRepeat) {
                    Toast.makeText(getActivity(), " Repeat on ",
                            Toast.LENGTH_SHORT).show();
                    mRepeat = true;
                    mShuffle = false;
                    reload.setImageResource(R.drawable.btn_repeat_focused);
                    shuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    mRepeat = false;
                    // mShuffle = true;
                    reload.setImageResource(R.drawable.btn_repeat);
                }

                MusicApplication.SingletonApplication.INSTANCE.getInstance().setmRepeat(mRepeat);
                MusicApplication.SingletonApplication.INSTANCE.getInstance().setmShuffle(mShuffle);

            }
            break;

            case R.id.btnShuffle: {
                if (!mShuffle) {
                    Toast.makeText(getActivity(), " Shuffle on ",
                            Toast.LENGTH_SHORT).show();
                    mShuffle = true;
                    mRepeat = false;
                    shuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    reload.setImageResource(R.drawable.btn_repeat);
                } else {
                    mShuffle = false;
                    // mRepeat = true;
                    shuffle.setImageResource(R.drawable.btn_shuffle);
                }

            }
            break;

            case R.id.btn_play_imageview: {
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        play.setImageResource(R.mipmap.outline_play_64);
                    }
                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        play.setImageResource(R.mipmap.outline_pause_64);
                    }
                }

            }
            break;

            case R.id.btn_next_imageview: {
                PlayerService.mPlayerService.onCompletion(mediaPlayer);
            }
            break;

            case R.id.btn_previous_imageview: {
                if (mRepeat) {
                    PlayerService.mPlayerService.playSong(currentSongIndex);
                    PlayerService.mPlayerService.updateProgressBar();
                } else if (mShuffle) {
                    Random rand = new Random();
                    currentSongIndex = rand
                            .nextInt((PlayerService.mPlayerService.getSizeOfMusicList() - 1) + 1);
                } else if (currentSongIndex == PlayerService.mPlayerService.getSizeOfMusicList() - 1)
                    currentSongIndex = 0;
                else
                    currentSongIndex--;
                PlayerService.mPlayerService.playSong(currentSongIndex);
                PlayerService.mPlayerService.updateProgressBar();

            }
            break;

            case R.id.btn_forward_imageview: {

                int currentPosition = mediaPlayer.getCurrentPosition();

                if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                } else {
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }
            }

            break;

            case R.id.btn_backward_imageview: {

                int currentPosition2 = mediaPlayer.getCurrentPosition();

                if (currentPosition2 - seekBackwardTime >= 0) {
                    mediaPlayer.seekTo(currentPosition2 - seekBackwardTime);
                } else {
                    mediaPlayer.seekTo(0);
                }
            }

            break;

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove progress bar update Hanlder callBacks
        progressBarHandler.removeCallbacks(PlayerService.mPlayerService.getmUpdateTimeTask());
    }
}
