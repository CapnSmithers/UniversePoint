package edu.mines.msmith1.universepoint.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
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
			SQLiteHelper.COLUMN_ASSISTING_PLAYER_ID,
			SQLiteHelper.COLUMN_TURNOVER_PLAYER_ID}; // shouldn't have removed the offensive_stat_type_ref table
	private GameDAO gameDAO;
	private PlayerDAO playerDAO;
	private TeamDAO teamDAO;
	
	public OffensiveStatDAO(Context context) {
		super(context);

		gameDAO = new GameDAO(context);
		playerDAO = new PlayerDAO(context);
		teamDAO = new TeamDAO(context);
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
	 * @return List of all OffensiveStat where the player scored a point
	 */
	public List<OffensiveStat> getAllPointsForPlayer(Player player) {
		return getAllOffensiveStatForPlayer(player, SQLiteHelper.COLUMN_PLAYER_ID + " = ?");
	}
	
	/**
	 * @param player
	 * @return List of all OffensiveStat where the player has an assist
	 */
	public List<OffensiveStat> getAllAssistsForPlayer(Player player) {
		return getAllOffensiveStatForPlayer(player, SQLiteHelper.COLUMN_ASSISTING_PLAYER_ID +" = ?");
	}
	
	/**
	 * @param player
	 * @return List of all OffensiveStat where the player has a turnover
	 */
	public List<OffensiveStat> getAllTurnoversForPlayer(Player player) {
		return getAllOffensiveStatForPlayer(player, SQLiteHelper.COLUMN_TURNOVER_PLAYER_ID + " = ?");
	}
	
	/**
	 * Helper method to getAll*ForPlayer queries
	 * @param player
	 * @param whereClause
	 * @return
	 */
	private List<OffensiveStat> getAllOffensiveStatForPlayer(Player player, String whereClause) {
		String[] whereArgs = {String.valueOf(player.getId())};
		Cursor cursor = db.query(SQLiteHelper.TABLE_OFFENSIVE_STAT, columns, whereClause,
				whereArgs, null, null, null);
		return cursorToList(cursor);
	}
	
	/**
	 * @param game
	 * @return all offensive stats for the game
	 */
	public List<BaseDTO> getAllOffensiveStatForGame(Game game) {
		List<BaseDTO> offensiveStats = new ArrayList<BaseDTO>();
		String[] whereArgs = {String.valueOf(game.getId())};
		Cursor cursor = db.query(SQLiteHelper.TABLE_OFFENSIVE_STAT, columns, "game_id = ?", whereArgs, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			offensiveStats.add(cursorToOffensiveStat(cursor));
			cursor.moveToNext();
		}
		return offensiveStats;
	}

	/**
	 * @param cursor to be examined
	 * @return a non-null {@link List} of {@link OffensiveStat}
	 */
	private List<OffensiveStat> cursorToList(Cursor cursor) {
		List<OffensiveStat> offensiveStats = new ArrayList<OffensiveStat>();
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
	public List<OffensiveStat> getOffensiveStatsForTeam(Team team, Game game) {
		String[] whereArgs = {String.valueOf(team.getId()), String.valueOf(game.getId())};
		Cursor cursor = db.query(SQLiteHelper.TABLE_OFFENSIVE_STAT, columns, "team_id = ? AND game_id = ?",
				whereArgs, null, null, null);
		return cursorToList(cursor);
	}
	
	/**
	 * Deletes all the {@link OffensiveStat}s for the associated game
	 * @param game
	 */
	public void deleteOffensiveStatsForGame(Game game) {
		String[] whereArgs = {String.valueOf(game.getId())};
		Log.d(LOG_TAG, "deleting offensive_stat for game " + game.getId());
		db.delete(SQLiteHelper.TABLE_OFFENSIVE_STAT, "game_id = ?", whereArgs);
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
			
			Long turnoverPlayerId = cursor.getLong(5);
			if (turnoverPlayerId != null)
				offensiveStat.setTurnoverPlayer(playerDAO.getPlayerById(turnoverPlayerId));
			else
				offensiveStat.setTurnoverPlayer(null);
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
		if (offensiveStat.getTurnoverPlayer() != null)
			values.put(SQLiteHelper.COLUMN_TURNOVER_PLAYER_ID, offensiveStat.getTurnoverPlayer().getId());
		return values;
	}
	
	@Override
	public void open() {
		super.open();
		gameDAO.open();
		playerDAO.open();
		teamDAO.open();
	}
	
	@Override
	public void close() {
		super.close();
		gameDAO.close();
		playerDAO.close();
		teamDAO.close();
	}

}
