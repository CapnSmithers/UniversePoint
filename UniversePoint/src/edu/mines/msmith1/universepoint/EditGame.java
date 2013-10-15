package edu.mines.msmith1.universepoint;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import edu.mines.msmith1.universepoint.dao.GameDAO;
import edu.mines.msmith1.universepoint.dao.OffensiveStatDAO;
import edu.mines.msmith1.universepoint.dto.BaseDTO;
import edu.mines.msmith1.universepoint.dto.BaseDTOArrayAdapter;
import edu.mines.msmith1.universepoint.dto.Game;
import edu.mines.msmith1.universepoint.dto.OffensiveStat;

public class EditGame extends ListActivity {
	private BaseDTOArrayAdapter mOffensiveStatsAdapter;
	private OffensiveStatDAO mOffensiveStatDAO;
	private Game mGame;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list);
		
		mOffensiveStatDAO = new OffensiveStatDAO(this);
		
		// update Textview message to be accurate
		TextView textView = (TextView) findViewById(android.R.id.empty);
		textView.setText(R.string.noOffensiveStats);
		
		Intent intent = getIntent();
		long gameId = intent.getLongExtra(EditGames.EXTRA_GAME_ID, 0);
		GameDAO gameDAO = new GameDAO(this);
		gameDAO.open();
		mGame = gameDAO.getGameById(gameId);
		gameDAO.close();
		
		ListView listView = getListView();
		// create long click listener to prompt user to delete the offensive stat
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				promptUserToDeleteOffensiveStat(mOffensiveStatsAdapter.getItem(position));
				return true;
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mOffensiveStatDAO.open();
		
		List<BaseDTO> offensiveStats = new AllOffensiveStatsTask().doInBackground(mOffensiveStatDAO);
		mOffensiveStatsAdapter = new BaseDTOArrayAdapter(this, R.layout.list_row, offensiveStats);
		setListAdapter(mOffensiveStatsAdapter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		mOffensiveStatDAO.close();
	}

	/**
	 * Prompts the user to delete the selected offensive stat
	 * @param offensiveStat
	 */
	private void promptUserToDeleteOffensiveStat(final BaseDTO offensiveStat) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.removeOffensiveStat);
		
		TextView textView = new TextView(this);
		textView.setText(R.string.removeOffensiveStatMsg);
		builder.setView(textView);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeOffensiveStat(offensiveStat);
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
	 * Deletes the offensive stat
	 * @param offensiveStat
	 */
	private void removeOffensiveStat(BaseDTO offensiveStat) {
		mOffensiveStatsAdapter.remove(offensiveStat);
		mOffensiveStatDAO.deleteOffensiveStat((OffensiveStat) offensiveStat);
	}
	
	/**
	 * Queries for all offensive stats asynchronously
	 */
	private class AllOffensiveStatsTask extends AsyncTask<OffensiveStatDAO, Object, List<BaseDTO>> {
		@Override
		protected List<BaseDTO> doInBackground(OffensiveStatDAO... params) {
			OffensiveStatDAO offensiveStatDAO = params[0];
			return offensiveStatDAO.getAllOffensiveStatForGame(mGame);
		}
	}
}
