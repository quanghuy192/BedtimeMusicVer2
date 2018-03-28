package com.hn.huy.bedtimemusicver2.model.business;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadDataFromSdCard {
    public List<HashMap<String, String>> getListSong(Context context) {
        String where = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        List<HashMap<String, String>> songList = new ArrayList<>();
        Cursor managedCursorExternal;
        try {

            managedCursorExternal = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.DATA,
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.ALBUM,
                            MediaStore.Audio.Media.ALBUM_ID,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.ARTIST_ID}, where, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            while (managedCursorExternal.moveToNext()) {
                HashMap<String, String> song = new HashMap<>();
                song.put("songTitle", managedCursorExternal.getString(0));
                song.put("songPath", managedCursorExternal.getString(1));
                song.put("songArtist", managedCursorExternal.getString(5));
                songList.add(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songList;
    }

}
