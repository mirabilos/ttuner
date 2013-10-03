package com.thefourthspecies.ttuner.model.listables;

import java.util.List;


/**
 * All Listables have certain things in common: a name and a default default value as false (That is
 * unless set otherwise, they are not considered defaults). Any Listable extending this class
 * and calling super() on creation will take care of those two things. This also provides a static
 * method to convert a list to a string, which is useful for getting the details of a Listable.
 * 
 * 
 * @author graham
 *
 */
public abstract class AbstractListable implements Listable {
	String name;
	boolean defaultListable;
	
	/**
	 * Assigns the default name and marks as not default
	 */
	protected AbstractListable() {
		defaultListable = false;
		name = defaultName();
	}
	
	@Override
	public boolean isDefault() {
		return defaultListable;
	}
	
	@Override
	public void makeDefault() {
		defaultListable = true;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the default name for this kind of Listable
	 * @return
	 */
	abstract String defaultName();

	@SuppressWarnings("rawtypes")
	static String listToString(List l) {
		if (l == null || l.isEmpty()) {
			return "";
		}

		String result = l.get(0).toString();
		for (int i = 1; i < l.size(); i++) {
			result = result + ", " + l.get(i).toString();
		}
		return result;
	}
	
}
