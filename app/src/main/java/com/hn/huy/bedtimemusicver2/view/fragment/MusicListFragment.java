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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hn.huy.bedtimemusicver2.R;
import com.hn.huy.bedtimemusicver2.application.MusicApplication;
import com.hn.huy.bedtimemusicver2.model.business.MusicAccessDatabase;
import com.hn.huy.bedtimemusicver2.model.business.MusicRealmAccess;
import com.hn.huy.bedtimemusicver2.model.entity.Music;
import com.hn.huy.bedtimemusicver2.model.entity.TypeFragment;
import com.hn.huy.bedtimemusicver2.service.PlayerService;
import com.hn.huy.bedtimemusicver2.view.activity.MainActivity;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hn.huy.bedtimemusicver2.R.layout.music_item;

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
    public static List<HashMap<String, String>> listSong;
    public static Intent playerService;
    private ViewHolder viewHolder;

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
            listSong = MusicApplication.SingletonApplication.INSTANCE.getInstance().getListSong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listview = (ListView) rootView.findViewById(R.id.music_list_base);
        adapter = new MusicListAdapter(getActivity(), R.layout.music_item, getItems());
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

    private List<Music> getItems() {
        List<Music> musicList = new ArrayList<>();
        for (int i = 0; i < listSong.size(); i++) {
            Music music = new Music(
                    listSong.get(i).get("songTitle"),
                    listSong.get(i).get("songArtist"),
                    listSong.get(i).get("songPath"));
            musicList.add(music);
        }
        return musicList;
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
            viewHolder.imageLike.setTag(String.valueOf(position));

            viewHolder.imageLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayAdapter<Music> adapter = (ArrayAdapter<Music>) listview
                            .getAdapter();
                    MusicRealmAccess access = new MusicRealmAccess();
                    Music currentMusic = adapter.getItem(Integer.parseInt((String) view.getTag()));
                    Toast.makeText(
                            getActivity(),
                            currentMusic.getSongTitle()
                                    + " is add to your favorite list ",
                            Toast.LENGTH_SHORT).show();
                    access.insert(currentMusic);
                }
            });

            return view;
        }
    }

    class ViewHolder {
        TextView songTitle, songArtist;
        ImageView imageLike;

        ViewHolder(View base) {
            this.songArtist = (TextView) base.findViewById(R.id.songArtist);
            this.songTitle = (TextView) base.findViewById(R.id.songTitle);
            this.imageLike = (ImageView) base.findViewById(R.id.imageLike);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflate = getActivity().getMenuInflater();
        menuInflate.inflate(R.menu.context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        ArrayAdapter<Music> adapter = (ArrayAdapter<Music>) listview
                .getAdapter();

        /*MusicAccessDatabase writeDatabase = new MusicAccessDatabase(getActivity());
        writeDatabase.openToWrite();
        switch (item.getItemId()) {
            case R.id.add:
                Music currentMusic = adapter.getItem(adapterInfo.position);
                Toast.makeText(
                        getActivity(),
                        currentMusic.getSongTitle()
                                + " is add to your favorite list ",
                        Toast.LENGTH_SHORT).show();

                writeDatabase.add(currentMusic);
                writeDatabase.close();
                break;

            default:
                break;
        }*/
        MusicRealmAccess access = new MusicRealmAccess();
        switch (item.getItemId()) {
            case R.id.add:
                Music currentMusic = adapter.getItem(adapterInfo.position);
                Toast.makeText(
                        getActivity(),
                        currentMusic.getSongTitle()
                                + " is add to your favorite list ",
                        Toast.LENGTH_SHORT).show();
                access.insert(currentMusic);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

}
