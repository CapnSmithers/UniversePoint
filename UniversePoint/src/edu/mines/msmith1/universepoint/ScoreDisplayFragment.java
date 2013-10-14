/**
 * Description: Persistent fragment to display the score 
 * of current game in GamerRunner. 
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

public class ScoreDisplayFragment extends Fragment {
	
	@Override 
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        return inflater.inflate(R.layout.score_layout, container, false);

	}
	
	public void updateScore() {
		// Increment or decrement score accordingly
	}
	
}
