package edu.mines.msmith1.universepoint;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScoreDisplayFragment extends Fragment {
	//TODO: Put some good stuffs in here
	@Override 
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        return inflater.inflate(R.layout.score_layout, container, false);

	}
	
}
