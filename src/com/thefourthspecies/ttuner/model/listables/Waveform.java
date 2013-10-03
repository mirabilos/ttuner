package com.thefourthspecies.ttuner.model.listables;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.thefourthspecies.ttuner.model.exceptions.ParseException;
import com.thefourthspecies.ttuner.model.exceptions.TunerException;
import com.thefourthspecies.ttuner.model.exceptions.WaveformException;



/**
 * Allows a tone's waveform to be specified as a list of relative strengths of its harmonics.
 * Has a name and a list of relative weights for the different overtones of the 
 * harmonic series. The characteristic that defines equals() however is just
 * an unnormalized list of overtone weights (harmonics) and its name.
 * @author graham
 *
 */
public class Waveform extends AbstractListable {
	
	private ArrayList<Double> harmonics;    // An array with the relative weights for an arbitrary depth of overtones, starting with the fundamental.
	private double totalWeights;   // The sum of all the weights. This is to save time later.

	public Waveform() {
		super();
		harmonics = new ArrayList<Double>();
		harmonics.add(1.0);
		totalWeights = 1.0;
	}
	
	public Waveform(List<Double> harmonics) throws WaveformException {
		super();
		setHarmonics(harmonics);
	}
	
	public Waveform(String name, String harmonics) throws ParseException, WaveformException {
		super();
		setName(name);
		setHarmonics(harmonics);
	}
	
	
	/**
	 * Sets the given harmonic to the given weight.
	 * Harmonic numbering starts at 1.
	 * @param harmonic		the harmonic number to be set
	 * @param weight		the weight of the given harmonic
	 * @throws WaveformException if given a harmonic number less than 1.
	 */
	public void setHarmonic(int harmonic, Double weight) throws WaveformException {
		double sum;
		if (inBounds(harmonic)) {
			int index = harmonic - 1;
			Double oldWeight= harmonics.get(index);
			harmonics.set(index, weight);
			sum = weight - oldWeight;
		} else {
			harmonics.ensureCapacity(harmonic);
			for (int h = harmonics.size(); h < harmonic-1; h++) {
				harmonics.add(0.0);
			}
			harmonics.add(weight);
			sum = weight;
		}
		totalWeights += sum;
	}
	
	/**
	 * Returns the weight of the given harmonic. Throws runtime exception if the given
	 * harmonic number is invalid (ie, less than 1). If the current harmonic
	 * is not yet in the list, make this list longer until it is!
	 */
	public Double getHarmonic(int harmonic) {
		if (harmonic <= harmonics.size()) {
			return harmonics.get(harmonic - 1);
		}
		try {
			setHarmonic(harmonic, 0.0);
		} catch (WaveformException e) {
			throw new IllegalArgumentException("Harmonic numbering must be equal " +
					"or greater than 1. Given: " + harmonic);
		}
		return 0.0;
	}
	
	/**
	 * Returns the weight of the harmonic at the ith position in the list. (0-indexed)
	 * This is NOT the same as getHarmonic, whose numbering starts at 1.
	 * This also throws an exception if the location is beyond length of list.
	 */
	public double get(int location) {
		return harmonics.get(location);
	}
	
	/**
	 * Return true if the given harmonic already has a place on the in the list
	 * of weights.
	 * @throws WaveformException   if given an invalid harmonic number (ie, less than 1)
	 */
	private boolean inBounds(int harmonic) throws WaveformException {
		if (harmonic < 1) {
		// This probably should have been a runtime exception. Oh well.
			throw new WaveformException("Harmonic Numbers start at 1");
		}
		return harmonic <= harmonics.size();
	}
	
	/**
	 * Return list of all weights
	 * @return
	 */
	public List<Double>	getHarmonics() {
		return harmonics;
	}
	
	/**
	 * Return the sum of all the weights. Sorry about the naming here and
	 * everywhere in this class. (This was one of the first things I wrote.)
	 */
	public double getWeight() {
		return totalWeights;
	}
	
	/**
	 * Return the number of harmonics currently included in the list, even if they are set to 0.
	 * @return
	 */
	public int getDepth() {
		return harmonics.size();
	}
	
		
	/**
	 * Assigns the relative overtone weights to the given list of Double, or arbitray number of values.
	 * @param harmonics
	 * @throws WaveformException
	 */
	public void setHarmonics(List<Double> harmonics) throws WaveformException {
		double total = 0;
		for (double weight : harmonics) {
			total += Math.abs(weight);
		}
		if (total == 0) {
			throw new WaveformException("Harmonics error: There must be at least one non-zero weight.");
		}
		totalWeights = total;
		this.harmonics = new ArrayList<Double>(harmonics);
	}
	
	/**
	 * Sets the weights to the harmonics as the ordered values in the given string, separated by commas
	 * @throws WaveformException
	 * @throws ParseException
	 */
	public void setHarmonics(String s) throws WaveformException, ParseException {
		setHarmonics(parse(s));
	}

	/**
	 * Returns a list of the doubles in the given string input in details format. Each can be separated by
	 * one newline or a comma.
	 * @throws ParseException If the separator is not followed pfoperly or it is not a string rep
	 * 		a number included.
	 */
	private List<Double> parse(String input) throws ParseException {
		String[] weights = input.split("[,\n]");  
		List<Double> harmonics = new ArrayList<Double>();
		
		try {
			for (String weight : weights) {
				harmonics.add(Double.valueOf(weight));
			}
			return harmonics;
		} catch (NumberFormatException e) {
			throw new ParseException("Incorrectly formatted Harmonics. Must be separated by comma or newline.");
		}
	}
	
	@Override
	String defaultName() {
		return "waveform";
	}
		
	/**
	 * Returns a nicely-formatted verions of the given double. It adds a 0 to the front of the version
	 * given in getDetails if it is less than 1, and includes one zero after the decimal
	 * point only if it is 0. Otherwise it shows like a simple integer if there is no decimal part.
	 */
	public static String formatWeight(Double weight) {
		weight = Math.abs(weight);
		String result = (weight < 1 && weight != 0) ? weight.toString() : formatted(weight);
		return result;
	}

	@Override
	public String getDetails() {
		return formatAll(harmonics);
	}
	
	/**
	 * Returns a string of details-formatted doubles from the given list. Numbers
	 * less than 1 have no initial 0, and number with no decimal part have no decimal 0 or point.
	 */
	public static String formatAll(List<Double> harms) {
		List<String> details = new ArrayList<String>();
		for (Double d : harms) {
			details.add(formatted(d));
		}
		return listToString(details);
	}

	/**
	 * Formats a single double for a detail display.
	 */
	private static String formatted(Double d) {
		// Check if d represents an integer before assigning pattern
		String pattern  = (d == Math.rint(d)) ? "" : ".###############";
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(d);
	}

	@Override
	public Listable getCopy() {
		try {
			Waveform wave = new Waveform(getName(), getDetails());
			return wave;

		} catch (TunerException e) {
			TunerException.impossible(e);
		}	
		return null;
	}

	@Override
	public void setDetails(String details) throws WaveformException, ParseException {
		setHarmonics(details);
	}

	@Override
	public int hashCode() {
		return harmonics.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Waveform)) {
			return false;
		}
		
		Waveform other = (Waveform) o;
		return harmonics.equals(other.harmonics) &&
			   name.equals(other.name);
	}
	
	@Override
	public String toString() {
		return name;
	}

}