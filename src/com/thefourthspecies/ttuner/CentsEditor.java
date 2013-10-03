package com.thefourthspecies.ttuner;

import com.thefourthspecies.ttuner.R;

import java.util.List;

import com.thefourthspecies.ttuner.model.data.Note;
import com.thefourthspecies.ttuner.model.exceptions.TemperamentException;
import com.thefourthspecies.ttuner.model.listables.CentsTemperament;
import com.thefourthspecies.ttuner.model.states.TunerState;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Allows creation and editing of a temperament by cents deviation from equal.
 * @author graham
 *
 */
public class CentsEditor extends ListActivity {

	TunerState state = TunerState.getInstance();
	
	CentsTemperament temperament;
	List<Double> deviations;
	List<Note> notes;
	
	EditText name;
	TextView note;
	EditText cents;
	Button next;
	Button clear;
	
	/**
	 * The position of the currently selected note.
	 */
	int selected;	
	
	ArrayAdapter<Double> adapter;
	
	/**
	 * True if this is a new temperament, not yet in the registry.
	 */
	boolean isNew;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cents_editor);
		// Show the Up button in the action bar.
		setupActionBar();

		name  = (EditText) findViewById(R.id.edit_name);
		note  = (TextView) findViewById(R.id.current_note);
		cents = (EditText) findViewById(R.id.deviation);
		next  = (Button)   findViewById(R.id.next_button);
		clear = (Button)   findViewById(R.id.clear_button);
		
		Intent intent = getIntent();
		int pos = intent.getIntExtra(ListableSelector.EXTRA_POSITION, -1);
		if (pos == -1) {
			isNew = true;
			temperament = new CentsTemperament();
			name.setText("");
		} else {
			isNew = false;
			temperament = (CentsTemperament) state.getTemperament(); 
			name.setText(temperament.getName());
		}
		name.setHint(temperament.getName());

		deviations = temperament.getDeviations();
		notes = temperament.getScale(0);
		
		
		
		adapter = new ArrayAdapter<Double>(this, R.layout.deviation, R.id.cents, deviations) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row = super.getView(position, convertView, parent);
				TextView note = (TextView) row.findViewById(R.id.note);
				TextView cents = (TextView) row.findViewById(R.id.cents);
				
				String name = notes.get(position).getName();
				note.setText(name + ": ");
			
				Double dev = deviations.get(position);
				cents.setText(CentsTemperament.formatCents(dev));
				
				return row;
			}
			
		};
		setListAdapter(adapter);
		
		// Make it so that by touching anything else, it sets whatever deviation has been typed in
		cents.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					saveDeviation();
					hideClear();
				} 
			}
		});
			
		
		clear.setVisibility(View.INVISIBLE);
		clear.setAlpha(0);
		
		selected = 0;
		updateEdit();
		
		getListView().requestFocus();
	}

	/**
	 * Saves the current deviation, and updates the display with the selected one.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		saveDeviation();
		selected = position;
		updateEdit();
		showClear();
	}	
	
	
	/**
	 * Sets the current deviation to the current note and updates display.
	 */
	protected void saveDeviation()  {
		String dev = cents.getText().toString();
		Double d = dev.equals("") ? 0 : Double.parseDouble(dev);
		
		try {
			if (selected == 0 && d != 0) {
				Toast.makeText(this, "A can only be set to 0 cents deviation.", Toast.LENGTH_SHORT).show();
			} else {
				temperament.setDeviation(selected, d);
				adapter.notifyDataSetChanged();
			}
		} catch (TemperamentException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	
	/**
	 * Updates the editable display with the currently-selected note's name and deviation
	 */
	private void updateEdit() {
		String name = notes.get(selected).getName();
		note.setText(name + ": ");
		
		Double dev = deviations.get(selected);
		cents.setText(dev.toString());

		setSelection(selected);
	}

	/**
	 * Shows Clear button, animating if possible
	 */
	@SuppressLint("NewApi")
	private void showClear() {
		clear.setVisibility(View.VISIBLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			clear.animate().alpha(1).setDuration(500);
		}
	}
	
	/**
	 * Hides Clear button, animating if possible
	 */
	@SuppressLint("NewApi")
	private void hideClear() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			clear.setAlpha(.5f);
			clear.animate().alpha(0).setDuration(200).withEndAction(new Runnable() {

				@Override
				public void run() {
					clear.setVisibility(View.INVISIBLE);
				}
				
			});
		} else {
			clear.setVisibility(View.INVISIBLE);
		} 
	}
	
	/**
	 * Approximately centers the list with the currently-selected note
	 */
	private void centerList() {
		int height = getListView().getHeight();
//		int rowHeight = adapter.getView(selected, null, parent1).getHeight();
//		int distance = (height - rowHeight)/2;
		getListView().setSelectionFromTop(selected, (height/2) - 10);  // magic, made-up number; should be calculated		
	}
	
	/**
	 * Called when Next is pressed
	 * Proceeds to the next note. If called at G#, returns to A
	 */
	public void nextNote(View v) {
		saveDeviation();
		selected = ++selected % CentsTemperament.NUMBER_OF_NOTES;
		updateEdit();
		centerList();
	}
	
	/**
	 * Called when Clear is pushed
	 * @param v
	 */
	public void clearDev(View v) {
		cents.setText("");
		saveDeviation();
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
		getMenuInflater().inflate(R.menu.cents_editor, menu);
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
	

	/**
	 * Produces true when name has at least one character
	 */
	private boolean goodName() {
		return name.getText().toString().length() > 0;
	}

	/**
	 * Produces true when every note's deviation is 0 -- i.e., it is equal temperament.
	 */
	private boolean isEqual() {
		for (Double d : deviations) {
			if (d != 0) return false;
		}
		return true;
	}

	/**
	 * Saves the temperament's name if it is good and enters it into the registry if it is new.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		saveDeviation();
		
		String n = name.getText().toString();
		if (goodName()) {
			temperament.setName(n);
		}
		
		if (isNew && (goodName() || !isEqual())) {
			int newPos = state.getRegistry().getTemperaments().size();
			state.getRegistry().addTemp(temperament);
			state.setTempPos(newPos);
		}
		state.saveData();
	}

}
