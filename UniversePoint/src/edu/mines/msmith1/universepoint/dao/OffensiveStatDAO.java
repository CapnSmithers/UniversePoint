package edu.mines.msmith1.universepoint.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.Game;
import edu.mines.msmith1.universepoint.dto.OffensiveStat;
import edu.mines.msmith1.universepoint.dto.Player;
import edu.mines.msmith1.universepoint.dto.Team;

/**
 * DAO for {@link OffensiveStat} table. Eager fetches {@link Game} and {@link Player}s.
 * @author vanxrice
 */
public class OffensiveStatDAO extends BaseDAO {
	private String LOG_TAG = OffensiveStatDAO.class.getSimpleName();

	private String[] columns = {SQLiteHelper.COLUMN_ID,
			SQLiteHelper.COLUMN_GAME_ID,
			SQLiteHelper.COLUMN_TEAM_ID,
			SQLiteHelper.COLUMN_PLAYER_ID,
			SQLiteHelper.COLUMN_ASSISTING_PLAYER_ID};
	private GameDAO gameDAO;
	private PlayerDAO playerDAO;
	private TeamDAO teamDAO;
	
	public OffensiveStatDAO(Context context) {
		super(context);

		gameDAO = new GameDAO(context);
		gameDAO.open();
		
		playerDAO = new PlayerDAO(context);
		playerDAO.open();
		
		teamDAO = new TeamDAO(context);
		teamDAO.open();
	}
	
	/**
	 * Helper method to persist multiple offensive stats, use to reduce the number of database calls
	 * @param offensiveStats to be added to the database
	 * @return persisted offensive stats
	 */
	public List<OffensiveStat> createOffensiveStats(List<OffensiveStat> offensiveStats) {
		List<OffensiveStat> newOffensiveStats = new ArrayList<OffensiveStat>();
		for (OffensiveStat offensiveStat : offensiveStats) {
			newOffensiveStats.add(createOffensiveStat(offensiveStat));
		}
		return newOffensiveStats;
	}
	
	/**
	 * @param offensiveStat to be added
	 * @return persisted offensiveStat
	 */
	public OffensiveStat createOffensiveStat(OffensiveStat offensiveStat) {
		ContentValues values = getContentValues(offensiveStat);
		long id = db.insert(SQLiteHelper.TABLE_OFFENSIVE_STAT, null, values);
		return getOffensiveStatById(id);
	}
	
	// TODO updateOffensiveStat
	
	/**
	 * @param offensiveStat to be removed from database
	 */
	public void deleteOffensiveStat(OffensiveStat offensiveStat) {
		String[] whereArgs = getWhereArgsWithId(offensiveStat);
		Log.d(LOG_TAG, "deleting offensive_stat with id " + offensiveStat.getId());
		db.delete(SQLiteHelper.TABLE_OFFENSIVE_STAT, WHERE_SELECTION_FOR_ID, whereArgs);
	}
	
	/**
	 * @param id
	 * @return OffensiveStat matching id, null if id is not found
	 */
	public OffensiveStat getOffensiveStatById(long id) {
		String[] whereArgs = {String.valueOf(id)};
		Cursor cursor = db.query(SQLiteHelper.TABLE_OFFENSIVE_STAT, columns, WHERE_SELECTION_FOR_ID,
				whereArgs, null, null, null);
		cursor.moveToFirst();
		OffensiveStat offensiveStat = cursorToOffensiveStat(cursor);
		cursor.close();
		return offensiveStat;
	}
	
	/**
	 * @param player
	 * @return List of all OffensiveStat for the player
	 */
	public List<OffensiveStat> getAllOffensiveStatsForPlayer(Player player) {
		List<OffensiveStat> offensiveStats = new ArrayList<OffensiveStat>();
		String[] whereArgs = {String.valueOf(player.getId())};
		Cursor cursor = db.query(SQLiteHelper.TABLE_OFFENSIVE_STAT, columns, "player_id = ?",
				whereArgs, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			offensiveStats.add(cursorToOffensiveStat(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return offensiveStats;
	}
	
	/**
	 * @param team
	 * @param game
	 * @return List of OffensiveStat for the team and game
	 */
	public List<OffensiveStat> getScoreForTeam(Team team, Game game) {
		List<OffensiveStat> offensiveStats = new ArrayList<OffensiveStat>();
		String[] whereArgs = {String.valueOf(team.getId()), String.valueOf(game.getId())};
		Cursor cursor = db.query(SQLiteHelper.TABLE_OFFENSIVE_STAT, columns, "game_id = ? AND team_id = ?",
				whereArgs, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			offensiveStats.add(cursorToOffensiveStat(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return offensiveStats;
	}
	
	/**
	 * Converts a {@link Cursor} to {@link OffensiveStat}
	 * @param cursor
	 */
	private OffensiveStat cursorToOffensiveStat(Cursor cursor) {
		OffensiveStat offensiveStat = null;
		if (!cursor.isAfterLast()) {
			offensiveStat = new OffensiveStat();
			offensiveStat.setId(cursor.getLong(0));
			offensiveStat.setGame(gameDAO.getGameById(cursor.getLong(1)));
			offensiveStat.setTeam(teamDAO.getTeamById(cursor.getLong(2)));
			
			Long playerId = cursor.getLong(3);
			if (playerId != null)
				offensiveStat.setPlayer(playerDAO.getPlayerById(playerId));
			else
				offensiveStat.setPlayer(null);
			
			Long assistingPlayerId = cursor.getLong(4);
			if (assistingPlayerId != null)
				offensiveStat.setAssistingPlayer(playerDAO.getPlayerById(assistingPlayerId));
			else
				offensiveStat.setAssistingPlayer(null);
		}
		return offensiveStat;
	}
	
	/**
	 * Populates {@link ContentValues} with values from {@link OffensiveStat}
	 * @param offensiveStat
	 * @return
	 */
	private ContentValues getContentValues(OffensiveStat offensiveStat) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_GAME_ID, offensiveStat.getGame().getId());
		values.put(SQLiteHelper.COLUMN_TEAM_ID, offensiveStat.getTeam().getId());
		if (offensiveStat.getPlayer() != null)
			values.put(SQLiteHelper.COLUMN_PLAYER_ID, offensiveStat.getPlayer().getId());
		if (offensiveStat.getAssistingPlayer() != null)
			values.put(SQLiteHelper.COLUMN_ASSISTING_PLAYER_ID, offensiveStat.getAssistingPlayer().getId());
		return values;
	}
	
	@Override
	public void close() {
		super.close();
		gameDAO.close();
		playerDAO.close();
	}

}
