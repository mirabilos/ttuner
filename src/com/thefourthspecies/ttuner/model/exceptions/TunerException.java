package com.thefourthspecies.ttuner.model.exceptions;


/**
 * Called when something unique to this program calls an exception. The superclass
 * to all checked exceptions in this program.
 * @author graham
 *
 */
@SuppressWarnings("serial")
public class TunerException extends Exception {
	int i;
	
	public TunerException() {
		super();
	}
	
	public TunerException(String msg) {
		super(msg);
	}
	
	public TunerException(String msg, int i) {
		super(msg);
		this.i = i;
	}
	
	public int getIndex() {
		return i;
	}
	
	public static void impossible(Exception e) throws IllegalStateException {
		throw new IllegalStateException("Error initializing pre-made data: " + e.getMessage());
	}
}
