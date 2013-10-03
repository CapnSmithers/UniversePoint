/**
 * Description: This class is a list of the teams listed in the database.
 * Clicking on a team will take you to a list of all of the players on that
 * team.  The list of teams can be edited to add or remove teams by using the
 * settings menu to add a team and long-pressing a team name to remove it.  
 * 
 * @author Matt Smith, Van Rice
 *
 */
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
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
import edu.mines.msmith1.universepoint.dto.BaseDTOArrayAdapter;
import edu.mines.msmith1.universepoint.dto.Team;

public class EditTeams extends ListActivity {
	public static final int ADD_TEAM_ID = 1;
	public static final String EXTRA_TEAM_ID = "edu.mines.msmith1.unviersepoint.TEAM_ID";
	private BaseDTOArrayAdapter mTeamAdapter;
	private TeamDAO mTeamDAO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list);
		
		mTeamDAO = new TeamDAO(this);
		
		ListView listView = getListView();
		// create long click listener to prompt user to delete the team
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				promptUserToDeleteTeam(mTeamAdapter.getItem(position));
				return true;
			}
		});
		// create click listener to prompt user to launch edit player intent
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				beginEditPlayersActivity((Team) mTeamAdapter.getItem(position));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_TEAM_ID, 0, R.string.addNewTeam);
		return result;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case ADD_TEAM_ID:
				promptUserForNewTeam();
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mTeamDAO.open();
		List<BaseDTO> teams = new AllTeamsAsyncTask().doInBackground(mTeamDAO);
		
		mTeamAdapter = new BaseDTOArrayAdapter(this, R.layout.list_row, teams);
		setListAdapter(mTeamAdapter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		mTeamDAO.close();
	}
	
	/**
	 * Launches {@link EditPlayers} and passes the _id to query for players
	 * @param team
	 */
	private void beginEditPlayersActivity(Team team) {
		Intent intent = new Intent(this, EditPlayers.class);
		intent.putExtra(EXTRA_TEAM_ID, team.getId());
		startActivity(intent);
	}
	
	/**
	 * Create a {@link AlertDialog} to accept user input for team name.
	 */
	private void promptUserForNewTeam() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.addNewTeam);
		
		// setup text input
		final EditText editText = new EditText(this);
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(editText);
		
		// setup buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String input = editText.getText().toString();
				if (input != null && input.length() > 0) {
					addTeam(input);
				} else {
					showToastForNullTeamName();
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
	 * Creates a new {@link Team} and persists to the database. Refreshes view.
	 * 
	 * @param teamName - User-enetered string to display as team name
	 */
	private void addTeam(String teamName) {
		Team team = new Team();
		team.setName(teamName);
		
		team = mTeamDAO.createTeam(team);
		mTeamAdapter.add(team);
	}
	
	/**
	 * Creates a {@link Toast} to notify the user that the team name cannot be empty.
	 */
	private void showToastForNullTeamName() {
		Toast toast = Toast.makeText(this, R.string.nameNotNullToastMessage, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/**
	 * Creates a {@link AlertDialog} to prompt user to delete the selected team.
	 * 
	 * @param team - Team to be deleted, selected by long-press
	 */
	private void promptUserToDeleteTeam(final BaseDTO team) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.removeTeam);
		
		TextView textView = new TextView(this);
		textView.setText(R.string.removeTeamMsg);
		builder.setView(textView);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeTeam((Team) team);
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
	 * Deletes the team from the database and refreshes view
	 * 
	 * @param team - to be removed from database
	 */
	private void removeTeam(Team team) {
		mTeamAdapter.remove(team);
		mTeamDAO.deleteTeam(team);
	}
	
	/**
	 * Retrieves all teams asynchronously
	 */
	private class AllTeamsAsyncTask extends AsyncTask<TeamDAO, Object, List<BaseDTO>> {
		@Override
		protected List<BaseDTO> doInBackground(TeamDAO... params) {
			// don't expect more than one DAO object
			TeamDAO teamDAO = params[0];
			return teamDAO.getTeams();
		}
	}
}
