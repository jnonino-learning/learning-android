package com.nonino.dailyselfie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final String NAME = "selfies_db";
	private static final int VERSION = 1;

	public final static String TABLE_NAME = "selfies";
	final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String PATH = "path";
	public final static String[] columns = {_ID, TITLE, PATH};

	private static final String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + "("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE
			+ " TEXT NOT NULL, " + PATH + " TEXT NOT NULL)";

	public DatabaseOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
}
