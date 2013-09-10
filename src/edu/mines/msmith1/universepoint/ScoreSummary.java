package edu.mines.msmith1.universepoint;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreSummary extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_summary);
		
		Intent prevIntent = getIntent();
		Bundle b = prevIntent.getExtras();
		
		if (b != null) {
			String team1Name = b.getString(NewGame.TEAM1_NAME);
			String team2Name = b.getString(NewGame.TEAM2_NAME);
			int team1Score = b.getInt(NewGame.TEAM1_SCORE);
			int team2Score = b.getInt(NewGame.TEAM2_SCORE);
			int team1Turns = b.getInt(NewGame.TEAM1_TURNS);
			int team2Turns = b.getInt(NewGame.TEAM2_TURNS);
			
			//Set values for all of the score stuffs
			TextView name1 = (TextView) findViewById(R.id.box_team1Name);
			TextView name2 = (TextView) findViewById(R.id.box_team2Name);
			TextView score1 = (TextView) findViewById(R.id.box_team1Score);
			TextView score2 = (TextView) findViewById(R.id.box_team2Score);
			TextView turns1 = (TextView) findViewById(R.id.box_team1Turns);
			TextView turns2 = (TextView) findViewById(R.id.box_team2Turns);
			
			name1.setText(team1Name);
			name2.setText(team2Name);
			score1.setText(String.valueOf(team1Score));
			score2.setText(String.valueOf(team2Score));
			turns1.setText(String.valueOf(team1Turns));
			turns2.setText(String.valueOf(team2Turns));
			
		} else {
			//Don't set text -- something went wrong on the screen before
			Toast.makeText(getApplicationContext(), getString(R.string.scoreLoadError), Toast.LENGTH_SHORT).show();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_summary, menu);
		return true;
	}
	
	public void mainMenu(View view) {
		//Take user to main menu
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	public void startNewGame(View view) {
		//Take user to new game screen
		Intent i = new Intent(this, NewGame.class);
		startActivity(i);
	}

}
