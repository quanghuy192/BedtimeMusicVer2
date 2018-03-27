package com.hn.huy.bedtimemusicver2.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hn.huy.bedtimemusicver2.R;
import com.hn.huy.bedtimemusicver2.application.MusicApplication;
import com.hn.huy.bedtimemusicver2.model.business.LoadDataFromSdCard;
import com.hn.huy.bedtimemusicver2.model.entity.TypeFragment;
import com.hn.huy.bedtimemusicver2.view.adapter.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<HashMap<String, String>> listSong;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                listSong = new ArrayList<>();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                // Get All song list from storage
                LoadDataFromSdCard loadDataFromSdCard = new LoadDataFromSdCard();
                listSong = loadDataFromSdCard.getList(this);
            }
        } else {
            LoadDataFromSdCard loadDataFromSdCard = new LoadDataFromSdCard();
            listSong = loadDataFromSdCard.getList(this);
        }


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void moveToTab(TypeFragment type) {

        // move to other tab
        mViewPager.setCurrentItem(type.ordinal());
    }

    public List<HashMap<String, String>> getListSong() {

        if (!com.hn.huy.bedtimemusicver2.utilities.ValidatorUtilities.isEmptyForList(listSong)) {
            return new ArrayList<>();
        }
        return listSong;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(MusicApplication.SingletonApplication.INSTANCE.getInstance().getPlayerService());
    }
}
