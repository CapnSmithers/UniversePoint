/**
 * Description: Handles database creation. Enables foreign keys for all database operations.
 * 
 * @author Van Rice, Matt Smith
 */
package edu.mines.msmith1.universepoint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	// TABLES
	public static final String TABLE_PLAYER = "player";
	public static final String TABLE_TEAM = "team";
	public static final String TABLE_GAME = "game";
	public static final String TABLE_OFFENSIVE_STAT= "offensive_stat";
	// COLUMNS
	public static final String COLUMN_ID = "_id"; // primary key of each table, this is the default name in SQLite
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TEAM_ID = "team_id";
	public static final String COLUMN_TEAM_1_ID = "team_1_id";
	public static final String COLUMN_TEAM_2_ID = "team_2_id";
	public static final String COLUMN_GAME_ID = "game_id";
	public static final String COLUMN_PLAYER_ID ="player_id";
	public static final String COLUMN_ASSISTING_PLAYER_ID = "assisting_player_id";
	// RANDOM SQL STATEMENTS
	private static final String COLUMN_ID_CREATE_STMT = COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
	private static final String PRAGMA_FOREIGN_KEYS = "PRAGMA foreign_keys = ON;";
	// CREATE TABLE STATEMENTS
	private static final String CREATE_TABLE_TEAM = "create table " + TABLE_TEAM + " (" + COLUMN_ID_CREATE_STMT + "name TEXT NOT NULL);";
	private static final String CREATE_TABLE_PLAYER = "create table " + TABLE_PLAYER + " (" + COLUMN_ID_CREATE_STMT +"name TEXT NOT NULL, team_id INTEGER NOT NULL, FOREIGN KEY(team_id) REFERENCES team(_id));";
	private static final String CREATE_TABLE_GAME = "create table " + TABLE_GAME + " (" + COLUMN_ID_CREATE_STMT + "team_1_id INTEGER NOT NULL, team_2_id INTEGER NOT NULL, FOREIGN KEY(team_1_id) REFERENCES team(_id), FOREIGN KEY(team_2_id) REFERENCES team(_id))";
	private static final String CREATE_TABLE_OFFENSIVE_STAT = "create table " + TABLE_OFFENSIVE_STAT + " (" + COLUMN_ID_CREATE_STMT + "game_id INTEGER NOT NULL, team_id INTEGER NOT NULL, player_id INTEGER, assisting_player_id INTEGER, FOREIGN KEY(game_id) REFERENCES game(_id), FOREIGN KEY(team_id) REFERENCES team(_id), FOREIGN KEY(player_id) REFERENCES player(_id), FOREIGN KEY(assisting_player_id) REFERENCES player(_id));";

	private static final String DATABASE_NAME = "universePoint.db";
	private static final int DATABASE_VERSION = 1;
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TEAM);
		db.execSQL(CREATE_TABLE_PLAYER);
		db.execSQL(CREATE_TABLE_GAME);
		db.execSQL(CREATE_TABLE_OFFENSIVE_STAT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// We won't really need this unless we plan on deploying to the play store
	}
	
	/**
	 * Insures foreign keys are enforced.
	 * @return super.getWritableDatabase();
	 */
	@Override
	public SQLiteDatabase getWritableDatabase() {
		SQLiteDatabase db = super.getWritableDatabase();
		db.execSQL(PRAGMA_FOREIGN_KEYS);
		return db;
	}
	
	/**
	 * Deletes all rows from all tables in the database
	 */
	public void truncateTables() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_OFFENSIVE_STAT, null, null);
		db.delete(TABLE_PLAYER, null, null);
		db.delete(TABLE_GAME, null, null);
		db.delete(TABLE_TEAM, null, null);
		db.close();
	}
}
