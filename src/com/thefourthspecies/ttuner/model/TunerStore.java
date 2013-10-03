package com.thefourthspecies.ttuner.model;


import java.util.ArrayList;
import java.util.List;

import com.thefourthspecies.ttuner.model.exceptions.TunerException;
import com.thefourthspecies.ttuner.model.listables.Listable;
import com.thefourthspecies.ttuner.model.listables.TemperamentFactory;
import com.thefourthspecies.ttuner.model.listables.Waveform;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Uses SharedPreferences to store key-value pairs of the data in TTuner.
 * Stores the contents of the registry as a giant string for each Listable.
 * Each Listable is separated from the next by LISTABLE_DELIMITER and each Listable
 * has three components, all separataer by LISTABLE_SEPARATOR: name, details, and a boolean string representing
 * whether or not it is default.
 * Other values are stored as ints except pitch, which is stored as a string and then parsed as a double on return. 
 * 
 * (Another option would have been to store them as a StringSet. See earlier versions of TTuner for a non-tested implementation
 * of that.)
 * 
 * @author graham
 *
 */
public class TunerStore {
	final String LISTABLE_DELIMITER = ";";
	final String LISTABLE_SEPARATOR = "&";

//	private final String STORE_NAME = "graham.ttuner.STORE";    //Leftover from when using the other SharedPreferences version

	/**
	 * The default string form of these are used as Keys for the SharedPreferences
	 */
	public enum Key {
		LISTABLE_TEMPERAMENTS,
		LISTABLE_WAVEFORMS,
		PITCH,
		POS_WAVE,
		POS_TEMP,
		NOTE_INDEX,
		NOTE_OCTAVE;
	}
	
	public SharedPreferences preferences;
	

	/**
	 * Initializes the TunerStore with its default private SharedPreferences
	 * @param activity   The activity whose SharedPreferences we will be accessing.
	 */
	public TunerStore(Activity activity) {
		preferences = activity.getPreferences(Activity.MODE_PRIVATE);
	}
	
	/**
	 * Inputs and saves a value in the preferences
	 * @param key		must be POS_WAVE, POS_TEMP, NOTE_INDEX, or NOTE_OCTAVE
	 * @param value		its integer value
	 */
	public void storeInt(Key key, int value) {
		preferences.edit().putInt(key.toString(), value).commit();
	}
	
	/**
	 * Returns the int stored at Key, or 0 if none exists. Throws exception if wrong type stored at Key.
	 * @param key 		must be POS_WAVE, POS_TEMP, NOTE_INDEX, or NOTE_OCTAVE
	 */
	public int retrieveInt(Key key) {
		return preferences.getInt(key.toString(), 0);
	}
	
	/**
	 * Stores a double as a String in the preferences.
	 * @param key		should be PITCH
	 */
	public void storeDouble(Key key, Double value) {
		preferences.edit().putString(key.toString(), value.toString()).commit();
	}
	
	/**
	 * Return the double stored at Key parsed as a Double, or 0.0 if it does not exist. Throws exception if wrong type stored at Key.
	 * @param key 		should be PITCH
	 */
	public double retrieveDouble(Key key) {
		String d = preferences.getString(key.toString(), "0");
		return Double.parseDouble(d);
	}

	
	/**
	 * Converts the given list into a string representing all given listables and stores it
	 * as the value in a key-value pair where the key is the the type of listable. Throws
	 * Runtime exception if given key isn't for a LISTABLE.
	 * @param key
	 * @param listables
	 */
	public void storeListables(Key key, List<Listable> listables) {
		ensureListableKey(key);
		
		String value = makeString(listables);
		preferences.edit().putString(key.toString(), value).commit();
	}
	
	/**
	 * Returns a list of the given type of listable in the store.  Throws
	 * Runtime exception if given key isn't for a LISTABLE.
	 * @param key
	 * @return
	 */
	public List<Listable> retrieveListables(Key key) {
		ensureListableKey(key);
//		return retrieveList(key);
		String value = preferences.getString(key.toString(), null);
		return parseListables(key, value);
	}


	
	/**
	 * Returns the given list as a delimited string representation. DELIMITER separates
	 * different listables. SEPARATOR separates the components of a listable (ie. the name,
	 * and the details; maybe the default status, later on.)
	 * @param listables
	 * @return
	 */
	private String makeString(List<Listable> listables) {
		String value = "";
		for (Listable l : listables) {
			value += stringForm(l) + LISTABLE_DELIMITER;
		}
		return value;
	}

	/**
	 * Converts a listable into a string with its name, details, and defaultness separated by SEPARATOR
	 * @param l
	 * @return
	 */
	private String stringForm(Listable l) {
		return l.getName() + LISTABLE_SEPARATOR + 
			   l.getDetails() + LISTABLE_SEPARATOR +
			   l.isDefault();
	}
	
	

	/**
	 * Converts a string form of listables into an actual List.
	 * @param value
	 * @return
	 */
	private List<Listable> parseListables(Key key, String value) {
		List<Listable> list = new ArrayList<Listable>();
		if (value == null) {
			return list;
		}
		String[] listableStrings = value.split(LISTABLE_DELIMITER);
		for (String s : listableStrings) {
			Listable l = fromString(key, s);
			list.add(l);
		}
		return list;
	}

	/**
	 * Converts a String into a new Listable
	 * @param s
	 * @return
	 */
	private Listable fromString(Key key, String s) {
		Listable listable;
		String[] components = s.split(LISTABLE_SEPARATOR);
		
		String name = components[0];
		String details = components[1];

		
		try {

			if (key==Key.LISTABLE_TEMPERAMENTS) {
				listable = TemperamentFactory.createTemperament(name, details);
			} else 
			if (key==Key.LISTABLE_WAVEFORMS) {
				listable = new Waveform(name, details);
			} else {
				throw new IllegalArgumentException("Conversion fromString() currently only supports LISTABLE_TEMPERAMENTS" +
						"or LISTABLE_WAVEFORMS. Given " + key);
			}

			if (components[2].equals("true")) {
				listable.makeDefault();
			}

			return listable;
		
		} catch (TunerException e) {
			throw new IllegalStateException("Error converting stored key-values to Listables");
		}		
	}

	/**
	 * Throws exception if a key is used other than one of the LISTABLE_xxx keys.
	 * @param key
	 * @throws RuntimeException
	 */
	private static void ensureListableKey(Key key) throws RuntimeException {
		if (!(key==Key.LISTABLE_TEMPERAMENTS || key==Key.LISTABLE_WAVEFORMS)) {
			throw new IllegalArgumentException("Key must be either LISTABLE_TEMPERAMENTS" +
					"or LISTABLE_WAVEFORMS. Given " + key);
		}	
	}
	
}
