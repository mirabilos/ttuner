package com.thefourthspecies.ttuner.model.exceptions;

/**
 * Called when there is something wrong with the format of an input string, from either 
 * a Temperament definition, or a Waveform definition.
 * @author graham
 *
 */
@SuppressWarnings("serial")
public class ParseException extends TunerException {
	public ParseException(String msg) {
		super(msg);
	}
	
	public ParseException(String msg, int i) {
		super(msg, i);
	}
}
