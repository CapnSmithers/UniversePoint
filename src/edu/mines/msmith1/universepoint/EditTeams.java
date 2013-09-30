package edu.mines.msmith1.universepoint;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EditTeams extends ListActivity {
	public static final int ADD_TEAM_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_TEAM_ID, 0, R.string.addNewTeam);
		return result;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case ADD_TEAM_ID:
				addTeam();
				return true;
			default:
				return false;
		}
	}
	
	private void addTeam() {
		// TODO Auto-generated method stub
		
	}
	
}


