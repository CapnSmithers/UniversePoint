package edu.mines.msmith1.universepoint;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
import edu.mines.msmith1.universepoint.dto.BaseDTOArrayAdapter;
import edu.mines.msmith1.universepoint.dto.Team;

public class TeamSelectDialog extends DialogFragment {
	private static final String EXTRA_DEFAULT_TEAM_2_ID = "edu.mines.msmith1.universepoint";
	private static final int DEFAULT_TEAM_2_ID = 1; // default team is created when the app is first started
	
	// Team Id to pass back to MainActivity
	private BaseDTOArrayAdapter mTeamAdapter;
	private TeamDAO mTeamDAO;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.teamSelect);
		
		mTeamDAO = new TeamDAO(getActivity());
		mTeamDAO.open();
		List<BaseDTO> teams = new AllTeamsAsyncTask().doInBackground(mTeamDAO);
		
		mTeamAdapter = new BaseDTOArrayAdapter(getActivity(), R.layout.list_row, teams);
		
		builder.setAdapter(mTeamAdapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				beginGameRunnerActivity((Team) mTeamAdapter.getItem(which));
				
			}
		});
		
		return builder.create();
	}
	
	private void beginGameRunnerActivity(Team team) {
		Intent intent = new Intent(getActivity(), GameRunner.class);
		intent.putExtra(EditTeams.EXTRA_TEAM_ID, team.getId());
		intent.putExtra(EXTRA_DEFAULT_TEAM_2_ID, DEFAULT_TEAM_2_ID);
		startActivity(intent);
	}
	
}
