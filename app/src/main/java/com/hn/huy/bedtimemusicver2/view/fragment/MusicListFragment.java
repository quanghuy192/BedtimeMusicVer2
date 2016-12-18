package com.hn.huy.bedtimemusicver2.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hn.huy.bedtimemusicver2.R;

/**
 * Created by Huy on 12/13/2016.
 */

public class MusicListFragment extends BaseFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public MusicListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public BaseFragment newInstance(int sectionNumber) {
        MusicListFragment fragment = new MusicListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_list_fragment, container, false);
        return rootView;
    }
}
