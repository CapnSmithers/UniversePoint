package edu.mines.msmith1.universepoint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_PLAYER = "player";
	public static final String COLUMN_ID = "_id"; // primary key of each table, this is the default name in SQLite
	public static final String COLUMN_NAME = "name";
	private static final String COLUMN_ID_CREATE_STMT = COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
	
	private static final String DATABASE_NAME = "universePoint.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE_1 = "create table"
			+ TABLE_PLAYER + "(" + COLUMN_ID_CREATE_STMT +" name TEXT NOT NULL)";
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// We won't really need this unless we plan on deploying to the play store
	}
}
