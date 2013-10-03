package com.thefourthspecies.ttuner.model.data;


import com.thefourthspecies.ttuner.model.exceptions.ParseException;


/**
 * Handles the fraction of a comma adjustment to a temperament relationship
 * @author graham
 *
 */
public class Adjustment {

	// The commas:
	final static private double SYNTONIC = 81/80.0;
	final static private double PYTHAGOREAN = 531441/524288.0;

	private double adjustment;
	private String symbol;
	
	/**
	 * A well-formed adjustment String is of the form [+-]\d* /0*[1-9]\d*[SP] (whITHOUT THE SPACE: WHere the denominator isn't zero.)
	 * There cannot be spaces within the string.
	 * @param symbol
	 */
	public Adjustment(String symbol) throws ParseException {
		adjustment = parse(symbol);
		this.symbol = symbol;
	}
	
	/**
	 * Returns the interval factor of this adjustment. 
	 */
	public double toDouble() { return adjustment; }
	
	/**
	 * Returns the string representation symbol for this adjustment
	 * @return
	 */
	public String getSymbol() { return symbol; }
	
	/**
	 * Given an adjustment string, returns its consequent interval factor as a double. Throws ParseException
	 * if the given string is incorretly-formatted
	 * @param symbol  A string in the form "+1/6P"
	 */ 
	private double parse(String symbol) throws ParseException {
		double comma;
		String last = symbol.substring(symbol.length()-1);
		if (last.equals("P")) {
			comma = PYTHAGOREAN;
		} else 
			if (last.equals("S")) {
				comma = SYNTONIC;
			} else {
				throw new ParseException("Adjustment missing correct comma designation: " + symbol);
			}
		
		try {
			String[] s = symbol.split("/");
			double numerator = Double.valueOf(s[0]);
			double denominator = Double.valueOf(s[1].substring(0, s[1].length()-1));

			return Math.pow(comma, numerator/denominator);

		} catch (NumberFormatException e) {
			throw new ParseException("Incorrectly formatted Adjustment fraction: " + symbol);
		}
	}

	
	@Override
	public String toString() {
		return "Adjustment [" + symbol + "]";
	}

}
