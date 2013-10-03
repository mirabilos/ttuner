package com.thefourthspecies.ttuner;

import com.thefourthspecies.ttuner.model.listables.CentsTemperament;
import com.thefourthspecies.ttuner.model.listables.Temperament;
import com.thefourthspecies.ttuner.model.states.TunerState;

import com.thefourthspecies.ttuner.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * An intermediate Activity that determines, based on the type of the current Temperament,
 * which Temperament editor to go to. If given a flag of -1, though it recognizes a new
 * Temperament and provides a simple two-button interface to choose between them.
 * @author graham
 *
 */
public class NewTemperament extends Activity {
		
	int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		Intent intent = getIntent();
		position = intent.getIntExtra(ListableSelector.EXTRA_POSITION, -1);
		
		
		if (position != -1) {
			Temperament temp = TunerState.getInstance().getTemperament();
			if (temp instanceof CentsTemperament) {
				byCents(null);
			} else {
				byRelationships(null);
			}
		}
		
		
		setContentView(R.layout.activity_new_temperament);
		// Show the Up button in the action bar.
		setupActionBar();
		
	}

	/**
	 * Called on press of By Cents button
	 * Starts Cents Editor activity, sending the appropriate flag/position
	 */
	public void byCents(View v) {
		Intent intent = new Intent(this, CentsEditor.class);
		intent.putExtra(ListableSelector.EXTRA_POSITION, position);
		startActivity(intent);
	}
	
	
	/**
	 * Called on press of By Relationships button
	 * Starts Relationships Editor activity, sending the appropriate flag/position
	 */
	public void byRelationships(View v) {
		Intent intent = new Intent(this, RelationshipsEditor.class);
		intent.putExtra(ListableSelector.EXTRA_POSITION, position);
		startActivity(intent);		
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_temperament, menu);
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.help:
			Intent intent = new Intent(this, HelpMain.class);
			intent.putExtra(HelpMain.ITEM, HelpMain.TEMPERAMENTS);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	protected void onPause() {
		super.onPause();

		// To ensure that a back button doesn't return here.
		finish();
	}

}
