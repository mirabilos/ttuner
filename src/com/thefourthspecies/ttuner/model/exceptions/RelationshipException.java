package com.thefourthspecies.ttuner.model.exceptions;

/**
 * Called when a Relationship is attempted to be created that isn't possible.
 * @author graham
 *
 */
@SuppressWarnings("serial")
public class RelationshipException extends TunerException {
	public RelationshipException(String msg) {
		super(msg);
	}
	
	public RelationshipException(String msg, int i) {
		super(msg, i);
	}
 }
