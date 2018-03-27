package com.hn.huy.bedtimemusicver2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hn.huy.bedtimemusicver2.R;
import com.hn.huy.bedtimemusicver2.model.business.MusicAccessDatabase;
import com.hn.huy.bedtimemusicver2.model.entity.Music;
import com.hn.huy.bedtimemusicver2.service.PlayerService;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hn.huy.bedtimemusicver2.R.layout.music_item;

public class AllListTab extends Fragment {

	private ListView listview;
	private ArrayAdapter<Music> adapter;
	private ArrayList<HashMap<String, String>> listSong;
	private ViewHolder viewHolder;
	public static Intent playerService;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.music_fragment, null);

		try {
		//	listSong = MainActivity.songsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// listview = (ListView) view.findViewById(R.id.listview);
		adapter = new MyAdapter(getActivity(), music_item, getItems());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				playerService = new Intent(getActivity(), PlayerService.class);
				playerService.putExtra("songIndex", position);
				playerService.putExtra("Key", 1);
				if (PlayerService.mediaPlayer == null) {
					getActivity().startService(playerService);
				} else {
					getActivity().stopService(playerService);
					getActivity().startService(playerService);
				}
				getActivity().finish();
			}
		});

		registerForContextMenu(listview);
		return view;
	}

	private class MyAdapter extends ArrayAdapter<Music> {

		ArrayList<Music> arrayList;

		public MyAdapter(Context context, int resource, ArrayList<Music> objects) {
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

	/**
	 * Tra lai List Music cho ListView
	 * */

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

	class ViewHolder {
		TextView songTitle, songArtist;

		ViewHolder(View base) {
			this.songArtist = (TextView) base.findViewById(R.id.songArtist);
			this.songTitle = (TextView) base.findViewById(R.id.songTitle);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
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

		MusicAccessDatabase writeDatabase = new MusicAccessDatabase(getActivity());
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
		}
		return super.onContextItemSelected(item);
	}

}
