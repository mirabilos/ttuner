package com.thefourthspecies.ttuner.model.sound;

import com.thefourthspecies.ttuner.model.listables.Waveform;

/**
 * An interface for Tone Generators. These generators have no maximum or minimums for pitches or
 * anything else.
 * 
 * @author graham
 *
 */
public interface Tone {
	
	/**
	 * Sets the frequency of the fundamental of the generated tone. Does not change
	 * play state. 
	 * @param pitch
	 */
	public void setPitch(double pitch);
	
	/**
	 * Sets the waveform to be used for the generated tone.
	 * Does not affect play state.
	 * @param wave
	 */
	public void setWaveform(Waveform wave);
	
	/**
	 * Starts the sound.
	 */
	public void play();
	
	/**
	 * Stops the sound.
	 */
	public void stop();
	
	/**
	 * Return true if a tone is currently being generated.
	 */
	public boolean isPlaying();
}
