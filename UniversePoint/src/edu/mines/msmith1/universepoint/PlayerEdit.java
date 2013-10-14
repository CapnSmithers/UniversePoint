package edu.mines.msmith1.universepoint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PlayerEdit extends Activity {
	
//	private int idDictionary[] = { R.id.addScore,
//									R.id.addAssist,
//									R.id.addTurn,
//									R.id.removeScore,
//									R.id.removeAssist,
//									R.id.removeTurn };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_edit);
	}
	
	//Function to remove player stats
	public void addStat(View addButton) {
		switch(addButton.getId()) {
			case (R.id.addScore):
				//Increment scores, update score display fragment
				break;
			case (R.id.addAssist):
				//Increment assist
				break;
			case (R.id.addTurn):
				//Increment turn
				break;
			default:
				Toast toast = Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
				toast.show();
		}		
	}
	
	public void removeStat(View removeButton) {
		switch(removeButton.getId()) {
			case (R.id.removeScore):
				//Decrement scores, update score display fragment
				break;
			case (R.id.removeAssist):
				//Decrement assist
				break;
			case (R.id.removeTurn):
				//Decrement turn
				break;
			default:
				Toast toast = Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
				toast.show();
		}		
	}
}
