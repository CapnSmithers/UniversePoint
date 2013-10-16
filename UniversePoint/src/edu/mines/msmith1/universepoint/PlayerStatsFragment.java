package edu.mines.msmith1.universepoint;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.mines.msmith1.universepoint.dto.Player;

/**
 * {@link Fragment} to display and update {@link OffensiveStat}s for each
 * {@link Player}.  Swaps with {@link PlayerListFragment} on back button.
 * 
 * @author Matt Smith, Van Rice
 *
 */
public class PlayerStatsFragment extends Fragment {
	private Player player;
	
	public interface OffensiveStatListener {
		public void offensiveStatAdded(View button);
		public void offensiveStatRemoved(View button);
	}
	
	@Override 
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        return inflater.inflate(R.layout.player_stats_layout, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		((GameRunner) getActivity()).updatePlayerStatsFragment();
	}
	
	/**
	 * Used to specify which player we're referencing
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Returns the player to update stats for
	 * @return
	 */
	public Player getPlayer() {
		return this.player;
	}

}
