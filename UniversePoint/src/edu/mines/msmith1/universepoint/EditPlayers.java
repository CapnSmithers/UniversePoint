package edu.mines.msmith1.universepoint;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.mines.msmith1.universepoint.dao.OffensiveStatDAO;
import edu.mines.msmith1.universepoint.dao.PlayerDAO;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
import edu.mines.msmith1.universepoint.dto.BaseDTOArrayAdapter;
import edu.mines.msmith1.universepoint.dto.Player;
import edu.mines.msmith1.universepoint.dto.Team;

/**
 * {@link ListView} for {@link Player} objects in a {@link Team}. Clicking on
 * a team in the team list will take you to this screen, which displays all 
 * players on that team, allowing for adding or removing players. 
 * 
 * @author Matt Smith, Van Rice
 *
 */
public class EditPlayers extends ListActivity {
	public static final int ADD_PLAYER_ID = 1;
	public static final String EXTRA_PLAYER_POINTS = "edu.mines.msmith1.universepoint.PLAYER_POINTS";
	public static final String EXTRA_PLAYER_ASSISTS = "edu.mines.msmith1.universepoint.PLAYER_ASSISTS";
	public static final String EXTRA_PLAYER_TURNOVERS = "edu.mines.msmith1.universepoint.PLAYER_TURNOVERS";
	public static final String EXTRA_PLAYER_NAME = "edu.mines.msmith1.universepoint.PLAYER_NAME";
	
	private Team mTeam;
	private BaseDTOArrayAdapter mPlayerAdapter;
	private PlayerDAO mPlayerDAO;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list);
		
		// update TextView message to be accurate
		TextView textView = (TextView) findViewById(android.R.id.empty);
		textView.setText(R.string.noPlayers);
		
		// fetch intent, get team._id, query for team and save locally
		Intent intent = getIntent();
		Long teamId = intent.getLongExtra(EditTeams.EXTRA_TEAM_ID, 0);
		TeamDAO teamDAO = new TeamDAO(this);
		mTeam = new TeamAsyncTask().doInBackground(teamDAO, teamId);
		
		mPlayerDAO = new PlayerDAO(this);
		
		ListView listView = getListView();
		// create long view listener to prompt user to delete player
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				promptUserToDeletePlayer((Player) mPlayerAdapter.getItem(position));
				return true;
			}
		});
		
		// create click listener to prompt user to launch edit player intent
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				beginPlayerViewActivity((Player) mPlayerAdapter.getItem(position));
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_PLAYER_ID, 0, R.string.addNewPlayer);
		return result;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case ADD_PLAYER_ID:
				promptUserForNewPlayer();
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mPlayerDAO.open();
		List<BaseDTO> players = new AllPlayersAsyncTask().doInBackground(mPlayerDAO);
		
		mPlayerAdapter = new BaseDTOArrayAdapter(this, R.layout.list_row, players);
		setListAdapter(mPlayerAdapter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	
		mPlayerDAO.close();
	}
	
	/**
	 * Launches {@link PlayerView} and passes the _id to query for players
	 * @param player
	 */
	private void beginPlayerViewActivity(Player player) {
		OffensiveStatDAO offensiveStatDAO = new OffensiveStatDAO(this);
		offensiveStatDAO.open();
		Intent intent = new Intent(this, PlayerView.class);
		intent.putExtra(EXTRA_PLAYER_NAME, player.getName());
		intent.putExtra(EXTRA_PLAYER_POINTS, offensiveStatDAO.getAllPointsForPlayer(player).size());
		intent.putExtra(EXTRA_PLAYER_ASSISTS, offensiveStatDAO.getAllAssistsForPlayer(player).size());
		intent.putExtra(EXTRA_PLAYER_TURNOVERS, offensiveStatDAO.getAllTurnoversForPlayer(player).size());
		offensiveStatDAO.close();
		startActivity(intent);
	}
	
	/**
	 * Creates an alert dialog to get user input for new player
	 */
	public void promptUserForNewPlayer() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.addNewPlayer);
		
		final EditText editText = new EditText(this);
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(editText);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String input = editText.getText().toString();
				if (input != null && input.length() > 0) {
					addPlayer(input);
				} else {
					showToastForNullPlayerName();
				}
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		builder.show();
	}
	
	/**
	 * Inserts a new player into the database
	 * @param playerName
	 */
	public void addPlayer(String playerName) {
		Player player = new Player();
		player.setName(playerName);
		player.setTeam(mTeam);
		
		player = mPlayerDAO.createPlayer(player);
		mPlayerAdapter.add(player);
	}
	
	/**
	 * Displays a toast telling the user that the name cannot be empty
	 */
	public void showToastForNullPlayerName() {
		Toast toast = Toast.makeText(this, R.string.nameNotNullToastMessage, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/**
	 * Create a alert dialog to prompt user to delete player
	 * @param player
	 */
	public void promptUserToDeletePlayer(final Player player) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.removePlayer);
		
		TextView textView = new TextView(this);
		textView.setText(R.string.removePlayerMsg);
		builder.setView(textView);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removePlayer(player);
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		builder.show();
	}

	/**
	 * Deletes the player from the database and refreshes view
	 * 
	 * @param player
	 */
	public void removePlayer(Player player) {
		mPlayerAdapter.remove(player);
		mPlayerDAO.deletePlayer(player);
	}
	
	/**
	 * Retrieves all players asynchronously
	 * @param params expects {@link PlayerDAO} as first param
	 */
	private class AllPlayersAsyncTask extends AsyncTask<PlayerDAO, Object, List<BaseDTO>> {
		@Override
		protected List<BaseDTO> doInBackground(PlayerDAO... params) {
			PlayerDAO playerDAO = params[0];
			return playerDAO.getPlayersByTeam(mTeam);
		}
	}
	
	/**
	 * Queries for {@link Team} asynchronously
	 * @param params expects {@link TeamDAO} as first param & {@link Long} as second param
	 */
	private class TeamAsyncTask extends AsyncTask<Object, Object, Team> {
		@Override
		protected Team doInBackground(Object... params) {
			TeamDAO teamDAO = (TeamDAO) params[0];
			teamDAO.open();
			Team team = teamDAO.getTeamById((Long) params[1]);
			teamDAO.close();
			return team;
		}
	}
}
