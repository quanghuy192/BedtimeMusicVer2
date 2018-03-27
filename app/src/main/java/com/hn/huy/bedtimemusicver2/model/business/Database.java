package com.hn.huy.bedtimemusicver2.model.business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	public static final String DATABASE_LIST = "listTable";
	private static final String DATABASE_NAME = "musicDatabase.db";

	public static final String KEY_ID = "key";

	public static final String PATH_NAME = "pathName";
	public static final String SONG_NAME = "songNameText";
	public static final String ARTIST_NAME = "artistName";

	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_TABLE_LIST = " create table "
			+ DATABASE_LIST + " ( " + KEY_ID
			+ " integer primary key autoincrement, " + PATH_NAME
			+ " text not null, " + ARTIST_NAME + " text not null, " + SONG_NAME
			+ " text not null); ";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_LIST);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_LIST);

		// recreate
		onCreate(db);
	}

}
