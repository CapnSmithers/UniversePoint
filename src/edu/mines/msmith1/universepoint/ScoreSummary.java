package edu.mines.msmith1.universepoint;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

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
			
		} else {
			//Don't set text -- something went wrong on the screen before
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_summary, menu);
		return true;
	}

}
