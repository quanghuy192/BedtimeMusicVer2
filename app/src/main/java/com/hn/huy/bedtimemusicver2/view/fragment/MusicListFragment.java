package com.hn.huy.bedtimemusicver2.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hn.huy.bedtimemusicver2.R;
import com.hn.huy.bedtimemusicver2.application.MusicApplication;
import com.hn.huy.bedtimemusicver2.model.entity.Music;
import com.hn.huy.bedtimemusicver2.model.entity.TypeFragment;
import com.hn.huy.bedtimemusicver2.service.PlayerService;
import com.hn.huy.bedtimemusicver2.view.activity.MainActivity;
import com.hn.huy.bedtimemusicver2.view.adapter.MusicListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Huy on 12/13/2016.
 */

public class MusicListFragment extends BaseFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView listview;
    private ArrayAdapter<Music> adapter;
    public static ArrayList<HashMap<String, String>> listSong;
    public static Intent playerService;

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
        View rootView = inflater.inflate(R.layout.music_fragment, container, false);
        TextView titleOfTab = (TextView) rootView.findViewById(R.id.title_tab);
        titleOfTab.setTextColor(getResources().getColor(R.color.white));
        titleOfTab.setText("Music List");

        try {
            MainActivity parentActivity = (MainActivity) getActivity();
            listSong = new ArrayList<>(parentActivity.getListSong());
        } catch (Exception e) {
            e.printStackTrace();
        }
        listview = (ListView) rootView.findViewById(R.id.music_list_base);
        adapter = new MusicListAdapter(getActivity(), R.layout.music_item, getItems(), MusicListAdapter.Style.DEFAULT);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                playerService = new Intent(getActivity(), PlayerService.class);
                playerService.putExtra("songIndex", position);
                playerService.putExtra("Key", 1);
                MusicApplication.SingletonApplication.INSTANCE.getInstance().setPlayerService(playerService);
                if (PlayerService.mediaPlayer == null) {
                    getActivity().startService(playerService);
                } else {
                    getActivity().stopService(playerService);
                    getActivity().startService(playerService);
                }
                MainActivity parentActivity = (MainActivity) getActivity();
                parentActivity.moveToTab(TypeFragment.MAIN);
            }
        });
        return rootView;
    }

    private ArrayList<Music> getItems() {
        ArrayList<Music> musicList = new ArrayList<Music>();
        for (int i = 0; i < listSong.size(); i++) {
            Music music = new Music();
            music.setSongTitle(listSong.get(i).get("songTitle"));
            music.setSongArtist(listSong.get(i).get("songArtist"));
            music.setSongPath(listSong.get(i).get("songPath"));
            musicList.add(music);
        }
        return musicList;
    }

}
