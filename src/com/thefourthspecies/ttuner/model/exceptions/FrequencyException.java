package com.thefourthspecies.ttuner.model.exceptions;

/**
 * Called when, for example, there is an attempt to assign a frequency to
 * a note with a sufficiently different frequency.
 * @author graham
 *
 */
@SuppressWarnings("serial")
public class FrequencyException extends TunerException {

	public FrequencyException(String msg) {
		super(msg);
	}
}
