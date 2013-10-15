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

import edu.mines.msmith1.universepoint.dto.Player;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayerStatsFragment extends Fragment {

	private OffensiveStatListener mCallback;
	private Player player;
	
	public interface OffensiveStatListener {
		public void offensiveStatAdded(Player player, View button);
		public void offensiveStatRemoved(Player player, View button);
	}
	
	//Enforce interface implementation
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		 try {
	            mCallback = (OffensiveStatListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnHeadlineSelectedListener");
	        }
	}
	
	@Override 
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        return inflater.inflate(R.layout.player_stats_layout, container, false);
	}
	
	//Add listeners for buttons
	public void addStat(View view) {
		mCallback.offensiveStatAdded(player, view);
	}
	
	public void removeStat(View view) {
		mCallback.offensiveStatRemoved(player, view);
	}

}
