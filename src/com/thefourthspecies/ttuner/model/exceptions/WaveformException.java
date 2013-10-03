package com.thefourthspecies.ttuner.model.exceptions;

/**
 * Called when there is somethign fundamentally wrong with a Waveform.
 * @author graham
 *
 */
@SuppressWarnings("serial")
public class WaveformException extends TunerException {
	
	public WaveformException(String msg) {
		super(msg);
	}
}
