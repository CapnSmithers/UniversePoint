package edu.mines.msmith1.universepoint;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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

}
