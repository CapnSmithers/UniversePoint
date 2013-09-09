package edu.mines.msmith1.universepoint;

import edu.mines.msmith1.universepoint.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
		String team1Name = et.getText().toString();
		intent.putExtra(TEAM1_NAME, team1Name);
		
		//And now team 2
		EditText et2 = (EditText) findViewById(R.id.team2Name);
		String team2Name = et2.getText().toString();
		intent.putExtra(TEAM2_NAME, team2Name);
		
		//And now the scores and turnovers
		intent.putExtra(TEAM1_SCORE, team1Score);
		intent.putExtra(TEAM2_SCORE, team2Score);
		intent.putExtra(TEAM1_TURNS, team1Turns);
		intent.putExtra(TEAM2_TURNS, team2Turns);
		
		startActivity(intent);
	}
}

