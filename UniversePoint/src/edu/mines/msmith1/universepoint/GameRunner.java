/**
 * Description: This class is the functionality behind the score-keeping screen.
 *   Provides methods to add/remove points and turnovers, in addition to saving
 *   information across onPause() and onStop() calls. Also provides navigation to
 *   the score summary screen via the Finish Game button.
 * 
 * @author Matt
 */

package edu.mines.msmith1.universepoint;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class GameRunner extends Activity implements PlayerListFragment.ListItemSelectedListener {

	public final static int FINISH_GAME_ID = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scorekeeping);
		
		//Inflate player list fragment
		if(findViewById(R.id.listContainer) != null) {
			if (savedInstanceState != null) {
				return;
			}
			
			PlayerListFragment playerList = new PlayerListFragment();
			playerList.setArguments(getIntent().getExtras());       // TODO: populate player list with database entries for
																	// given team
			getFragmentManager().beginTransaction()
				.add(R.id.listContainer, (Fragment)playerList).commit();
		}
	}		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, FINISH_GAME_ID, 0, R.string.finishGame);
		return result;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case FINISH_GAME_ID:
				finishGame();
				return true;
			default:
				return false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d("EXITING APP", "Universe Point is destroyed");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("ON_PAUSE", "onPause called");

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void finishGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listItemSelected(int position) {
		// TODO Swap out player list fragment with player stats fragment
		
	}
	
}

