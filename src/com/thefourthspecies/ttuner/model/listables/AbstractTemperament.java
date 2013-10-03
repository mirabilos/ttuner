package com.thefourthspecies.ttuner.model.listables;

import com.thefourthspecies.ttuner.model.exceptions.TunerException;

/**
 * Uses the TemperamentFactory to make copies and sets the default name.
 * @author graham
 *
 */
public abstract class AbstractTemperament extends AbstractListable implements Temperament {
	
	AbstractTemperament() {
		super();
	}
	
	@Override
	public Listable getCopy() {
		try {
			Temperament temp = TemperamentFactory.createTemperament(getName(), getDetails());
			return temp;
		} catch (TunerException e) {
			TunerException.impossible(e);
		}
		return null;
	}
	
	@Override
	String defaultName() {
		return "temperament";
	}
}
