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

/**
 *  This class is the main activity behind the scorekeeping mechanic
 *  of the app.  Displays a {@link ScoreDisplayFragment} to show the current
 *  game score, and swaps between {@link PlayerListFragment} and {@link PlayerStatsFragment}  
 *  to add {@link OffensiveStat} objects to each player and game.  Selecting Finish game from
 *  the options menu will save the {@link Game} to the database and take the user to the main 
 *  menu.
 * 
 * @author Matt Smith, Van Rice
 */

public class GameRunner extends Activity implements PlayerListFragment.ListItemSelectedListener, 
 								PlayerStatsFragment.OffensiveStatListener {

	public final static int FINISH_GAME_ID = 1;
	
	private Game mGame; // persists immediately if the teams are not null
	private Team mTeam1, mTeam2;
	private List<OffensiveStat> mOffensiveStats; // onPause persist
	private PlayerDAO mPlayerDAO;
	private OffensiveStatDAO mOffensiveStatDAO;
	private BaseDTOArrayAdapter mPlayerAdapter;
	private PlayerStatsFragment mPlayerStatsFragment;
	
	private int team1AnonymousScore, team2AnonymousScore = 0;
	
	FragmentManager fragManager;
	private HashMap<Player, HashMap<String, Integer>> playerStats;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scorekeeping);
		
		mPlayerDAO = new PlayerDAO(this);
		mOffensiveStatDAO = new OffensiveStatDAO(this);
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
				finish();
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

		mOffensiveStatDAO.close();
		mPlayerDAO.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		mOffensiveStatDAO.open();
		mPlayerDAO.open();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finishGame();
	}
	
	/**
	 * Method to take the user to the summary screen.  Accessed by pressing
	 * the finish game button in the settings menu.
	 */
	private void finishGame() {
		convertMapToOffensiveStats();
		convertAnonymousScoresToOffensiveStats();
		
		OffensiveStatDAO offensiveStatDAO = new OffensiveStatDAO(this);
		offensiveStatDAO.open();
		offensiveStatDAO.createOffensiveStats(mOffensiveStats);
		offensiveStatDAO.close();
	}
	
	/**
	 * Converts our int values into database objects without a player association
	 */
	private void convertAnonymousScoresToOffensiveStats() {
		for (int i = 0; i < team1AnonymousScore; i++) {
			createOffensiveStatForTeam1(null, false, false, false);
		}
		for (int i = 0; i < team2AnonymousScore; i++) {
			OffensiveStat os = new OffensiveStat();
			os.setTeam(mTeam2);
			os.setGame(mGame);
			mOffensiveStats.add(os);
		}
	}
	
	/**
	 * Converts our temporary map into database objects
	 */
	private void convertMapToOffensiveStats() {
		for (Player player : playerStats.keySet()) {
			Map<String, Integer> stats = playerStats.get(player);
			if (stats.get("Scores").compareTo(0) > 0) {
				for (int i = 0; stats.get("Scores").compareTo(i) > 0; i++) {
					createOffensiveStatForTeam1(player, true, false, false);
				}
			}
			if (stats.get("Assists").compareTo(0) > 0) {
				for (int i = 0; stats.get("Assists").compareTo(i) > 0; i++) {
					createOffensiveStatForTeam1(player, false, true, false);
				}
			}
			if (stats.get("Turns").compareTo(0) > 0) {
				for (int i = 0; stats.get("Turns").compareTo(i) > 0; i++) {
					createOffensiveStatForTeam1(player, false, false, true);
				}
			}
		}
	}
	
	/**
	 * Creates {@link OffensiveStat} database objects for team 1
	 * @param player
	 * @param point
	 * @param assist
	 * @param turnover
	 */
	private void createOffensiveStatForTeam1(Player player, boolean point, boolean assist, boolean turnover) {
		OffensiveStat stat = new OffensiveStat();
		stat.setGame(mGame);
		stat.setTeam(mTeam1);
		if (point)
			stat.setPlayer(player);
		if (assist)
			stat.setAssistingPlayer(player);
		if (turnover)
			stat.setTurnoverPlayer(player);
		mOffensiveStats.add(stat);
	}
	
	// Swaps fragments based on player selected from playerList fragment
	@Override
	public void listItemSelected(int position) {
		Player player = (Player) mPlayerAdapter.getItem(position);
		
		fragManager = getFragmentManager();
		
		mPlayerStatsFragment = new PlayerStatsFragment();
		mPlayerStatsFragment.setPlayer(player);

		FragmentTransaction transaction = fragManager.beginTransaction();
		transaction.replace(R.id.listContainer, mPlayerStatsFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	//Add button hit on PlayerStatsFragment
	@Override
	public void offensiveStatAdded(View button) {
		Player player = mPlayerStatsFragment.getPlayer();
		// Add value to 'playerStats' map here, update score display
		switch(button.getId()) {
			case R.id.addPoint:
				Integer scores = playerStats.get(player).get("Scores");
				scores += 1;
				playerStats.get(player).put("Scores", scores);
				break;
			case R.id.addAssist:
				Integer assists = playerStats.get(player).get("Assists");
				assists += 1;
				playerStats.get(player).put("Assists", assists);
				break;
			case R.id.addTurn:
				Integer turns = playerStats.get(player).get("Turns");
				turns += 1;
				playerStats.get(player).put("Turns", turns);
				break;
			default:
				Toast toast = Toast.makeText(this, R.string.uhoh, Toast.LENGTH_SHORT);
				toast.show();
		}
		updatePlayerStatsFragment();
		updateScoreDisplayFragment();
	}

	//Remove button hit on PlayerStatsFragment
	@Override
	public void offensiveStatRemoved(View button) {
		Player player = mPlayerStatsFragment.getPlayer();
		// Remove value from 'playerStats' map here, update score display
		switch(button.getId()) {
			case R.id.removePoint:
				Integer scores = playerStats.get(player).get("Scores");
				scores -= 1;
				playerStats.get(player).put("Scores", scores);
				break;
			case R.id.removeAssist:
				Integer assists = playerStats.get(player).get("Assists");
				assists -= 1;
				playerStats.get(player).put("Assists", assists);
				break;
			case R.id.removeTurn:
				Integer turns = playerStats.get(player).get("Turns");
				turns -= 1;
				playerStats.get(player).put("Turns", turns);
				break;
			default:
				Toast toast = Toast.makeText(this, R.string.uhoh, Toast.LENGTH_SHORT);
				toast.show();
		}
		updatePlayerStatsFragment();
		updateScoreDisplayFragment();
	}
	
	/**
	 * Adds a point to the appropriate team
	 * @param button
	 */
	public void teamPointAdded(View button) {
		switch(button.getId()) {
			case R.id.team1AddScore:
				team1AnonymousScore++;
				break;
			case R.id.team2AddScore:
				team2AnonymousScore++;
				break;
			default:
				Toast toast = Toast.makeText(this, R.string.uhoh, Toast.LENGTH_SHORT);
				toast.show();
		}
		updateScoreDisplayFragment();
	}
	
	/**
	 * Remvoes a point from the appropriate team, does not allow the counter to go below 0
	 * @param button
	 */
	public void teamPointRemoved(View button) {
		switch(button.getId()) {
			case R.id.team1RemoveScore:
				team1AnonymousScore--;
				if (team1AnonymousScore < 0)
					team1AnonymousScore = 0;
				break;
			case R.id.team2RemoveScore:
				team2AnonymousScore--;
				if (team2AnonymousScore < 0)
					team2AnonymousScore = 0;
				break;
			default:
				Toast toast = Toast.makeText(this, R.string.uhoh, Toast.LENGTH_SHORT);
				toast.show();
		}
		updateScoreDisplayFragment();
	}
	
	/**
	 * Refreshes the player stats fragment counters
	 */
	public void updatePlayerStatsFragment() {
		Player player = mPlayerStatsFragment.getPlayer();
		
		TextView pointCountTV = (TextView) findViewById(R.id.pointCount);
		pointCountTV.setText(playerStats.get(player).get("Scores").toString());
		
		TextView assistCountTV = (TextView) findViewById(R.id.assistCount);
		assistCountTV.setText(playerStats.get(player).get("Assists").toString());
		
		TextView turnoverCountTV = (TextView) findViewById(R.id.turnCount);
		turnoverCountTV.setText(playerStats.get(player).get("Turns").toString());
	}
	
	/**
	 * Refreshes the score display
	 */
	public void updateScoreDisplayFragment() {
		int scoreTeam1 = 0;
		for (Player player : playerStats.keySet()) {
			Map<String, Integer> stats = playerStats.get(player);
			scoreTeam1 += stats.get("Scores");
		}
		scoreTeam1 += team1AnonymousScore;
		TextView team1ScoreTV = (TextView) findViewById(R.id.team1Score);
		team1ScoreTV.setText(String.valueOf(scoreTeam1));
		
		TextView team2ScoreTV = (TextView) findViewById(R.id.team2Score);
		team2ScoreTV.setText(String.valueOf(team2AnonymousScore));
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

