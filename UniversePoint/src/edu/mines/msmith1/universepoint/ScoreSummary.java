/**
 * Description: This class provides the functionality behind the score summary 
 *   screen.  It accepts variables from the NewGame class via the intent.putExtra()
 *   function, and sets the variables to the appropriate displays. Provides navigation
 *   to the main menu or the new game (score-keeping) screens. 
 * 
 * @author Matt
 */
package edu.mines.msmith1.universepoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class ScoreSummary extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_summary);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_summary, menu);
		return true;
	}
	
	/**
	 * This method creates an intent and takes the user to the main menu
	 * 
	 * @param view - Main Menu button
	 */
	public void mainMenu(View view) {
		//Take user to main menu
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	/**
	 * This method creates an intent and takes the user to the scorekeeping 
	 * screen.
	 * 
	 * @param view - New Game button
	 */
	public void startNewGame(View view) {
		//Take user to new game screen
		Intent i = new Intent(this, GameRunner.class);
		startActivity(i);
	}

}
