package com.thefourthspecies.ttuner.model.exceptions;


/**
 * Called when there is a problem with the tone generator.
 * @author graham
 *
 */
@SuppressWarnings("serial")
public class ToneException extends TunerException {
	public ToneException(String msg) {
		super(msg);
	}
}
