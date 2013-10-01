package edu.mines.msmith1.universepoint.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.Game;

public class GameDAO extends BaseDAO {
	private String LOG_TAG = GameDAO.class.getSimpleName();
	
	private String[] columns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_TEAM_1_ID, SQLiteHelper.COLUMN_TEAM_2_ID};
	private TeamDAO teamDAO;

	public GameDAO(Context context) {
		super(context);
		
		teamDAO = new TeamDAO(context);
		teamDAO.open();
	}
	
	public Game createGame(Game game) {
		ContentValues values = getContentValues(game);
		long id = db.insert(SQLiteHelper.TABLE_GAME, null, values);
		return getGameById(id);
	}
	
	public Game updateGame(Game game) {
		String[] whereArgs = getWhereArgsWithId(game);
		ContentValues values = getContentValues(game);
		long id = db.update(SQLiteHelper.TABLE_GAME, values, WHERE_SELECTION_FOR_ID, whereArgs);
		return getGameById(id);
	}

	public void deleteGame(Game game) {
		String[] whereArgs = getWhereArgsWithId(game);
		Log.d(LOG_TAG, "deleting game with id " + game.getId());
		db.delete(SQLiteHelper.TABLE_GAME, WHERE_SELECTION_FOR_ID, whereArgs);
	}
	
	public Game getGameById(long id) {
		String[] whereArgs = {String.valueOf(id)};
		Cursor cursor = db.query(SQLiteHelper.TABLE_GAME, columns, WHERE_SELECTION_FOR_ID,
				whereArgs, null, null, null);
		cursor.moveToFirst();
		Game game = cursorToGame(cursor);
		cursor.close();
		return game;
	}
	
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

	private ContentValues getContentValues(Game game) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TEAM_1_ID, game.getTeam1().getId());
		values.put(SQLiteHelper.COLUMN_TEAM_2_ID, game.getTeam2().getId());
		return values;
	}
	
	@Override
	public void close() {
		super.close();
		teamDAO.close();
	}
}
