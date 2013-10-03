package com.thefourthspecies.ttuner.model.exceptions;

/**
 * Called when there is a fundamental problem with a Temperament.
 * @author graham
 *
 */
@SuppressWarnings("serial")
public class TemperamentException extends TunerException {

	public TemperamentException(String msg) {
		super(msg);
	}
}
