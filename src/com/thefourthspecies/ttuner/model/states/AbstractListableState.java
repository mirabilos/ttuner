package com.thefourthspecies.ttuner.model.states;

import com.thefourthspecies.ttuner.model.listables.Listable;

/**
 * Some concrete methods that WaveformState and TemperamentState have in common
 * @author graham
 *
 */
public abstract class AbstractListableState implements ListableState {
		
	@Override
	public void copy(int position) {
		Listable l = getListable(position).getCopy();
		uniqueizeName(l);		
		addListable(l);
	}
	
	/**
	 * Assigns the given Listable a name that is different from every one in the
	 * current list of Listables. Does this by appending a number to the end of its name
	 */
	private void uniqueizeName(Listable l) {
		String name = l.getName();
		int count = 0;
		
		while (notUnique(name + count)) {
			count++;
		}
		l.setName(name + count);
	}

	/**
	 * Return true if there is already a Listable in the current list that whose
	 * name is given name
	 */
	private boolean notUnique(String name) {
		for (Listable l : getAll()) {
			if (l.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

}
