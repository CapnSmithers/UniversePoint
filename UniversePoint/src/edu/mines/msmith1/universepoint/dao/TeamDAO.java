package edu.mines.msmith1.universepoint.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
import edu.mines.msmith1.universepoint.dto.Team;

/**
 * DAO for {@link Team} table.
 * @author vanxrice
 */
public class TeamDAO extends BaseDAO {
	private static final String LOG_TAG = TeamDAO.class.getSimpleName();
	
	private String[] columns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_NAME};
	
	public TeamDAO(Context context) {
		super(context);
	}
	
	/**
	 * @param team to be added
	 * @return persisted team
	 */
	public Team createTeam(Team team) {
		ContentValues values = getContentValues(team);
		long insertId = db.insert(SQLiteHelper.TABLE_TEAM, null, values);
		return getTeamById(insertId);
	}
	
	/**
	 * @param team to be updated
	 * @return updated team
	 */
	public Team updateTeam(Team team) {
		String[] whereArgs = getWhereArgsWithId(team);
		ContentValues values = getContentValues(team);
		db.update(SQLiteHelper.TABLE_TEAM, values, WHERE_SELECTION_FOR_ID, whereArgs);
		return getTeamById(team.getId());
	}
	
	/**
	 * @param team to be deleted
	 */
	public void deleteTeam(Team team) {
		String[] whereArgs = getWhereArgsWithId(team);
		Log.d(LOG_TAG, "deleting team with id " + whereArgs[0]);
		db.delete(SQLiteHelper.TABLE_TEAM, WHERE_SELECTION_FOR_ID, whereArgs);
	}
	
	/**
	 * @return all teams
	 */
	public List<BaseDTO> getTeams() {
		List<BaseDTO> teams = new ArrayList<BaseDTO>();
		
		Cursor cursor = db.query(SQLiteHelper.TABLE_TEAM, columns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			BaseDTO team = cursorToTeam(cursor);
			teams.add(team);
			cursor.moveToNext();
		}
		cursor.close();
		
		return teams;
	}
	
	/**
	 * @param id
	 * @return team matching id, null if id is not found
	 */
	public Team getTeamById(long id) {
		String[] whereArgs = {String.valueOf(id)};
		Cursor cursor = db.query(SQLiteHelper.TABLE_TEAM, columns, WHERE_SELECTION_FOR_ID, whereArgs,
				null, null, null);
		cursor.moveToFirst();
		Team team = cursorToTeam(cursor);
		cursor.close();
		return team;
	}
	
	/**
	 * Converts a {@link Cursor} to {@link Team}
	 * @param cursor
	 */
	private Team cursorToTeam(Cursor cursor) {
		Team team = null;
		if (!cursor.isAfterLast()) {
			team = new Team();
			team.setId(cursor.getLong(0));
			team.setName(cursor.getString(1));
		}
		return team;
	}

	/**
	 * Populates {@link ContentValues} with values from {@link Team}
	 * @param team
	 */
	private ContentValues getContentValues(Team team) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, team.getName());
		return values;
	}
}
