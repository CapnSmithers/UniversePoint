/**
 * Description: This class is the functionality behind the score-keeping screen.
 *   Displays two fragments - one persistent fragment that shows the score for both teams,
 *   and one dynamic fragment that switches between a list of players on team 1 and a screen
 *   that displays current statistics on a selected player stored in the database.  This 
 *   screen allows entry of stats and updates the score screen accordingly.  
 * 
 * @author Matt Smith, Van Rice
 */

package edu.mines.msmith1.universepoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.mines.msmith1.universepoint.dao.GameDAO;
import edu.mines.msmith1.universepoint.dao.OffensiveStatDAO;
import edu.mines.msmith1.universepoint.dao.PlayerDAO;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
import edu.mines.msmith1.universepoint.dto.BaseDTOArrayAdapter;
import edu.mines.msmith1.universepoint.dto.Game;
import edu.mines.msmith1.universepoint.dto.OffensiveStat;
import edu.mines.msmith1.universepoint.dto.Player;
import edu.mines.msmith1.universepoint.dto.Team;

public class GameRunner extends Activity implements PlayerListFragment.ListItemSelectedListener, 
 								PlayerStatsFragment.OffensiveStatListener {

	public final static int FINISH_GAME_ID = 1;
	
	private Game mGame; // persists immediately if the teams are not null
	private Team mTeam1, mTeam2;
	private List<OffensiveStat> mOffensiveStats; // onPause persist
	private PlayerDAO mPlayerDAO;
	private BaseDTOArrayAdapter mPlayerAdapter;
	FragmentManager fragManager;
	private HashMap<Player, HashMap<String, Integer>> playerStats;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scorekeeping);
		
		mPlayerDAO = new PlayerDAO(this);
		TeamDAO teamDAO = new TeamDAO(this);
		teamDAO.open();
		
		// Team 1
		Intent intent = getIntent();
		Long teamId = intent.getLongExtra(MainActivity.EXTRA_TEAM_1_ID, 0);
		mTeam1 = teamDAO.getTeamById(teamId);
		
		// Team 2
		Long team2Id = intent.getLongExtra(MainActivity.EXTRA_TEAM_2_ID, 0);
		mTeam2 = teamDAO.getTeamById(team2Id);
		
		// if either team is null, show toast and finish
		if (mTeam1 == null || mTeam2 == null) {
			Toast toast = Toast.makeText(this, R.string.gameTeamError, Toast.LENGTH_LONG);
			toast.show();
			finish();
		} else {
			Game game = new Game();
			game.setTeam1(mTeam1);
			game.setTeam2(mTeam2);
			
			GameDAO gameDAO = new GameDAO(this);
			gameDAO.open();
			mGame = gameDAO.createGame(game);
			gameDAO.close();
			
			mOffensiveStats = new ArrayList<OffensiveStat>();
			
			TextView team1TV = (TextView) findViewById(R.id.team1Name);
			team1TV.setText(mTeam1.toString());
			
			TextView team2TV = (TextView) findViewById(R.id.team2Name);
			team2TV.setText(mTeam2.toString());
		}
		
		//Inflate player list fragment
		if(findViewById(R.id.listContainer) != null) {
			if (savedInstanceState != null) {
				return;
			}
			
			fragManager = getFragmentManager();
			
			mPlayerDAO.open();
			List<BaseDTO> players = new AllPlayersAsyncTask().doInBackground(mPlayerDAO);
			
			//Initialize player stats map here
			playerStats = new HashMap<Player, HashMap<String, Integer>>();
			for(int i = 0; i < players.size(); i++) {
				//Initialize stats map
				HashMap<String, Integer> statsMap = new HashMap<String, Integer>();
				statsMap.put("Scores", 0);
				statsMap.put("Assists", 0);
				statsMap.put("Turns", 0);
				
				playerStats.put((Player) players.get(i), statsMap);
			}
			
			mPlayerAdapter = new BaseDTOArrayAdapter(this, R.layout.list_row, players);
			
			PlayerListFragment playerList = new PlayerListFragment();
			playerList.setListAdapter(mPlayerAdapter);
			
			fragManager.beginTransaction()
				.add(R.id.listContainer, (Fragment) playerList).commit();
		}
	}		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, FINISH_GAME_ID, 0, R.string.finishGame);
		return result;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case FINISH_GAME_ID:
				finishGame();
				return true;
			default:
				return false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d("EXITING APP", "Universe Point is destroyed");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("ON_PAUSE", "onPause called");

	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * Method to take the user to the summary screen.  Accessed by pressing
	 * the finish game button in the settings menu.
	 */
	private void finishGame() {
		OffensiveStatDAO offensiveStatDAO = new OffensiveStatDAO(this);
		offensiveStatDAO.open();
		offensiveStatDAO.createOffensiveStats(mOffensiveStats);
		offensiveStatDAO.close();
		
		finish();
	}
	
	// Swaps fragments based on player selected from playerList fragment
	@Override
	public void listItemSelected(int position) {
		Player player = (Player) mPlayerAdapter.getItem(position);
		
		fragManager = getFragmentManager();
		
		Fragment playerView = new PlayerStatsFragment();
		// Function to populate playerView here
		
		FragmentTransaction transaction = fragManager.beginTransaction();
		transaction.replace(R.id.listContainer, playerView);
		transaction.addToBackStack(null);
		transaction.commit();
		
	}
	
	//Add button hit on PlayerStatsFragment
	@Override
	public void offensiveStatAdded(Player player, View button) {
		// Add value to 'playerStats' map here, update score display
		switch(button.getId()) {
			case R.id.addPoint:
				Integer scores = playerStats.get(player).get("Scores");
				scores += 1;
				playerStats.get(player).put("Scores", scores);
			case R.id.addAssist:
				Integer assists = playerStats.get(player).get("Assists");
				assists += 1;
				playerStats.get(player).put("Assists", assists);
			case R.id.addTurn:
				Integer turns = playerStats.get(player).get("Turns");
				turns += 1;
				playerStats.get(player).put("Turns", turns);
			default:
				Toast toast = Toast.makeText(this, R.string.uhoh, Toast.LENGTH_SHORT);
				toast.show();
		}
	}

	//Remove button hit on PlayerStatsFragment
	@Override
	public void offensiveStatRemoved(Player player, View button) {
		// Remove value from 'playerStats' map here, update score display
		switch(button.getId()) {
			case R.id.removePoint:
				Integer scores = playerStats.get(player).get("Scores");
				scores -= 1;
				playerStats.get(player).put("Scores", scores);
			case R.id.removeAssist:
				Integer assists = playerStats.get(player).get("Assists");
				assists -= 1;
				playerStats.get(player).put("Assists", assists);
			case R.id.removeTurn:
				Integer turns = playerStats.get(player).get("Turns");
				turns -= 1;
				playerStats.get(player).put("Turns", turns);
			default:
				Toast toast = Toast.makeText(this, R.string.uhoh, Toast.LENGTH_SHORT);
				toast.show();
		}
	}
	
	/**
	 * Retrieves all players asynchronously
	 * @param params expects {@link PlayerDAO} as first param
	 */
	private class AllPlayersAsyncTask extends AsyncTask<PlayerDAO, Object, List<BaseDTO>> {
		@Override
		protected List<BaseDTO> doInBackground(PlayerDAO... params) {
			PlayerDAO playerDAO = params[0];
			return playerDAO.getPlayersByTeam(mTeam1);
		}
	}
	
}

