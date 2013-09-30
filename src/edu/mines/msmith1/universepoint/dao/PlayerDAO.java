package edu.mines.msmith1.universepoint.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.Player;

public class PlayerDAO extends BaseDAO {
	private static final String LOG_TAG = PlayerDAO.class.getSimpleName();
	
	private String[] columns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_NAME, SQLiteHelper.COLUMN_TEAM_ID};
	private TeamDAO teamDAO;
	
	public PlayerDAO(Context context) {
		super(context);
		teamDAO = new TeamDAO(context);
		teamDAO.open();
	}
	
	public Player createPlayer(Player player) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, player.getName());
		values.put(SQLiteHelper.COLUMN_TEAM_ID, player.getTeam().getId());
		long insertId = db.insert(SQLiteHelper.TABLE_PLAYER, null, values);
		return getPlayerById(insertId);
	}
	
	public Player updatePlayer(Player player) {
		String[] whereArgs = getWhereArgsWithId(player);
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, player.getName());
		values.put(SQLiteHelper.COLUMN_TEAM_ID, player.getTeam().getId());
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
		Player player = null;
		if (!cursor.isAfterLast()) {
			player = new Player();
			player.setId(cursor.getLong(0));
			player.setName(cursor.getString(1));
			player.setTeam(teamDAO.getTeamById(cursor.getLong(2)));
		}
		return player;
	}
	
	@Override
	public void close() {
		super.close();
		teamDAO.close();
	}
}
