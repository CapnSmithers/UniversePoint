package edu.mines.msmith1.universepoint;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
import edu.mines.msmith1.universepoint.dto.BaseDTOArrayAdapter;

/**
 * Main menu for application. Provides navigation to {@link EditTeams} and {@link EditGames}
 * as well as {@link GameRunner} via new game button.  
 *   
 * Usage: Used Nexus 4 and HTC One phones to test.  Target API is 18,
 * minimum API is 11.  
 * 
 * Documentation Statement: According to the Academic Honesty Policy outlined in the 
 *   course syllabus, we have been the sole authors and debuggers of this piece of 
 *   software. The only resources that I have used are developer.android.com, class 
 *   notes, and internet resources such as stackoverflow.com for troubleshooting purposes.
 *   No code has been copied or stolen.
 *   
 * Work/Grade Breakdown: 50/50 split for work and point distribution
 * 
 * @author Matthew Smith, Vander Rice
 */
public class MainActivity extends Activity {
	public static final String EXTRA_TEAM_1_ID = "edu.mines.msmith1.universepoint.TEAM_1_ID";
	public static final String EXTRA_TEAM_2_ID = "edu.mines.msmith1.universepoint.TEAM_2_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Called when New Game button is pressed on main menu screen.
	 * Opens dialog to select the user's team. Opens second dialog to select
	 * the opposing team. Selecting team takes user to the {@link GameRunner}.
	 * 
	 * @param view - new game button
	 */
	public void createNewGame(View view) {
		Log.d("NEWGAME","New game created");
		
		final TeamDAO teamDAO = new TeamDAO(this);
		teamDAO.open();
		final List<BaseDTO> teams = new AllTeamsAsyncTask().doInBackground(teamDAO);
		
		final BaseDTOArrayAdapter adapter = new BaseDTOArrayAdapter(this, R.layout.list_row, teams);
		
		// Create Team 1 Dialog
		final AlertDialog.Builder team2Builder = new AlertDialog.Builder(this);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.team1Select);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				teamDAO.close();
				
				final Long team1Id = adapter.getItem(which).getId();
				// remove team1 from the team2 dialog options
				adapter.remove(adapter.getItem(which));
				
				// Create Team 2 Dialog
				team2Builder.setTitle(R.string.team2Select);
				team2Builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Long team2Id = adapter.getItem(which).getId();
						beginGameRunnerActivity(team1Id, team2Id);
					}
				});
				team2Builder.show();
			}
		});
		builder.show();
	}
	
	/**
	 * Starts {@link GameRunner}, places the team._id as extras.
	 * @param team1
	 * @param team2
	 */
	private void beginGameRunnerActivity(Long team1, Long team2) {
		Intent intent = new Intent(this, GameRunner.class);
		intent.putExtra(EXTRA_TEAM_1_ID, team1);
		intent.putExtra(EXTRA_TEAM_2_ID, team2);
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
	 * @param view - edit/view games button
	 */
	public void viewGames(View view) {
		Intent intent = new Intent(this, EditGames.class);
		startActivity(intent);
	}

}
