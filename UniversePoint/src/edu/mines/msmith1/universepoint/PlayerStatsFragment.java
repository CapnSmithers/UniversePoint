/**
 * Description: Dynamic fragment to display individual player stats.
 * Loaded onto the GameRunner screen when a player is selected from 
 * PlayerListFragment. Pressing the back button will take the user
 * back to the player list.
 * 
 * @author Matt Smith, Van Rice
 *
 */
package edu.mines.msmith1.universepoint;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayerStatsFragment extends Fragment {

	@Override 
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        return inflater.inflate(R.layout.player_stats_layout, container, false);

	}
	

}
