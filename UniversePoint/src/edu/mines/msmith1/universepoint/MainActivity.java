/**
 * Description: Main menu for Universe Point app.  Provides 
 *   navigation to score-keeping screen, edit/view team screen,
 *   and edit/view game screen. Currently only new game and edit/view
 *   team buttons are functional. More options will be implemented in 
 *   future releases. 
 *   
 * Usage: Used Nexus 4 emulator and Nexus 7 tablet to test.  Target API is 18,
 * minimum API is 11.  
 * 
 * Documentation Statement: According to the Academic Honesty Policy outlined in the 
 *   course syllabus, we have been the sole authors and debuggers of this piece of 
 *   software. The only resources that I have used are developer.android.com, class 
 *   notes, and internet resources such as stackoverflow.com for troubleshooting purposes.
 *   No code has been copied or stolen.
 * 
 * @author Matthew Smith, Vander Rice
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
	
	
	/**
	 * Called when the Edit/View Teams button is pressed. Navigates
	 * user to the EditTeams class screen
	 * 
	 * @param view - Edit/View teams button
	 */
	public void createTeam(View view) {
		Log.d("EDIT TEAMS", "Edit team button pressed");
		
		//Bring up create new team intent
		Intent intent = new Intent(this, EditTeams.class);
		startActivity(intent);
	}
	
	/**
	 * Called when the view/edit games button is pressed. Navigates
	 * user to ViewGames class screen
	 * 
	 * TODO: implement ViewGames class
	 * 
	 * @param view - edit/view games button
	 */
	public void viewGames(View view) {
		Toast toast = Toast.makeText(this, R.string.noNotYet, Toast.LENGTH_SHORT);
		toast.show();
	}
}
