package com.hn.huy.bedtimemusicver2.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Huy on 12/13/2016.
 */

public abstract class BaseFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public BaseFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public abstract BaseFragment newInstance(int sectionNumber);

    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState);
}
