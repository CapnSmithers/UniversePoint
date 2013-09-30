package edu.mines.msmith1.universepoint.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.Team;

public class TeamDAO extends BaseDAO {
	private static final String LOG_TAG = TeamDAO.class.getSimpleName();
	
	private String[] columns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_NAME};
	
	public TeamDAO(Context context) {
		super(context);
	}
	
	public Team createTeam(Team team) {
		ContentValues values = getContentValues(team);
		long insertId = db.insert(SQLiteHelper.TABLE_TEAM, null, values);
		return getTeamById(insertId);
	}
	
	public Team updateTeam(Team team) {
		String[] whereArgs = getWhereArgsWithId(team);
		ContentValues values = getContentValues(team);
		db.update(SQLiteHelper.TABLE_TEAM, values, WHERE_SELECTION_FOR_ID, whereArgs);
		return getTeamById(team.getId());
	}
	
	public void deleteTeam(Team team) {
		String[] whereArgs = getWhereArgsWithId(team);
		Log.d(LOG_TAG, "deleting team with id " + whereArgs[0]);
		db.delete(SQLiteHelper.TABLE_TEAM, WHERE_SELECTION_FOR_ID, whereArgs);
	}
	
	public Team getTeamById(long id) {
		String[] whereArgs = {String.valueOf(id)};
		Cursor cursor = db.query(SQLiteHelper.TABLE_TEAM, columns, WHERE_SELECTION_FOR_ID, whereArgs,
				null, null, null);
		cursor.moveToFirst();
		Team team = cursorToTeam(cursor);
		cursor.close();
		return team;
	}
	
	private Team cursorToTeam(Cursor cursor) {
		Team team = null;
		if (!cursor.isAfterLast()) {
			team = new Team();
			team.setId(cursor.getLong(0));
			team.setName(cursor.getString(1));
		}
		return team;
	}

	private ContentValues getContentValues(Team team) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, team.getName());
		return values;
	}
}
