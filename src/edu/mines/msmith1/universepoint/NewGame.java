package edu.mines.msmith1.universepoint;

import edu.mines.msmith1.uinversepoint.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class NewGame extends Activity {

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
		
	}
	
	public void addPoint(View view) {
		
	}

	public void removePoint(View view) {
		
	}
	
	public void finishGame(View view) {
		//Take user to score summary screen
	}
}

