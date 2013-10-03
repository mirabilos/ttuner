package com.thefourthspecies.ttuner;

import com.thefourthspecies.ttuner.R;

import java.util.ArrayList;
import java.util.List;

import com.thefourthspecies.ttuner.model.exceptions.WaveformException;
import com.thefourthspecies.ttuner.model.listables.Waveform;
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
 * Allows editing of Waveforms by setting Relative weights of Harmonics.
 * Has a 
 * @author graham
 *
 */
public class WaveformEditor extends ListActivity {
	TunerState state = TunerState.getInstance();

	/**
	 * The minimum length of the list of harmonics.
	 */
	final private int DEFAULT_DEPTH = 32;
	
	Waveform waveform;
	List<Double> harmonics;
	
	ArrayAdapter<Double> adapter;
	
	boolean isNew;

	EditText name;
	TextView allHarmonics;
	EditText weight;
	TextView harmonic;
	Button clear;
	
	int harmonicNumber;
	
	Toast toast;
	
	int selected;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waveform_editor);
		// Show the Up button in the action bar.
		setupActionBar();
	
		name = (EditText) findViewById(R.id.edit_name);
		allHarmonics = (TextView) findViewById(R.id.harmonics);
		harmonic = (TextView) findViewById(R.id.harmonic);
		weight = (EditText) findViewById(R.id.weight);
		clear = (Button) findViewById(R.id.clear_button);
		
		clear.setVisibility(View.INVISIBLE);
		toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		
		Intent intent = getIntent();
		int position = intent.getIntExtra(ListableSelector.EXTRA_POSITION, -1);
		
		if (position == -1) {
			isNew = true;
			waveform = new Waveform();
		} else {
			isNew = false;
			waveform = state.getRegistry().getWave(position);
			name.setText(waveform.getName());
		}
		harmonics = filledHarms();
		
		System.out.println(Waveform.formatAll(harmonics));
		System.out.println(waveform.getDetails());
		
		harmonicNumber = 1;
				
		name.setHint(waveform.getName());
		
		adapter = new ArrayAdapter<Double>(this, R.layout.harmonic, R.id.weight, harmonics) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent){
				final View harm = (View) super.getView(position, convertView, parent);
				final TextView ordinal = (TextView) harm.findViewById(R.id.ordinal);
				final TextView weight = (TextView) harm.findViewById(R.id.weight);
	
				ordinal.setText("Harmonic " + (position + 1) + ": ");
				weight.setText(Waveform.formatWeight(getItem(position)));
									
				return harm;
			}
		};
		setListAdapter(adapter);
		
		
		// Whenever anything other than the weight area is pressed, make sure that the number in the weight textbox is recorded
		weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					hideClear();
					setWeight();
					update();
				} else {
					showClear();
				}
				
			}

						
		});
		
		update();
		
	}	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Sets the current weight and updates display.
		setWeight();
		harmonicNumber = position + 1;
		update();
	}
	
	/**
	 * Updates display based on current state.
	 */
	private void update() {
		harmonic.setText("Harmonic " + harmonicNumber + ": ");
		Double w = waveform.getHarmonic(harmonicNumber);
		String wString = w == 0 ? "" : w.toString();

		weight.setText(wString);
		
		List<Double> harms = new ArrayList<Double>(harmonics);
		cleanList(harms);
		allHarmonics.setText(Waveform.formatAll(harms));
		adapter.notifyDataSetChanged();
		setSelection(harmonicNumber-1);
	}
	
	/**
	 * Called when Clear is pressed.
	 * Sets the displayed, editable weight to 0.0
	 */
	public void clearWeight(View v) {
		weight.setText("");
		setWeight();
		update();
	}
	
	/**
	 * Called when Next is pressed.
	 * Sets the current harmonic's weight and brings up the next one.
	 */
	public void nextWeight(View view) {
		setWeight();
		harmonicNumber++;
		update();
	}
	
	/**
	 * Ensures is showing has at least 32 harmonics, by default.
	 */
	private List<Double> filledHarms() {
		if (waveform.getHarmonic(DEFAULT_DEPTH) == 0.0) {
			try {
				waveform.setHarmonic(DEFAULT_DEPTH, 0.0);
			} catch (WaveformException e) {
				throw new IllegalStateException("Any harmonic should be able to be set to 0.0");
		
			}
		}
		return waveform.getHarmonics();
	}
	
	
	/**
	 * Removes any trailing 0 weights from list.
	 */
	private void cleanList(List<Double> harms) {
		for (int i = harms.size()-1; i > 0; i--) {
			if (harms.get(i) != 0) {
				return;
			}
			harms.remove(i);
		}
	}
	
	/**
	 * Sets the weight of the currently-showing harmonic number to the number currently input at Weight.
	 */
	private void setWeight() {
		String input = weight.getText().toString();
		System.out.println(input);
		double w = (input.equals("")) ? 0.0 : Double.parseDouble(input);
		try {
			waveform.setHarmonic(harmonicNumber, w);
		} catch (WaveformException e) {
			toast.setText(e.getMessage());
			toast.show();
		}
	}
	
	

	/**
	 * Hides the Clear button, animate if possible.
	 */
	@SuppressLint("NewApi")
	private void hideClear() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			clear.setAlpha(0.5f);
			clear.animate().alpha(0f).setDuration(100).withEndAction(new Runnable() {

				@Override
				public void run() {
					clear.setVisibility(View.INVISIBLE);
				}
				
			});

		} else {
			clear.setVisibility(View.INVISIBLE);  // TODO UNTESTED
		}
	}

	/**
	 * Shows the clear button, animate if possible.
	 */
	@SuppressLint("NewApi")
	private void showClear() {
		clear.setAlpha(0);
		clear.setVisibility(View.VISIBLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			clear.animate().alpha(1).setDuration(500);
		} else {
			clear.setAlpha(1);   // UNTESTED TODO
		}
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
		getMenuInflater().inflate(R.menu.waveform_editor, menu);
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
			intent.putExtra(HelpMain.ITEM, HelpMain.WAVEFORMS);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// If the waveform is new and has good data, save it to the registry and save data in store. Otherwise it is already
		// there and just needs to be updated.
		
		super.onPause();
		
		setWeight();
		cleanList(harmonics);

		String newName = name.getText().toString();
		boolean goodName = newName.length() > 0;
		if (goodName) {
			waveform.setName(newName);
		}
	
		
		if (isNew && (goodName || harmonics.size() > 1)) {
			state.setWavePos(state.getRegistry().getWaveforms().size());
			state.getRegistry().addWave(waveform);
		}
		
		state.saveData();
	}

	
}
