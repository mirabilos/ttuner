package com.thefourthspecies.ttuner.model.data;


/**
 * A Relationship is defined by two Note names and an Adjustment from a pure
 * interval between the notes.
 */

import com.thefourthspecies.ttuner.model.exceptions.ParseException;
import com.thefourthspecies.ttuner.model.exceptions.RelationshipException;



public class Relationship {
	private String lower, upper;
	private int distance;
	private double interval;
	
	private String symbol;
	
	/**
	 * Create a new Relationship with the given string symbol
	 * @param input
	 * @throws RelationshipException	if the string is well-formed, but includes a bad interval
	 * @throws ParseException			if the string is of bad form.
	 */
	
	public Relationship(String input) throws RelationshipException, ParseException {
		parse(input);
	}
	

// // These constructors are just used in tests.
//	public Relationship(String left, String right) throws RelationshipException, ParseException {
//		initialize(left, right, 1, "");
//	}
//	
//	public Relationship(String left, String right, Adjustment adj) throws RelationshipException, ParseException {
//		initialize(left, right, adj.toDouble(), adj.getSymbol());
//	}
	
	
	/**
	 * In the closest possible pairing, this is the lower of the two notes.
	 * @return  lower Note
	 */
	public String   getLower()    { return lower;    }
	
	/**
	 * In the closest possible pairing, this is the higher of the two notes.
	 * @return   higher Note
	 */
	public String   getUpper()    { return upper;    }
	
	
	/**
	 * In the closest possible pairing, this is the distance between the notes in semitones.
	 * @return
	 */
	public int    getDistance() { return distance; }
	
	/**
	 * In the closest possible pairing, this is the ratio of the higher note to the lower note.
	 * Note: this is not necessarily the ratio between the two frequencies for the two notes. (Due
	 * to octave displacements)
	 * @return  higher:lower ratio.
	 */
	public double getInterval() { return interval; }
	
	
	/**
	 * Nicely-formatted string representation of the note and adjustment relationship.
	 * Thirds have the lower note on the left.
	 * Although internally represented as fourths, fifths reverse that so that they
	 * too can have the lower note on the left.
	 * @return
	 */
	public String getSymbol() { return symbol; }
	
	/**
	 * Assigns the given symbol to the given Relationship throws exceptions as per constructor
	 */
	public void setSymbol(String symbol) throws RelationshipException, ParseException {
		parse(symbol);
	}
	
	/**
	 * Sets this Relationship
	 * @param left	the note name to left of =
	 * @param right the note name to right of =
	 * @param interval 	the adjustment interval
	 * @param symbol	the adjustment symbol
	 * @throws RelationshipException
	 * @throws ParseException
	 */
	private void initialize(String left, String right, double interval, String symbol) 
			throws RelationshipException, ParseException {
		configure(left, right);
		assignInterval(interval);
		assignSymbol(symbol);
	}
	
	/**
	 * Determines the distance between notes with the two given names in their closest
	 * possible pairing. Records that as distance.
	 * Records the lower and upper of the two notes as lower and upper.
	 * 
	 * Throws ParseException if the note names are bad format.
	 */
	private void configure(String left, String right) throws ParseException {
		/* 
		 *  There are four cases:
		 *
		 *     "G = A"
		 *   delta = G(7) - A(9)
		 *  (delta = left - right)
		 *  *  
		 *                           lower    upper   distance
		 *                                      
		 *   -12  < delta < -6       right    left    delta+12  
		 *   C(0)-A(9)=-9            A(9)     C(0)       3         
		 *   
		 *    -6 <= delta <  0       left      right   -delta
		 *     G(7)-B(11)=-4         G(7)      B(11)    4
		 *   
		 *     0 <= delta <  6       right     left    delta
		 *    G(7)-D(2)=5             D(2)     G(7)      5  
		 *   
		 *    6  <= delta < 12       left    right     -delta+12
		 *    G(7)-C(0)=7             G(7)    C(0)      5
		 */
		
		Note l = new Note(left);
		Note r = new Note(right);
		
		int delta = l.getValue() - r.getValue();
		if (-12 < delta && delta < -6) {
			lower = right;
			upper = left;
			distance = 12+delta;
		} else 
			
		if (delta < 0) {
			lower = left;
			upper = right;
			distance = -delta;
		} else
			
		if (delta < 6) {
			lower = right;
			upper = left;
			distance = delta;
		} else
			
		if (delta < 12) {
			lower = left;
			upper = right;
			distance = 12-delta;
		} else {
			throw new IllegalArgumentException("Difference in note values not within -12<delta<12.");
		}
	}
	/**
	 * Determines the pure interval between the two notes and then adjusts it accordingly
	 * depending on the double value given as adjustment. Records this as interval
	 * 
	 * Only to be called after setting distance.
	 * 
	 * @param adj
	 * @throws RelationshipException
	 */
	private void assignInterval(double adj) throws RelationshipException {
		double pureInt;
		switch (distance) {
			case 3: pureInt = 6/5.0; break;
			case 4: pureInt = 5/4.0; break;
			case 5: pureInt = 4/3.0; break;
			
			case 0: 
				throw new RelationshipException(lower + ", " + upper + ": Sorry, this counts as a unison. You can't mess with it.");
			case 6: 
				throw new RelationshipException(lower + ", " + upper + ": That's a Tritone, silly!");
			default:
			    throw new RelationshipException(lower + ", " + upper + ": Major Thirds, Minor Thirds, and Perfect Fifths only, please!");
		}
		
		if (distance == 3 || distance == 4) {
			interval = pureInt*adj;
		} else {
			interval = pureInt/adj;
		}
	}

	/**
	 * Only to be called after setting upper and lower.
	 * Creates and assigns the symbolic representation of this relationship as either
	 * a major of minor third and a fifth, with the lower note on left.
	 * Following that is the adjustment, which is "" if it is pure.
	 * @param adjString
	 */
	private void assignSymbol(String adjString) {
		String s;
		// Since fifths are internally recorded as fourths.
		if (distance == 5) {
			s = upper + " = " + lower + " " + adjString;
		} else {
			s = lower + " = " + upper + " " + adjString;
		}
		symbol = s.trim();
	}

	/**
	 * Given a well-formed symbolic representation of a Relationship, parses the string
	 * and assigns all the fields of the Relationship apropriately.
	 * @param input
	 * @throws RelationshipException
	 * @throws ParseException
	 */
	private void parse(String input) throws RelationshipException, ParseException {
		// Eg: C = G -1/6P

		String[] div = input.split("=");  // { C, G -1/6P }
		String left = div[0].trim();  // C
		if (div.length != 2) {
			throw new ParseException("Incorrectly formatted relationship");
		}

		div = div[1].split("[+-]");   // { G, 1/6P }
		String right = div[0].trim();  // G
		
		double interval;
		String symbol;
		switch (div.length) {
			case 1: 
				interval = 1;
				symbol = "";
				break;
			case 2:
				String sign = input.contains("+") ? "+" : "-"; // -
				Adjustment adj = new Adjustment(sign + div[1]);  // -1/6P
				
				interval = adj.toDouble();
				symbol = adj.getSymbol();
				break;
			default:
				throw new ParseException("Incorrectly formatted relationship");
		}
		initialize(left, right, interval, symbol);
	}


	@Override
	public int hashCode() {
		return symbol.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Relationship)) {
			return false;
		}
		
		Relationship other = (Relationship) o;
		return symbol.equals(other.symbol);
	}

	@Override
	public String toString() {
		
		// Toying with idea of using unicode flat and sharp symbols...
//		String sharp = "\u266f";
//		String flat = "\u266d";
//		String s = symbol.replaceAll("#", sharp).replaceAll("b", flat);		// Unfortunately, they come with too much space to look good.
//		
		return symbol;
//		return s;
	}
	
	
	
	
}
