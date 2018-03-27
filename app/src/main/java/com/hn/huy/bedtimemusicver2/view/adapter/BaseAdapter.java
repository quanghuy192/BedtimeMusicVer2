package com.hn.huy.bedtimemusicver2.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hn.huy.bedtimemusicver2.view.fragment.MainPlayFragment;
import com.hn.huy.bedtimemusicver2.view.fragment.MusicFavoriteFragment;
import com.hn.huy.bedtimemusicver2.view.fragment.MusicListFragment;
import com.hn.huy.bedtimemusicver2.view.fragment.SettingTimeFragment;

/**
 * Created by Huy on 12/13/2016.
 */

public class BaseAdapter extends FragmentPagerAdapter {

    public BaseAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return new MainPlayFragment().newInstance(position + 1);
            case 1:
                return new MusicListFragment().newInstance(position + 1);
            case 2:
                return new MusicFavoriteFragment().newInstance(position + 1);
            case 3:
                return new SettingTimeFragment().newInstance(position + 1);
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
            case 3:
                return "SECTION 4";
        }
        return null;
    }
}
