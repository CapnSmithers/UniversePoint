/**
 * Description: Main menu for Universe Point app.  Provides 
 *   navigation to score-keeping screen. Currently only has new game and exit 
 *   buttons - more options will be implemented. 
 *   
 * Usage: Used Nexus 4 emulator and Nexus 7 tablet to test.  Target API is 18  
 * 
 * Documentation Statement: According to the Academic Honesty Policy outlined in the 
 *   course syllabus, I have been the sole author and debugger of this piece of 
 *   software. The only resources that I have used are developer.android.com and class 
 *   notes.
 * 
 * @author Matthew Smith
 */
package edu.mines.msmith1.universepoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Called when New Game button is pressed on main menu screen.
	 * Takes user to scorekeeping screen
	 * 
	 * @param view - new game button
	 */
	public void createNewGame(View view) {
		Log.d("NEWGAME","New game created");
		
		//Create new game dialog screen
		Intent intent = new Intent(this, GameRunner.class);
		startActivity(intent);
	}
	
	public void createTeam(View view) {
		Log.d("EDIT TEAMS", "Edit team button pressed");
		
		//Bring up create new team intent
		Intent intent = new Intent(this, EditTeams.class);
		startActivity(intent);
	}
	
	public void viewGames(View view) {
		Toast toast = Toast.makeText(this, R.string.noNotYet, Toast.LENGTH_SHORT);
		toast.show();
	}
}
