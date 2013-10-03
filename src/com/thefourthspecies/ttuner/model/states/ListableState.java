package com.thefourthspecies.ttuner.model.states;


import java.util.List;

import com.thefourthspecies.ttuner.model.listables.Listable;

import android.app.Activity;

/**
 * This is designed for Waveforms or Temperaments since they share a similar
 * structure for listing. This interface provides convenient methods for
 * displaying that information
 * 
 * @author graham
 *
 */
public interface ListableState {
	
	/**
	 * In the list of Listables, sets the one at position as selected.
	 */
	void setListable(int position);
	
	/**
	 * Returns the registered Listable at the given position in the list.
	 */
	Listable getListable(int position);

	/**
	 * Adds the given Listable to the the current list. Throws Cast exception if
	 * the wrong type of listable is added.
	 */
	void addListable(Listable listable);
	
	/**
	 * Gets the position of the currently-selected listable.
	 */
	int getPos();
	
	/**
	 * Gets a list of all Registered listables of the current subtype.
	 */
	List<Listable> getAll();
	
	/**
	 * The editor activity for editing and creating new Listables of this
	 * subtype.
	 */
	Class<? extends Activity> getEditor();

	/**
	 * The simple name of the actual subtype of the current Listable.
	 */
	String getType();
	
	/**
	 * Removes the Listable at position from the list.
	 */
	void delete(int position);

	/**
	 * Adds a copy of the Listable at position to the bottom of the list.
	 */
	void copy(int position);
	
	/**
	 * Returns the ordinal of the appropriate help screen. 
	 */
	int getHelp();
}
