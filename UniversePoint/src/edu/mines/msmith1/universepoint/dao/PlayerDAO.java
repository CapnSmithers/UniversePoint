package edu.mines.msmith1.universepoint.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.Player;

/**
 * DAO for {@link Player} table. Eager fetches {@link Team} objects.
 * @author vanxrice
 */
public class PlayerDAO extends BaseDAO {
	private static final String LOG_TAG = PlayerDAO.class.getSimpleName();
	
	private String[] columns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_NAME, SQLiteHelper.COLUMN_TEAM_ID};
	private TeamDAO teamDAO;
	
	public PlayerDAO(Context context) {
		super(context);
		teamDAO = new TeamDAO(context);
		teamDAO.open();
	}
	
	/**
	 * @param player to be added
	 * @return persisted player
	 */
	public Player createPlayer(Player player) {
		ContentValues values = getContentValues(player);
		long insertId = db.insert(SQLiteHelper.TABLE_PLAYER, null, values);
		return getPlayerById(insertId);
	}
	
	/**
	 * @param player to be updated
	 * @return updated player
	 */
	public Player updatePlayer(Player player) {
		String[] whereArgs = getWhereArgsWithId(player);
		ContentValues values = getContentValues(player);
		db.update(SQLiteHelper.TABLE_PLAYER, values, WHERE_SELECTION_FOR_ID, whereArgs);
		return getPlayerById(player.getId());
	}
	
	/**
	 * @param player to be deleted
	 */
	public void deletePlayer(Player player) {
		String[] whereArgs = getWhereArgsWithId(player);
		Log.d(LOG_TAG, "deleting player with id " + whereArgs[0]);
		db.delete(SQLiteHelper.TABLE_PLAYER, WHERE_SELECTION_FOR_ID, whereArgs);
	}
	
	/**
	 * @return all players
	 */
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
	
	/**
	 * @param id
	 * @return player matching id, null if id is not found
	 */
	public Player getPlayerById(long id) {
		String[] whereArgs = {String.valueOf(id)};
		Cursor cursor = db.query(SQLiteHelper.TABLE_PLAYER, columns, WHERE_SELECTION_FOR_ID,
				whereArgs, null, null, null);
		cursor.moveToFirst();
		Player player = cursorToPlayer(cursor);
		cursor.close();
		return player;
	}
	
	/**
	 * Converts a {@link Cursor} to {@link Player}
	 * @param cursor
	 */
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

	/**
	 * Populates {@link ContentValues} with values from {@link Player}
	 * @param player
	 */
	private ContentValues getContentValues(Player player) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, player.getName());
		values.put(SQLiteHelper.COLUMN_TEAM_ID, player.getTeam().getId());
		return values;
	}
	
	@Override
	public void close() {
		super.close();
		teamDAO.close();
	}
}
