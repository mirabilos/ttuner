package com.thefourthspecies.ttuner.model.listables;

import com.thefourthspecies.ttuner.model.exceptions.TunerException;

/**
 * Provides a static method for creating new Temperaments
 * @author graham
 *
 */
public class TemperamentFactory {
	
	
	/**
	 * Returns an initialized, temperament of the appropriate type
	 * with the given name and the given list of details. It determines the appropriate
	 * kind of temperament by the format of the given details.
	 *
	 * @throws TunerException		if something goes awry.
	 */
	public static Temperament createTemperament(String name, String details) throws TunerException {
		Temperament temp;
		// CentsTemperament all begin with the same bit since the deviation from A is always 0.
		if (details.startsWith(CentsTemperament.DETAILS_START)) {
			temp = new CentsTemperament();
		} else {
			temp = new RelationshipsTemperament();
		}
		
		temp.setName(name);
		temp.setDetails(details);
		return temp;
	}

}
