package com.thefourthspecies.ttuner.model.listables;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.thefourthspecies.ttuner.model.data.Note;
import com.thefourthspecies.ttuner.model.exceptions.FrequencyException;
import com.thefourthspecies.ttuner.model.exceptions.ParseException;
import com.thefourthspecies.ttuner.model.exceptions.TemperamentException;
import com.thefourthspecies.ttuner.model.exceptions.TunerException;

/**
 * These temperaments are defined as deviations from equal temperament in units of cents
 * for each of the twelve notes of an equal tempered scale. Consequently, it only allows
 * for 12-note temperaments.
 * @author graham
 *
 */
public class CentsTemperament extends AbstractTemperament {
	
	/**
	 * Any details string of a CentsTemperament will start with these 5 characters
	 */
	public final static String DETAILS_START = "A+0.0";
	
	/**
	 * CentsTemperaments all have this same number of notes in them (12).
	 */
	public final static int NUMBER_OF_NOTES = 12;

	/**
	 * The pattern used by DecimalFormatter to display deviations.
	 */
	final static String DEV_PATTERN = "+0.0##############;-";
	
	/**
	 * The decimal format of the equal-tempered semitone ratio.
	 */
	final static double SEMITONE = Math.pow(2, 1.0/12.0);
	
	
	/*
	 * I experimented with Maps and enums and other ways of associating notes
	 * with cents-deviations, but in the end I found that simply keeping two
	 * separate lists was easiest (besides, enums woulnd't accept # in the name).
	 * 
	 * Since I ultimately wanted each CentsTemperament to have its have its own list
	 * of notes with appropriate frequencies that it can pass to the TunerState as a scale,
	 * keeping that as a list here made sense. So each CentsTemperament is initialized with
	 * a new scale of 12 notes using setUpScale() as well a 12 note List of Doubles set to 0: equal
	 * temperament.
	 * 	
	 * 
	 */
	private List<Note> scale;
	private List<Double> deviations;
	
	
	public CentsTemperament() {
		super();
		deviations = new ArrayList<Double>(NUMBER_OF_NOTES);
		for (int i = 0; i < NUMBER_OF_NOTES; i++) {
			deviations.add(0.0);
		}
		setupScale();
	}

	
	/**
	 * Returns the actual instance of the list of deviations.
	 */
	public List<Double> getDeviations() { 
		return deviations;
	}
	
	/**
	 * Assigns the given list as the deviations.
	 * @throws TemperamentException		if a deviation is greater than 100 cents.
	 */
	public void setDeviations(List<Double> deviations) throws TemperamentException {
		/* Needs to check for bad deviation before setting,
		 * so we go through each one by one */
		for (int i = 0; i < NUMBER_OF_NOTES; i++) {
			double dev = deviations.get(i);
			setDeviation(i, dev);
		}
	}
	
	/**
	 * Sets the deviation of a  note.
	 * @param note	the note we are setting
	 * @param dev	the amount of deviation from equal temp in cents
	 * @throws TemperamentException		if the deviation is greater than 100 cents
	 */
	public void setDeviation(int note, double dev) throws TemperamentException {
		if (Math.abs(dev) > 100) {
			throw new TemperamentException("Cannot deviate by more than 100 cents. Given " + dev);
		}
		deviations.set(note, dev);
	}
	
	
	/**
	 * Returns the frequency of the note at index according to its deciation and 
	 * within an octave of given reference pitch of A.
	 * @param index    the number of semitones above A for the note in question
	 * @param pitchA   the reference frequency of A
	 */
	private double calculateFreq(double pitchA, int index) {
		 double eqFreq = pitchA * Math.pow(2, index/12.0);
		 double cents = deviations.get(index);
		 double dev = Math.pow(SEMITONE, cents/100.0);
		 return eqFreq * dev;
	}
	

	/**
	 * Initalizes scale to the twelve empty notes of the chromatic scale.
	 */
	private void setupScale() {
		scale = new ArrayList<Note>(NUMBER_OF_NOTES);
		add("A"); add("Bb"); add("B"); add("C"); add("C#"); add("D");
		add("Eb"); add("E"); add("F"); add("F#"); add("G"); add("G#");
	}
	/**
	 * A helper to setUpScale(); adds a Note with given name to scale.
	 */
	private void add(String name) {
		Note n;
		try {
			n = new Note(name);
			scale.add(n);
		} catch (ParseException e) {
			TunerException.impossible(e);
		}
	}
	
	@Override
	public List<Note> getScale(double pitchA) {
		clearFreqs();
		for (int i = 0; i < NUMBER_OF_NOTES; i++) {
			Note n = scale.get(i);
			
			double freq = calculateFreq(pitchA, i);
			
			try {
				n.setFreq(freq);
			} catch (FrequencyException e) {
				TunerException.impossible(e);
			}
		}
		return scale;
	}

	/**
	 * Sets all frequencies in notes to null, so they can be re-set.
	 */
	private void clearFreqs() {
		for (Note n : scale) {
			n.eraseFreq();
		}
	}

	
	@Override
	public String getDetails() {
		return listToString(makeDetails());
	}

	/**
	 * Return the ordered list of detail-formatted Note/deviation pair.
	 * @return
	 */
	private List<String> makeDetails() {
		List<String> list = new ArrayList<String>(NUMBER_OF_NOTES);
		for (int i = 0; i < NUMBER_OF_NOTES; i++) {
			list.add(makeDetail(i));
		}
		return list;
	}
	
	/**
	 * Given the note at index, returns a nicely-formatted String of its deviation
	 * according to DEV_PATTERN. The order of the notes is A Bb C C# D Eb E F F# G G#
	 * Results in note name/deviation pair, eg: A+0.0, or C-3.6
	 */
	public String makeDetail(int index) {
		String dev = formatCents(deviations.get(index));
		String name = scale.get(index).getName();
		return name + dev;
	}
	
	/**
	 * Formats given Double to the style used by CentsTemperament for its deviations.
	 */
	public static String formatCents(Double d) {
		DecimalFormat formatter = new DecimalFormat(DEV_PATTERN);
		return formatter.format(d);	
	}
	
	@Override
	public void setDetails(String detailString) throws TemperamentException {
		String[] details = detailString.split(",");
		List<Double> devs = new ArrayList<Double>();
		for (String d : details) {
			devs.add(getDetail(d));
		}
		setDeviations(devs);
	}
	/**
	 * Returns the double corresponding to the deviation of the given string
	 * representation (detail) of a deviation. E.g., C+5 returns 5 and F#-2.6 returns -2.6.
	 */
	private double getDetail(String d) {
		/* Since each detail will have either a + or - but not both, just try getting 
		 * the index of one, and if it isn't there, try the other's. Then parse the 
		 * string from the resultant index to the end. */
		int index = d.indexOf("+");
		if (index == -1) {
			index = d.indexOf("-");
		}
		String num = d.substring(index);
		return Double.parseDouble(num);
	}

	
	@Override
	public int hashCode() {
		return deviations.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CentsTemperament)) {
			return false;
		}
		CentsTemperament other = (CentsTemperament) o;
		return this.deviations.equals(other.deviations) &&
			   this.name.equals(other.name);
	}

	@Override
	public String toString() {
		return name + deviations;
	}
}

