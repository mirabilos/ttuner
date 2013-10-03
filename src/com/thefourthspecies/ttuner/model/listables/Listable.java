package com.thefourthspecies.ttuner.model.listables;

import com.thefourthspecies.ttuner.model.exceptions.TunerException;

/**
 * These are the objects that are recorded in the registry and displayed for selection and 
 * editing as a fundamental part of the state of TTuner: Waveforms and Temperaments.
 * 
 * Details: refers to the defining characteristics of a Listable. I.e., in WAveforms, it is the list 
 * of weights; in RelationshipsTemperaments, it is the list of Relationships, and in CentsTemperamens
 * it is the list of cents deviations. 
 * @author graham
 *
 */
public interface Listable {
	
	public String getName();
	
	public void setName(String name);

	/**
	 * Returns a comma delimited, pretty string of the defining-characteristic array for the listable.
	 * E.g., For Waveforms, its the list of harmonic weights; for RelationshipsTemperaments,
	 * it's a list of Relationships; for CentsTemperamnts its a list of cents deviations.
	 */
	public String getDetails();
	
	
	/**
	 * Given a string in the same format as getDetails returns, sets the defining array for the listable. 
	 */
	public void setDetails(String details) throws TunerException;
	
	/**
	 * Returns a non-default copy of this Listable. 
	 * @return
	 */
	public Listable getCopy();
	
	/**
	 * Sets mutable field to false.
	 */
	public void makeDefault();
	
	/**
	 * Returns the mutable state of this Listable
	 */
	public boolean isDefault();
	
}
