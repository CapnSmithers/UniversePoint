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

public class EditGames extends ListActivity {
	public static final String EXTRA_GAME_ID = "edu.mines.msmith1.universepoint.GAME_ID";
	private BaseDTOArrayAdapter mGameAdapter;
	private GameDAO mGameDAO;
	private OffensiveStatDAO mOffensiveStatDAO;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list);
		
		mGameDAO = new GameDAO(this);
		mOffensiveStatDAO = new OffensiveStatDAO(this);
		
		// update TextView message to be accurate
		TextView textView = (TextView) findViewById(android.R.id.empty);
		textView.setText(R.string.noGames);
		
		ListView listView = getListView();
		// create long click listener to prompt user to delete the game
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				promptUserToDeleteGame(mGameAdapter.getItem(position));
				return true;
			}
		});
		// create click listener to prompt user to launch edit game intent
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				beginEditGameActivity(mGameAdapter.getItem(position));
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mGameDAO.open();
		mOffensiveStatDAO.open();
		
		List<BaseDTO> games = new AllGamesAsyncTask().doInBackground(mGameDAO);
		mGameAdapter = new BaseDTOArrayAdapter(this, R.layout.list_row, games);
		setListAdapter(mGameAdapter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		mGameDAO.close();
		mOffensiveStatDAO.close();
	}
	
	/**
	 * Begins {@link EditGame}
	 * @param game
	 */
	private void beginEditGameActivity(BaseDTO game) {
		Intent intent = new Intent(this, EditGame.class);
		intent.putExtra(EXTRA_GAME_ID, game.getId());
		startActivity(intent);
	}
	
	/**
	 * Prompts the user to delete the selected game
	 * @param game
	 */
	private void promptUserToDeleteGame(final BaseDTO game) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.removeGame);
		
		TextView textView = new TextView(this);
		textView.setText(R.string.removeGameMsg);
		builder.setView(textView);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeGame(game);
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
	 * Deletes the game and corresponding {@link OffensiveStat}s.
	 * @param game
	 */
	private void removeGame(BaseDTO game) {
		mGameAdapter.remove(game);
		mOffensiveStatDAO.deleteOffensiveStatsForGame((Game) game);
		mGameDAO.deleteGame((Game) game);
	}
	
	/**
	 * Queries for all games asynchronously
	 */
	private class AllGamesAsyncTask extends AsyncTask<GameDAO, Object, List<BaseDTO>> {
		@Override
		protected List<BaseDTO> doInBackground(GameDAO... params) {
			GameDAO gameDAO = params[0];
			return gameDAO.getGames();
		}
	}
}
