package edu.mines.msmith1.universepoint.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.Player;

public class PlayerDAO {
	private static final String LOG_TAG = PlayerDAO.class.getSimpleName();
	
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private String[] columns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_NAME};
	
	public PlayerDAO(Context context) {
		dbHelper = new SQLiteHelper(context);
	}
	
	// TODO move methods to a base DAO class
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Player createPlayer(Player player) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, player.getName());
		long insertId = db.insert(SQLiteHelper.TABLE_PLAYER, null, values);
		return getPlayerById(insertId);
	}
	
	public Player updatePlayer(Player player) {
		String[] whereArgs = getWhereArgsWithId(player);
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, player.getName());
		db.update(SQLiteHelper.TABLE_PLAYER, values, SQLiteHelper.COLUMN_ID + " = ?", whereArgs);
		return getPlayerById(player.getId());
	}
	
	public void deletePlayer(Player player) {
		String[] whereArgs = getWhereArgsWithId(player);
		Log.d(LOG_TAG, "deleting player with id " + whereArgs[0]);
		db.delete(SQLiteHelper.TABLE_PLAYER, SQLiteHelper.COLUMN_ID + " = ?", whereArgs);
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		
		Cursor cursor = db.query(SQLiteHelper.TABLE_PLAYER, columns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Player player = cursorToPlayer(cursor);
			players.add(player);
			cursor.moveToNext();
		}
		cursor.close();
		
		return players;
	}
	
	public Player getPlayerById(long id) {
		String[] whereArgs = {String.valueOf(id)};
		Cursor cursor = db.query(SQLiteHelper.TABLE_PLAYER, columns, SQLiteHelper.COLUMN_ID + " = ?",
				whereArgs, null, null, null);
		cursor.moveToFirst();
		Player player = cursorToPlayer(cursor);
		cursor.close();
		return player;
	}
	
	private Player cursorToPlayer(Cursor cursor) {
		Player player = new Player();
		player.setId(cursor.getLong(0));
		player.setName(cursor.getString(1));
		return player;
	}
	
	/**
	 * @param player
	 * @return creates String[] containing only the player id
	 */
	private String[] getWhereArgsWithId(Player player) {
		String[] whereArgs = {String.valueOf(player.getId())};
		return whereArgs;
	}
}
