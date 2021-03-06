package edu.mines.msmith1.universepoint.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.mines.msmith1.universepoint.SQLiteHelper;
import edu.mines.msmith1.universepoint.dto.BaseDTO;

/**
 * Handles core DAO functionality and random helper functions.
 * @author vanxrice
 */
public abstract class BaseDAO {
	protected static final String WHERE_SELECTION_FOR_ID = SQLiteHelper.COLUMN_ID + " = ?";
	
	protected SQLiteDatabase db;
	protected SQLiteHelper dbHelper;
	
	public BaseDAO(Context context) {
		dbHelper = new SQLiteHelper(context);
	}
	
	/**
	 * Opens a connection to the database
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Closes the database connection
	 */
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * @param player
	 * @return creates String[] containing only the player id
	 */
	protected String[] getWhereArgsWithId(BaseDTO baseDto) {
		String[] whereArgs = {String.valueOf(baseDto.getId())};
		return whereArgs;
	}
}
