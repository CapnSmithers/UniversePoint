/**
 * Description: Fragment to display a list of players on Team 1 from the GameRunner screen.
 * Allows for selection of player to take user to player stat screen.  Part of the Dynamic UI
 * on the GameRunner screen.
 * 
 * @author Matt Smith, Van Rice
 */
package edu.mines.msmith1.universepoint;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
