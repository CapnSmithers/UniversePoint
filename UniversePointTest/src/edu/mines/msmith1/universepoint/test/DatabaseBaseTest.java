package edu.mines.msmith1.universepoint.test;

import edu.mines.msmith1.universepoint.SQLiteHelper;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class DatabaseBaseTest extends AndroidTestCase {
	protected RenamingDelegatingContext context;
	SQLiteHelper sqLiteHelper;
	SQLiteDatabase db;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		context = new RenamingDelegatingContext(getContext(), "_test");
		sqLiteHelper = new SQLiteHelper(context);
		db = sqLiteHelper.getWritableDatabase();
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		sqLiteHelper.close();
	}
}
