/**
 * Description: This class is the functionality behind the score-keeping screen.
 *   Provides methods to add/remove points and turnovers, in addition to saving
 *   information across onPause() and onStop() calls. Also provides navigation to
 *   the score summary screen via the Finish Game button.
 * 
 * @author Matt
 */

package edu.mines.msmith1.universepoint;

import edu.mines.msmith1.universepoint.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameRunner extends FragmentActivity {

	//Constants for intent messages - for package-unique names
	public final static int FINISH_GAME_ID = 1;
	
	//variables to keep track of score, name, and turnovers
	private int team1Score, team2Score, team1Turns, team2Turns = 0;
	private String team1Name, team2Name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scorekeeping);
		
		Log.d("APP_CREATED", "New instance of universe point");
		

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
		Log.d("ON_PAUSE", "onPause called");

	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	private void finishGame() {
		// TODO Auto-generated method stub
		
	}
	

}

