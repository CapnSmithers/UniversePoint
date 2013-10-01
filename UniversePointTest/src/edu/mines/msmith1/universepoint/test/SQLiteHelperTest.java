package edu.mines.msmith1.universepoint.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.mines.msmith1.universepoint.SQLiteHelper;

import android.database.Cursor;

public class SQLiteHelperTest extends DatabaseBaseTest {
	@Test
	public void testCreateDatabase() {
		List<String> tables = new ArrayList<String>();
		// query for all tables
		Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			tables.add(cursor.getString(1));
			cursor.moveToNext();
		}
		cursor.close();
		
		assertTrue(tables.contains(SQLiteHelper.TABLE_TEAM));
		assertTrue(tables.contains(SQLiteHelper.TABLE_PLAYER));
	}
}
