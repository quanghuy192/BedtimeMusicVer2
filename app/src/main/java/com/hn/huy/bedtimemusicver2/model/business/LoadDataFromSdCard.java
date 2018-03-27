package com.hn.huy.bedtimemusicver2.model.business;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadDataFromSdCard {
	public ArrayList<HashMap<String, String>> getList(Context context) {

		String where = MediaStore.Audio.Media.IS_MUSIC + "=1";

		ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();
		Cursor managedCursor;
		try {
			managedCursor = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Audio.Media.TITLE,
							MediaStore.Audio.Media.DATA,
							MediaStore.Audio.Media._ID,
							MediaStore.Audio.Media.ALBUM,
							MediaStore.Audio.Media.ALBUM_ID,
							MediaStore.Audio.Media.ARTIST,
							MediaStore.Audio.Media.ARTIST_ID }, where, null,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			while (managedCursor.moveToNext()) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", managedCursor.getString(0));
				song.put("songPath", managedCursor.getString(1));
				song.put("songArtist", managedCursor.getString(5));
				songList.add(song);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return songList;
	}

}
