package com.thefourthspecies.ttuner.model.states;



import java.util.List;

import com.thefourthspecies.ttuner.model.Registry;
import com.thefourthspecies.ttuner.model.TunerStore;
import com.thefourthspecies.ttuner.model.data.Note;
import com.thefourthspecies.ttuner.model.listables.Listable;
import com.thefourthspecies.ttuner.model.listables.Temperament;
import com.thefourthspecies.ttuner.model.listables.Waveform;
import com.thefourthspecies.ttuner.model.sound.QuickTone;
import com.thefourthspecies.ttuner.model.sound.Tone;

import android.app.Activity;


/**
 * This class keeps track of the state of everything in TTuner. It uses a Registry
 * to keep track of all of the available Listables, though. This uses a Singleton pattern.
 * This also handles the changing of the state, including communicating with the Tone.
 * @author graham
 *
 */
public class TunerState {
	private static TunerState tunerState = new TunerState();
	public final static double DEFAULT_PITCH = 415;
	
	private TunerStore store;
	
	private Registry registry;
	private Temperament temperament;
	private Waveform waveform;
	private double pitchA;
	
	private Note note;
	private double frequency;
		
	private int index;
	private int octave;
	private List<Note> scale;

	private int temperamentPosition;
	private int waveformPosition;
	
	private boolean playing;
	
	private ListableState currentListable;
	
	private Tone tone;
	
	/**
	 * A default TunerState is a Nice Tone in equal-temperament, and on A at 440Hz;
	 */
	private TunerState() {
		initialize();
	}
	
	public static TunerState getInstance() {
		return tunerState;
	}
	
	/**
	 * Set State to Defaults, including default Registry.
	 */
	private void initialize() {
		pitchA = DEFAULT_PITCH;
		waveformPosition = 0;
		temperamentPosition = 0;
		octave = 0;
		index = 0; 									// SHOULD BE SAME AS: scale.indexOf(new Note("A"));
		currentListable = new TemperamentState();  	// arbitrary decision

		registry = new Registry();
		waveform = registry.getWave(0);
		temperament = registry.getTemp(0);

		tone = new QuickTone(waveform);
		playing = tone.isPlaying();

		makeScale();
		updateNote();
	}
	
	/**
	 * Return to default values, including default Listables.
	 */
	public void reset() {
		initialize();
	}
	
	/**
	 * Returns to initialized state (default values and listables).
	 * Sets the current ListableSelector state to state.
	 * @param state
	 */
	public void resetAll(ListableState state) {
		tone.stop();
		reset();
		setListableState(state);
	}
	
	
	
	/**
	 * Update state to play tone.
	 */
	public void playTone() {
		tone.play();
		playing = true;  
	}
	
	/**
	 * Update state to silence.
	 */
	public void stopTone() {
		tone.stop();
		playing = false; 
	}

	/**
	 * Return true if the sound is in a playing state.
	 */
	public boolean isPlaying() {
		return playing;
	}


	/**
	 * Returns the current Registry. Since this is set on Initialization and there are no methods to change it,
	 * its instance will never change.
	 * @return
	 */
	public Registry getRegistry() {
		return registry;
	}

	
	/**
	 * Set the type of Listable that can view/select in the ListableSelector.
	 */
	public void setListableState(ListableState currentListable) {
		this.currentListable = currentListable;
	}
	
	/**
	 * Get the type of Listable that can view/select in the ListableSelector.
	 */
	public ListableState getListableState() {
		return currentListable;
	}


	/**
	 * Gets currently-queued-up frequency.
	 */
	public double getFrequency() {
		return frequency;
	}
	
	/**
	 * Return currently-queued-up Note.
	 */
	public Note getNote() {
		return note;
	}
	
	/**
	 * Change note to next highest in current temperament.
	 */
	public void increaseNote() {
		index++;
		if (index == scale.size()) {
			index = 0;
			octave++;
		}
		updateNote();
	}
	
	/**
	 * Change note to next lowest in current temperament.
	 */
	public void decreaseNote() {
		index--;
		if (index == -1) {
			index = scale.size()-1;
			octave--;
		}
		updateNote();
	}
	
	/**
	 * Doubles frequency for current note.
	 */
	public void increaseOctave() {
		octave++;
		updateNote();
	}
	
	/**
	 * Halves frequency for current note.
	 */
	public void decreaseOctave() {
		octave--;
		updateNote();
	}
	
	
	
	/**
	 * Sets the current Temperament.
	 */
	public void setTemperament(Temperament temperament) {
		this.temperament = temperament;

		makeScale();

		if (!(scale.contains(note))) {
			note = closestNote();
		}
		index = scale.indexOf(note);
		updateNote();
	}
	
	/**
	 * Returns actual instance of the current temperament.
	 */
	public Temperament getTemperament() {
		return temperament;
	}
	
	/**
	 * Manually sets the position of the current Temperament in the registry. Does not actually set that temperament, though.
	 * This probably could be refactored to a better way. TODO
	 */
	public void setTempPos(int temperamentPosition) {
		this.temperamentPosition = temperamentPosition;
	}

	/**
	 * Returns the position of the current Temperament in the registry. May be wrong if it was set erroneously.
	 */
	public int getTempPos() {
		return temperamentPosition;
	}



	
	/**
	 * Sets the current Waveform.
	 */
	public void setWaveform(Waveform waveform) {
		this.waveform = waveform;
		tone.setWaveform(waveform);
	}
	
	/**
	 * Returns actual instance of current Waveform
	 */
	public Waveform getWaveform() {
		return waveform;
	}

	
	/** Manually sets the position of the current Waveformin the registry. Does not actually set that temperament, though.
	 * This probably could be refactored to a better way. TODO
	 */
	public void setWavePos(int waveformPosition) {
		this.waveformPosition = waveformPosition;
	}

	/**
	 * Returns the position of the current Temperament in the registry. May be wrong if it was set erroneously.
	 */
	public int getWavePos() {
		return waveformPosition;
	}

	
	
	/**
	 * Sets reference pitch for A.
	 */
	public void setPitchA(double pitchA) {
		this.pitchA = pitchA;
		makeScale();
		updateNote();
	}
	
	/**
	 * Returns reference pitch for A.
	 */
	public double getPitchA() {
		return pitchA;
	}
	
	/**
	 * Sets the current Note and assigns the appropriate frequency based on octave, sending that to the Tone.
	 */
	private void updateNote()  {
		note = scale.get(index);
		double freq = note.getFreq() == null ? 0 : note.getFreq();
		frequency = freq*Math.pow(2, octave);
		tone.setPitch(frequency);
	}


		
	
	
	/**
	 * Returns the Note whose potential frequency is closest to the current Note's frequency in the current (new) Temperament.
	 * @return
	 */
	private Note closestNote() {
		double delta = pitchA;
		Note closest = scale.get(0);
		double currentFreq = note.getFreq();
		for (Note n : scale) {
			double freq = n.getFreq() == null ? 0 : n.getFreq();
			double d = Math.abs(currentFreq - freq); 
			if (d <= delta) {
				closest = n;
				delta = d;
			}
		}
		return closest;
	}

	/**
	 * Assign the scale according to the current temperament.
	 */
	private void makeScale() {
		scale = temperament.getScale(pitchA);
	}

	
	
	
	/**
	 * Sets the activity that owns the preferences store where all the persistent data
	 * will be stored. Must be called before loadData() or saveData().
	 * @param activity
	 */
	public void initializeStore(Activity activity) {
		store = new TunerStore(activity);
	}
	
	/**
	 * Loads in data stored on Android device. Cannot be called before initializeStore(Activity).
	 */
	public void loadData() {
		
		// TEMPERAMENTS FIRST:
		List<Listable> temps = store.retrieveListables(TunerStore.Key.LISTABLE_TEMPERAMENTS);
		if (temps.size() != 0) {
			registry.clearTemperaments();
			registry.addTemperaments(temps);	
		}
		temperamentPosition = store.retrieveInt(TunerStore.Key.POS_TEMP);
		setTemperament(registry.getTemp(temperamentPosition));
		
		// WAVEFORMS NEXT:
		List<Listable> waves = store.retrieveListables(TunerStore.Key.LISTABLE_WAVEFORMS);
		if (waves.size() != 0) {
			registry.clearWaveforms();
			registry.addWaveforms(waves);
		}
		waveformPosition = store.retrieveInt(TunerStore.Key.POS_WAVE);
		setWaveform(registry.getWave(waveformPosition));
		
		// AND THE REST (PITCH/NOTE):
		double a = store.retrieveDouble(TunerStore.Key.PITCH);
		pitchA = a==0 ? DEFAULT_PITCH : a;
		
		index = store.retrieveInt(TunerStore.Key.NOTE_INDEX);
		octave = store.retrieveInt(TunerStore.Key.NOTE_OCTAVE);
		
		makeScale();
		updateNote();
	}

	/**
	 * Stores data in Android device. Store must first be initialized with initializeStore(Activity).
	 */
	public void saveData() {
		store.storeListables(TunerStore.Key.LISTABLE_WAVEFORMS, registry.getWaveforms());
		store.storeInt(TunerStore.Key.POS_WAVE, waveformPosition);

		store.storeListables(TunerStore.Key.LISTABLE_TEMPERAMENTS, registry.getTemperaments());
		store.storeInt(TunerStore.Key.POS_TEMP, temperamentPosition);
		
		store.storeDouble(TunerStore.Key.PITCH, pitchA);
		
		store.storeInt(TunerStore.Key.NOTE_INDEX, index);
		store.storeInt(TunerStore.Key.NOTE_OCTAVE, octave);		
	}
	
}
