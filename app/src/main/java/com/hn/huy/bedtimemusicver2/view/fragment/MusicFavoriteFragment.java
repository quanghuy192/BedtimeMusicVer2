package com.hn.huy.bedtimemusicver2.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hn.huy.bedtimemusicver2.R;
import com.hn.huy.bedtimemusicver2.model.business.MusicRealmAccess;
import com.hn.huy.bedtimemusicver2.model.entity.Music;
import com.hn.huy.bedtimemusicver2.service.PlayerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hn.huy.bedtimemusicver2.R.layout.music_item;

/**
 * Created by Huy on 12/13/2016.
 */

public class MusicFavoriteFragment extends BaseFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView favoriteList;
    private ArrayAdapter<Music> adapter;
    private List<Map<String, String>> listSong;
    public static Intent playerService;
    private ViewHolder viewHolder;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public BaseFragment newInstance(int sectionNumber) {
        MusicFavoriteFragment fragment = new MusicFavoriteFragment();
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
        titleOfTab.setTextColor(getResources().getColor(R.color.pink));
        titleOfTab.setText("Favorite Music");

        /*MusicAccessDatabase readDatabase = new MusicAccessDatabase(
                getActivity());
        readDatabase.openToRead();
        try {
            listSong = readDatabase.getList();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        MusicRealmAccess access = new MusicRealmAccess();
        try {
            listSong = access.getList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        favoriteList = (ListView) rootView.findViewById(R.id.music_list_base);
        adapter = new MusicListAdapter(getActivity(), R.layout.music_item,
                getItems());

        favoriteList.setAdapter(adapter);
        favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                playerService = new Intent(getActivity(), PlayerService.class);
                playerService.putExtra("songIndex", position);
                playerService.putExtra("Key", 0);
                if (PlayerService.mediaPlayer == null) {
                    getActivity().startService(playerService);
                } else {
                    getActivity().stopService(playerService);
                    getActivity().startService(playerService);
                }
                getActivity().finish();
            }
        });
        // readDatabase.close();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    private class MusicListAdapter extends ArrayAdapter<Music> {

        List<Music> arrayList;

        public MusicListAdapter(Context context, int resource, List<Music> objects) {
            super(context, resource, objects);
            arrayList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            if (view == null) {
                LayoutInflater layoutinflate = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = layoutinflate.inflate(music_item, parent, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            Music music = arrayList.get(position);
            viewHolder.songTitle.setText(music.getSongTitle());
            viewHolder.songArtist.setText(music.getSongArtist());

            return view;
        }

    }

    private List<Music> getItems() {
        List<Music> musicList = new ArrayList<>();
        for (int i = 0; i < listSong.size(); i++) {
            Music music = new Music(
                    listSong.get(i).get("songTitle"),
                    listSong.get(i).get("songArtist"),
                    listSong.get(i).get("songPath"));
            // music.setId(Long.valueOf(listSong.get(i).get("songId")));
            musicList.add(music);
        }
        return musicList;
    }

    class ViewHolder {
        TextView songTitle, songArtist;

        ViewHolder(View base) {
            this.songTitle = (TextView) base.findViewById(R.id.songTitle);
            this.songArtist = (TextView) base.findViewById(R.id.songArtist);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflate = getActivity().getMenuInflater();
        menuInflate.inflate(R.menu.favorite_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        @SuppressWarnings({})
        ArrayAdapter<Music> adapter = (ArrayAdapter<Music>) favoriteList
                .getAdapter();

        // MusicAccessDatabase writeDatabase = new MusicAccessDatabase(
        // getActivity());
        // writeDatabase.openToWrite();
        switch (item.getItemId()) {
            case R.id.remove: {
                Music removeMusic = adapter.getItem(menuInfo.position);
                // writeDatabase.remove(removeMusic);
                adapter.remove(removeMusic);

                if (PlayerService.mediaPlayer != null
                        && PlayerService.mediaPlayer.isPlaying()) ;
                PlayerService.mediaPlayer.pause();
                Toast.makeText(getActivity(),
                        removeMusic.getSongTitle() + " is removed ",
                        Toast.LENGTH_SHORT).show();
            }
            break;
            default:
                break;
        }

        adapter.notifyDataSetChanged();
        // writeDatabase.close();
        return super.onContextItemSelected(item);
    }
}
