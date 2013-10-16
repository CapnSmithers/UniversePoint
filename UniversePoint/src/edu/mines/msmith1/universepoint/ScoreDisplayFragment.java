package edu.mines.msmith1.universepoint;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description: Persistent {@link Fragment} to display the score 
 * of current game in {@link GameRunner}. 
 * 
 * @author Matt Smith, Van Rice
 *
 */
public class ScoreDisplayFragment extends Fragment {
	
	@Override 
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        return inflater.inflate(R.layout.score_layout, container, false);

	}
}
