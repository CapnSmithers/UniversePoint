package edu.mines.msmith1.universepoint.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.Game;

/**
 * DAO for {@link Game} table. Eager fetches {@link Team} objects.
 * @author vanxrice
 */
public class GameDAO extends BaseDAO {
	private String LOG_TAG = GameDAO.class.getSimpleName();
	
	private String[] columns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_TEAM_1_ID, SQLiteHelper.COLUMN_TEAM_2_ID};
	private TeamDAO teamDAO;

	public GameDAO(Context context) {
		super(context);
		
		teamDAO = new TeamDAO(context);
	}
	
	/**
	 * @param game to be added
	 * @return persisted game
	 */
	public Game createGame(Game game) {
		ContentValues values = getContentValues(game);
		long id = db.insert(SQLiteHelper.TABLE_GAME, null, values);
		return getGameById(id);
	}
	
	/**
	 * @param game to be updated
	 * @return updated game
	 */
	public Game updateGame(Game game) {
		String[] whereArgs = getWhereArgsWithId(game);
		ContentValues values = getContentValues(game);
		long id = db.update(SQLiteHelper.TABLE_GAME, values, WHERE_SELECTION_FOR_ID, whereArgs);
		return getGameById(id);
	}

	/**
	 * @param game to be removed from database
	 */
	public void deleteGame(Game game) {
		String[] whereArgs = getWhereArgsWithId(game);
		Log.d(LOG_TAG, "deleting game with id " + game.getId());
		db.delete(SQLiteHelper.TABLE_GAME, WHERE_SELECTION_FOR_ID, whereArgs);
	}
	
	/**
	 * @param id
	 * @return Game matching id, null if id is not found
	 */
	public Game getGameById(long id) {
		String[] whereArgs = {String.valueOf(id)};
		Cursor cursor = db.query(SQLiteHelper.TABLE_GAME, columns, WHERE_SELECTION_FOR_ID,
				whereArgs, null, null, null);
		cursor.moveToFirst();
		Game game = cursorToGame(cursor);
		cursor.close();
		return game;
	}
	
	/**
	 * Converts a {@link Cursor} to {@link Game}
	 * @param cursor
	 */
	private Game cursorToGame(Cursor cursor) {
		Game game = null;
		if (!cursor.isAfterLast()) {
			game = new Game();
			game.setId(cursor.getLong(0));
			game.setTeam1(teamDAO.getTeamById(cursor.getLong(1)));
			game.setTeam2(teamDAO.getTeamById(cursor.getLong(2)));
		}
		return game;
	}

	/**
	 * Populates {@link ContentValues} with values from {@link Game}.
	 * @param game
	 */
	private ContentValues getContentValues(Game game) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TEAM_1_ID, game.getTeam1().getId());
		values.put(SQLiteHelper.COLUMN_TEAM_2_ID, game.getTeam2().getId());
		return values;
	}
	
	@Override
	public void open() {
		super.open();
		teamDAO.open();
	}
	
	@Override
	public void close() {
		super.close();
		teamDAO.close();
	}
}
