package com.thefourthspecies.ttuner;

import com.thefourthspecies.ttuner.R;

import java.text.DecimalFormat;

import com.thefourthspecies.ttuner.model.states.TemperamentState;
import com.thefourthspecies.ttuner.model.states.TunerState;
import com.thefourthspecies.ttuner.model.states.WaveformState;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Starts TTuner the Temperament Tuner for Android.
 * Plays one tone in your choice of Temperament with your choice of timbre.
 * Allows changing of the tone throughout the notes of the temperament.
 * Temperament can be defined by deviations from pure intervals by fractions of a comma, or by 
 * deviations from equal by cents.
 * Reference pitch for A can be an arbitrary value.
 * Defaults to A415, note A (=415Hz), and Equal Temperament.
 * Saves state on exit.
 * 
 * @author graham
 *
 */
public class TTuner extends Activity {
//	final static private String sharp = "\u266F";
//	final static private String flat = "\u266D";
	
	Button mute;
	Button pitchA;
	Button temperament;
	Button waveform;
	TextView note;
	TextView noteHeader;
	
	TunerState state = TunerState.getInstance();
	
	Intent intent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ttuner);
		
		// Must do this here, as opposed to in TunerState because requires an activity.
		state.initializeStore(this);
		state.loadData();
		
		intent = new Intent(this, ListableSelector.class);
		
		mute = (Button) findViewById(R.id.mute);
		pitchA = (Button) findViewById(R.id.pitch_A);
		temperament = (Button) findViewById(R.id.temperament);
		waveform = (Button) findViewById(R.id.waveform);
		note = (TextView) findViewById(R.id.note);
		noteHeader = (TextView) findViewById(R.id.note_header);
				
		temperament.setText(state.getTemperament().getName());
		temperament.setSelected(true);
		
		waveform.setText(state.getWaveform().getName());
		waveform.setSelected(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		updatePitchA();
		updateNote();
		updateMute();
		
	}
	

	/**
	 * Called when user presses Temperaments button.
	 * Starts the ListableSelector enable for Temperament selection.
	 */
	public void seeTemperaments(View view) {
		state.setListableState(new TemperamentState());
		startActivity(intent);
	}

	/**
	 * Called when used presses Waveforms button
	 * Starts the ListableSelector enable for Waveform selection.
	 */
	public void seeWaveforms(View view) {
		state.setListableState(new WaveformState());
		startActivity(intent);
	}

	
	/**
	 * Called when use presses the mute/play button
	 * Toggles sound and updates display.
	 */
	public void handleMute(View view) {
		if (state.isPlaying()) {
			state.stopTone();
		} else {
			state.playTone();
		}
		updateMute();
	}

	/**
	 * Called when AXXX button is pressed (Reference Tone)
	 * Opens a dialog allowing decimal input for the reference tone's frequency and updates display and sound.
	 */
	public void setPitchA(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		View pitchView = getLayoutInflater().inflate(R.layout.pitch, null);
		final EditText pitch = (EditText) pitchView.findViewById(R.id.pitch);
		String currentPitch = ((Double) state.getPitchA()).toString();
		pitch.setText(currentPitch);
		pitch.setHint(currentPitch);
		builder.setView(pitchView);
			
		builder.setTitle(R.string.set_A);

		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String input = pitch.getText().toString();
				if (input.length() > 0) {
					Double p = Double.parseDouble(input);
					state.setPitchA(p);
					updatePitchA();
					updateNote();
				}
				dialog.dismiss();	
			}
		});
		
		builder.show();
	}

	/**
	 * Called when +Note button is pressed.
	 * Moves state to next note up in temperament and updates display.
	 */
	public void noteUp(View view) {
		state.increaseNote();
		updateNote();		
	}

	/**
	 * Called when -Note button is pressed.
	 * Moves state to next note down in temperament and updates display.
	 */
	public void noteDown(View view) {
		state.decreaseNote();
		updateNote();
	}

	/**
	 * Called when +Octave button pressed
	 * Moves state to next octave up and updates display
	 */
	public void octaveUp(View view) {
		state.increaseOctave();
		updateNote();
	}

	/**
	 * Called when -Octave button pressed
	 * Moves state to next octave down and updates display
	 */
	public void octaveDown(View view) {
		state.decreaseOctave();
		updateNote();
	}


	
	/**
	 * Update display of play/mute button, as well as header for note. 
	 * Shows "play" when there is no sound; shows "mute" when sound is playing. 
	 */
	private void updateMute() {
		if (state.isPlaying()) {
			mute.setText("mute");
			noteHeader.setText("Now playing:");
		} else {
			mute.setText("play");
			noteHeader.setText("Ready to play:");
		}
	}

	/**
	 * Formats and displays current note name and frequency.
	 */
	private void updateNote() {
		if (state.getNote() == null) {
			note.setText("");
			return;
		}

		String name = state.getNote().getName();

		DecimalFormat df = new DecimalFormat("#.00");
		String num = df.format(state.getFrequency());

		note.setText(name + " at " + num + " Hz");
	}

	/**
	 * Formats to no decimals and updates display for current reference pitch.
	 */
	private void updatePitchA() {
		int pitch = (int) state.getPitchA();
		String text = "A" + pitch;
		pitchA.setText(text);
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		// Saves data in store on exit.
		state.saveData();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ttuner, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.help:
				Intent intent = new Intent(this, HelpMain.class);
				intent.putExtra(HelpMain.ITEM, HelpMain.HOME);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
		
}
