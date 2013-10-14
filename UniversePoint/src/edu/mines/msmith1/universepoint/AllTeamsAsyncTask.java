package edu.mines.msmith1.universepoint;

import java.util.List;

import android.os.AsyncTask;
import edu.mines.msmith1.universepoint.dao.TeamDAO;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
	
/**
 * Retrieves all teams asynchronously
 * @author vanxrice
 */
public class AllTeamsAsyncTask extends AsyncTask<TeamDAO, Object, List<BaseDTO>> {
	@Override
	protected List<BaseDTO> doInBackground(TeamDAO... params) {
		// don't expect more than one DAO object
		TeamDAO teamDAO = params[0];
		return teamDAO.getTeams();
	}
}