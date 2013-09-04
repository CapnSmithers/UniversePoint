package edu.mines.msmith1.universepoint;

import edu.mines.msmith1.uinversepoint.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Called when New Game button is pressed on main menu screen
	 * 
	 * @param view - View active when button is clicked
	 */
	public void createNewGame(View view) {
		Log.d("NEWGAME","New app created");
		
		//Create new game dialog screen
		Intent intent = new Intent(this, NewGame.class);
		startActivity(intent);
	}
	
	/**
	 * Called when Exit button is pressed from main menu screen
	 * 
	 * @param view - View that was called when button is clicked
	 */
	public void exitApp(View view) {
		Log.d("EXIT", "App exiting");
	}
}
