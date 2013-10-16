package edu.mines.msmith1.universepoint;

import edu.mines.msmith1.universepoint.dto.OffensiveStat;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * {@link Activity} to display {@link OffensiveStat}s for given 
 * {@link Player}.  Displays lifetime stats.
 * 
 * @author Matt Smith, Van Rice
 *
 */
public class PlayerView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_view);
		
		Bundle extras = getIntent().getExtras();
		
		TextView playerNameTextView = (TextView) findViewById(R.id.playerName);
		playerNameTextView.setText(extras.getString(EditPlayers.EXTRA_PLAYER_NAME));
		
		TextView scoresTextView = (TextView) findViewById(R.id.scoresValue);
		scoresTextView.setText(String.valueOf(extras.getInt(EditPlayers.EXTRA_PLAYER_POINTS)));
		
		TextView assistsTextView = (TextView) findViewById(R.id.assistsValue);
		assistsTextView.setText(String.valueOf(extras.getInt(EditPlayers.EXTRA_PLAYER_ASSISTS)));
		
		TextView turnoverTextView = (TextView) findViewById(R.id.turnsValue);
		turnoverTextView.setText(String.valueOf(extras.getInt(EditPlayers.EXTRA_PLAYER_TURNOVERS)));
	}
}
