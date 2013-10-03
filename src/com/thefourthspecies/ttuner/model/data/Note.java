package com.thefourthspecies.ttuner.model.data;



import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.thefourthspecies.ttuner.model.exceptions.FrequencyException;
import com.thefourthspecies.ttuner.model.exceptions.ParseException;



/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 * 	(It compareTo is based on Frequency; equals is based on note name. Later,
 * 	maybe make compareTo based on Note name for equal frequencies.)
 * Also, Notes exists that can have null frequencies. That means that they would
 * cause an exception if they were ordered.
 * 
 * All notes have a letter name from A-G. Some have accidentals as part of their name.
 * Based on its name, a note has an assigned value too which represents its semitone
 * distance above C. Notes can be assigned a frequency; once set
 * this cannot be changed to something new without throwing a FrequencyException.
 * 
 * @author graham
 *
 */
public class Note implements Comparable<Note> {

	private static final double DELTA_RATIO = 1.00008; // When comparing two frequencies, if they are less than this ratio, they are considered equal.

	private static final String NOTE_REGEX = "[A-G][#b]*";
	
	/**
	 * A collection of Note names and their semitone distance above C
	 * Accidentals are +- 1,
	 */
	private static final Map<Character, Integer> MAPPINGS; 
	static {
		Map<Character, Integer> m = new HashMap<Character, Integer>();
		m.put(ch("C"), 0);
		m.put(ch("D"), 2);
		m.put(ch("E"), 4);
		m.put(ch("F"), 5);
		m.put(ch("G"), 7);
		m.put(ch("A"), 9);
		m.put(ch("B"), 11);
		
		m.put(ch("#"), 1);
		m.put(ch("b"), -1);
		
		MAPPINGS = Collections.unmodifiableMap(m);	
	} 
	static private char ch(String c) { return c.charAt(0); }
	
	
	private String name;
	private Double freq;
	private Integer value;
	
	/**
	 * Notes must be defined with a Letter Name.
	 */
	@SuppressWarnings("unused")
	private Note(){}
	
	/**
	 * Note names must be A-G with or without # or b accidentals following
	 * @param name
	 * @throws ParseException
	 */
	public Note(String name) throws ParseException {
		this.name = name;
		value = getValue(name);
	}
		
	public String  getName()  { return name;  }
	public Integer getValue() { return value; }
	public Double  getFreq()  { return freq;  };
		
	/**
	 * You can only assign a frequency if it doesn't already have one, or it is the same one,
	 * otherwise throws FrequencyException. Frequency must be positive.
	 * @param given
	 * @throws FrequencyException
	 */
	public void setFreq(double given) throws FrequencyException {
		if (given < 0) {
			throw new FrequencyException(given + " is not a valid pitch freqency for " + name);
		}
		
		Double old = getFreq();
		if (old != null && tooDifferent(old, given)) {
			throw new FrequencyException("Incompatible relationship: " + name + " cannot be both " + given + "Hz and " + old + "Hz");
		}
		freq = given;
	}
	
	/**
	 * Returns true if two numbers are too different from each other based on their ratio
	 * and DELTA_RATIO
	 */
	private boolean tooDifferent(Double old, Double given) {
		double max = Math.max(old, given);
		double min = Math.min(old, given);
		return max/min > DELTA_RATIO;
	}

	
	/**
	 * EFFECTS: returns its integer value for a 12-tone system: the distance
	 * above middle C.
	 */
	private int getValue(String name) throws ParseException {
		if (!name.matches(NOTE_REGEX)) {
			throw new ParseException("No such note name: " + name);
		}

		int value = 0;
		for (char c : name.toCharArray()) {
			value += MAPPINGS.get(c);
		}
		value = value % 12;
		return (value >= 0) ? value : 12 + value;
	}
	

	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Note)) {
			return false;
		}
		Note other = (Note) obj;
		return this.name.equals(other.name);
	}
	
	@Override
	public String toString() {
		return "[" + name + "=" + freq + "Hz]";
	}

	/**
	 * Throws exception if note has no assigned frequency. Otherwise, higher-frequency notes
	 * are considered to follow lower-frquency notes.
	 */
	@Override
	public int compareTo(Note n) {
		return freq.compareTo(n.freq);
	}

	/**
	 * Sets frequency to null, so it can be reset to something else without throwing exception.
	 */
	public void eraseFreq() {
		freq = null;
	}
}
