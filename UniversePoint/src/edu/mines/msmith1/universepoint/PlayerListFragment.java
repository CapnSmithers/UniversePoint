package edu.mines.msmith1.universepoint;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * {@link ListView} of {@link Player}s on Team 1.  Selecting a player will
 * swap this fragment with the {@link PlayerStatsFragment}.
 * 
 * @author Matt Smith, Van Rice
 */

public class PlayerListFragment extends ListFragment {
	ListItemSelectedListener playerSelectedListener;
	
	/**
	 * Interface to allow list item selection
	 */
	public interface ListItemSelectedListener {
		public void listItemSelected(int position);
	}
	
	 @Override
	 public void onAttach(Activity activity) {
		 super.onAttach(activity);
		 
		 try {
			 playerSelectedListener = (ListItemSelectedListener) activity;
		 } catch (ClassCastException e) {
			 throw new ClassCastException(activity.toString() + " must implement ListItemSelectedListener");
		 }
	 }
	 
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
		  super.onActivityCreated(savedInstanceState);
		  
	  }
	 
	 //Send info back to GameRunner class
	 @Override
	 public void onListItemClick(ListView l, View v, int position, long id) {
		 playerSelectedListener.listItemSelected(position);
	 }
		
}
