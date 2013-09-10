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
	protected void onDestroy() {
		// Delete scores from shared prefs
		super.onDestroy();
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}

	public void addTurn(View view) {
		//find which team to add to
		switch(view.getId()) {
		
			case R.id.team1AddTurn:
				team1Turns += 1;
				break;
				
			case R.id.team2AddTurn:
				team2Turns += 1;
				break;
		}
	}
	
	public void addPoint(View view) {
		//Find team to add to
		switch(view.getId()) {
			case R.id.team1AddPoint:
				Log.d("TEAM1", "Button 1!");
				team1Score += 1;
				//update display
				TextView team1Display = (TextView)findViewById(R.id.team1Score);
				team1Display.setText(String.valueOf(team1Score));
				break;
				
			case R.id.team2AddPoint:
				Log.d("TEAM2", "button 2!");
				team2Score += 1;
				//update display
				TextView team2Display = (TextView)findViewById(R.id.team2Score);
				team2Display.setText(String.valueOf(team2Score));
				break;
		}
	}

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
	
	public void finishGame(View view) {
		//Take user to score summary screen
		Intent intent = new Intent(this, ScoreSummary.class);
		//Pass team 1 name to score summary
		EditText et = (EditText) findViewById(R.id.team1Name);
		team1Name = et.getText().toString();
		intent.putExtra(TEAM1_NAME, team1Name);
		
		//And now team 2
		EditText et2 = (EditText) findViewById(R.id.team2Name);
		team2Name = et2.getText().toString();
		intent.putExtra(TEAM2_NAME, team2Name);
		
		//And now the scores and turnovers
		intent.putExtra(TEAM1_SCORE, team1Score);
		intent.putExtra(TEAM2_SCORE, team2Score);
		intent.putExtra(TEAM1_TURNS, team1Turns);
		intent.putExtra(TEAM2_TURNS, team2Turns);
		
		startActivity(intent);
	}
}

