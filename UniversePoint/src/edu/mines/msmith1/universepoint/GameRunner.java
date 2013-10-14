/**
 * Description: This class is the functionality behind the score-keeping screen.
 *   Displays two fragments - one persistent fragment that shows the score for both teams,
 *   and one dynamic fragment that switches between a list of players on team 1 and a screen
 *   that displays current statistics on a selected player stored in the database.  This 
 *   screen allows entry of stats and updates the score screen accordingly.  
 * 
 * @author Matt Smith, Van Rice
 */

package edu.mines.msmith1.universepoint;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GameRunner extends Activity implements PlayerListFragment.ListItemSelectedListener {

	public final static int FINISH_GAME_ID = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scorekeeping);
		
		//Grab team id
		Intent intent = getIntent();
		Long teamId = intent.getLongExtra(EditTeams.EXTRA_TEAM_ID, 0);
		
		//Set Team 1 based on id
		
		
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
	
	
	/**
	 * Method to take the user to the summary screen.  Accessed by pressing
	 * the finish game button in the settings menu.
	 */
	private void finishGame() {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(this, R.string.noNotYet, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	// Swaps fragments
	@Override
	public void listItemSelected(int position) {
		// TODO Swap out player list fragment with player stats fragment
		
		
	}
	
}

