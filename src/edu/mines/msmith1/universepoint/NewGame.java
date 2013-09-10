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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewGame extends Activity {

	//Constants for intent messages - for package-unique names
	public final static String TEAM1_SCORE = "edu.mines.msmith1.universepoint.TEAM1_SCORE";
	public final static String TEAM2_SCORE = "edu.mines.msmith1.universepoint.TEAM2_SCORE";
	public final static String TEAM1_TURNS = "edu.mines.msmith1.universepoint.TEAM1_TURNS";
	public final static String TEAM2_TURNS = "edu.mines.msmith1.universepoint.TEAM2_TURNS";
	public final static String TEAM1_NAME = "edu.mines.msmith1.universepoint.TEAM1_NAME";
	public final static String TEAM2_NAME = "edu.mines.msmith1.universepoint.TEAM2_NAME";
	
	//variables to keep track of score, name, and turnovers
	private int team1Score, team2Score, team1Turns, team2Turns = 0;
	private String team1Name, team2Name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
		Log.d("APP_CREATED", "New instance of universe point");
		
		//Delete shared prefs, if any
		SharedPreferences sharedPref = this.getPreferences( Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
		
		//Set textviews 
		TextView score1 = (TextView) findViewById(R.id.team1Score);
		score1.setText(String.valueOf(team1Score));
		
		TextView score2 = (TextView) findViewById(R.id.team2Score);
		score2.setText(String.valueOf(team2Score));
	}		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Delete scores from shared prefs
		SharedPreferences sharedPref = this.getPreferences( Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
		
		Log.d("EXITING APP", "Universe Point is destroyed");
	}

	@Override
	protected void onPause() {
		Log.d("ON_PAUSE", "onPause called");
		
		EditText et = (EditText) findViewById(R.id.team1Name);
		team1Name = et.getText().toString();
				
		EditText et2 = (EditText) findViewById(R.id.team2Name);
		team2Name = et2.getText().toString();
		
		//Open shared prefs
		SharedPreferences sharedPref = this.getPreferences( Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		
		//Add values to shared prefs and commit
		editor.putString(TEAM1_NAME, team1Name);
		editor.putString(TEAM2_NAME, team2Name);
		editor.putInt(TEAM1_SCORE, team1Score);
		editor.putInt(TEAM2_SCORE, team2Score);
		editor.putInt(TEAM1_TURNS, team1Turns);
		editor.putInt(TEAM2_TURNS, team2Turns);
	    editor.commit();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// Open shared prefs
		SharedPreferences sharedPrefs = this.getPreferences( Context.MODE_PRIVATE );
		
		Log.d("ON_RESUME", "OnResume Called");
		
		//Assign shared prefs to appropriate values
		team1Name = sharedPrefs.getString(TEAM1_NAME, "");
		team2Name = sharedPrefs.getString(TEAM2_NAME, "");
		team1Score = sharedPrefs.getInt(TEAM1_SCORE, 0);
		team2Score = sharedPrefs.getInt(TEAM2_SCORE, 0);
		team1Turns = sharedPrefs.getInt(TEAM1_TURNS, 0);
		team2Turns = sharedPrefs.getInt(TEAM2_TURNS, 0);
		super.onResume();
	}

	/**
	 * This method adds one turnover to the count for either team, 
	 * based on which button was pressed.  The method detects the 
	 * button that was pressed, increments the correct variable
	 * and updates the display.
	 * Called by one of the Add Turn buttons
	 * 
	 * @param view - Button that was pressed (Team 1 Add Turn or Team 2 Add Turn)
	 */
	public void addTurn(View view) {
		//find which team to add to
		switch(view.getId()) {
		
			case R.id.team1AddTurn:
				team1Turns += 1;
				//update display
				TextView team1Display = (TextView)findViewById(R.id.team1Turns);
				team1Display.setText(String.valueOf(team1Turns));
				break;
				
			case R.id.team2AddTurn:
				team2Turns += 1;
				//update display
				TextView team2Display = (TextView)findViewById(R.id.team2Turns);
				team2Display.setText(String.valueOf(team2Turns));
				break;
		}
	}
	
	/**
	 * This method removes a point from the correct team using 
	 * the same process as addTurn, but also checks for negative input.
	 * Called by one of the Remove Turn buttons
	 * 
	 * @param view - Button that was pressed (Team 1 Remove Turn or Team 2 Remove Turn)
	 */
	public void removeTurn(View view) {
		switch(view.getId()) {
			case R.id.team1RemoveTurn:
				if (team1Turns - 1 >= 0) {
					team1Turns -= 1;
				} else {
					//toast pop-up saying can't go lower than 0
					Toast.makeText(getApplicationContext(), getString(R.string.removeError), Toast.LENGTH_SHORT).show();
				}
				//update display
				TextView team1Display = (TextView)findViewById(R.id.team1Turns);
				team1Display.setText(String.valueOf(team1Turns));
				break;
				
			case R.id.team2RemoveTurn:
				if (team2Turns - 1 >= 0) {
					team2Turns -= 1;
				} else {
					//toast pop-up saying can't go lower than 0
					Toast.makeText(getApplicationContext(), getString(R.string.removeError), Toast.LENGTH_SHORT).show();
				}
				//update display
				TextView team2Display = (TextView)findViewById(R.id.team2Turns);
				team2Display.setText(String.valueOf(team2Turns));
				break;
		}
	}
	
	/**
	 * This method adds one point to the score for either team, 
	 * based on which button was pressed.  The method detects the 
	 * button that was pressed, increments the correct variable
	 * and updates the display.
	 * Called by pressing one of the Add Point buttons
	 * 
	 * @param view - Button that was pressed (Team 1 Add Point or Team 2 Add Point)
	 */
	public void addPoint(View view) {
		//Find team to add to
		switch(view.getId()) {
			case R.id.team1AddPoint:
				team1Score += 1;
				//update display
				TextView team1Display = (TextView)findViewById(R.id.team1Score);
				team1Display.setText(String.valueOf(team1Score));
				break;
				
			case R.id.team2AddPoint:
				team2Score += 1;
				//update display
				TextView team2Display = (TextView)findViewById(R.id.team2Score);
				team2Display.setText(String.valueOf(team2Score));
				break;
		}
	}

	/**
	 * This method removes a point from the score using
	 * the same method as addPoint and checks for negative input.
	 * Called by pressing one of the Remove Point buttons
	 * 
	 * @param view - Button that was pressed (Team 1 Remove Point or Team 2 Remove Point
	 */
	public void removePoint(View view) {
		switch(view.getId()) {
			case R.id.team1RemovePoint:
				if (team1Score - 1 >= 0) {
					team1Score -= 1;
				} else {
					//toast pop-up saying can't go lower than 0
					Toast.makeText(getApplicationContext(), getString(R.string.removeError), Toast.LENGTH_SHORT).show();
				}
				//update display
				TextView team1Display = (TextView)findViewById(R.id.team1Score);
				team1Display.setText(String.valueOf(team1Score));
				break;
				
			case R.id.team2RemovePoint:
				if (team2Score - 1 >= 0) {
					team2Score -= 1;
				} else {
					//toast pop-up saying can't go lower than 0
					Toast.makeText(getApplicationContext(), getString(R.string.removeError), Toast.LENGTH_SHORT).show();
				}
				//update display
				TextView team2Display = (TextView)findViewById(R.id.team2Score);
				team2Display.setText(String.valueOf(team2Score));
				break;
		}
	}
	
	/**
	 * This method packages the collected variables for the game,
	 * creates a new Intent, and sends the collected variables to the
	 * summary screen.  
	 * Called by pressing the Finish Game button
	 * 
	 * @param view - Finish Game button
	 */
	public void finishGame(View view) {
		//Take user to score summary screen
		Intent intent = new Intent(this, ScoreSummary.class);
		//Pass team 1 name to score summary
		EditText et = (EditText) findViewById(R.id.team1Name);
		team1Name = et.getText().toString();
		if(team1Name == null || team1Name.isEmpty()) {
			team1Name = "Team 1";
		}
		intent.putExtra(TEAM1_NAME, team1Name);
		
		//And now team 2
		EditText et2 = (EditText) findViewById(R.id.team2Name);
		team2Name = et2.getText().toString();
		if(team2Name == null || team2Name.isEmpty()) {
			team2Name = "Team 2";
		}
		intent.putExtra(TEAM2_NAME, team2Name);
		
		//And now the scores and turnovers
		intent.putExtra(TEAM1_SCORE, team1Score);
		intent.putExtra(TEAM2_SCORE, team2Score);
		intent.putExtra(TEAM1_TURNS, team1Turns);
		intent.putExtra(TEAM2_TURNS, team2Turns);
		
		startActivity(intent);
	}
}

