package com.thefourthspecies.ttuner.model.data;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thefourthspecies.ttuner.model.exceptions.FrequencyException;
import com.thefourthspecies.ttuner.model.exceptions.ParseException;
import com.thefourthspecies.ttuner.model.listables.RelationshipsTemperament;



/**
 * Responsible for assigning and manipulating frequencies notes in RelationshipTemperaments,
 * as well as making scales for RelationshipTemperaments. This may not be the best place for this, but oh well.
 * @author graham
 *
 */
public class Frequencies {
	
	/**
	 * When differnece between two note frequencies if they are sufficiently the same.
	 */
	final private static double DELTA = 1E-6;
	
	/**
	 * Returns an ordered list of unique Notes with frequencies in given Temperament ordered with A at pitchA being the lowest.
	 * If the temperament is degenerate (ie, there are pitches that aren't related) order is arbitrary.
	 * 
	 * @throws FrequencyException If one note is assigned two different frequencies; ie, it's a bad temperament
	 */
	public static List<Note> makeScale(RelationshipsTemperament temp, double pitchA) throws FrequencyException {
		NotePool notes = calculate(temp, pitchA);
		List<Note> scale = new ArrayList<Note>(notes.getAll());
		if (notes.isComplete()) {
			Collections.sort(scale);
		} 
		return scale;
	}
	
	/**
	 * Returns unordered list of the Frequencies of all the notes in the given Pool. Can only be called
	 * if Note.isComplete() is true, otherwise throws NullPointerException. (I.e., 
	 * all notes have to have a Frequency).
	 * 
	 * (This is only public because I used it for tests)
	 */
	public static List<Double>	getAll(Iterable<Note> notes) {
		List<Double> freqs = new ArrayList<Double>();
		for (Note n : notes) {
			freqs.add(n.getFreq());
		}
		return freqs;
	}
	
	
	/**
	 * Assigns frequencies to every note in pool based on given temperament and given reference pitchA.
	 * @return
	 * @throws FrequencyException
	 */
	private static NotePool calculate(RelationshipsTemperament temperament, double pitchA) throws FrequencyException {
		double min = pitchA;
		double max = 2*pitchA;
		
		NotePool notes = makePool(temperament);
		
		initializeFreq(notes, pitchA);
		
		int counter = 0;
		while (!notes.isComplete() && counter < notes.size()) {
			for (Relationship r : temperament.getRelationships()) {
				Double lowerFreq = notes.get(r.getLower()).getFreq();
				Double upperFreq = notes.get(r.getUpper()).getFreq();

				if (lowerFreq != null) {
					upperFreq = normalize(lowerFreq * r.getInterval(), min, max);
					notes.get(r.getUpper()).setFreq(upperFreq);
				} else 
				if (upperFreq != null) {
					lowerFreq = normalize(upperFreq/r.getInterval(), min, max);
					notes.get(r.getLower()).setFreq(lowerFreq);
				}
			}
//			System.out.println("Temperament: " + temperament.getName() + ", Counter: " + counter + ", FreqNotes: " + withFreqs(notes));
			counter++;
		}
		return notes;
	}

//	 // For tests:
//	/**
//	 *	Prints the name of every notes that has an assigned freqency in the given notepool so far. 
//	 */
//	private static String withFreqs(NotePool notes) {
//		String s = "";
//		for (Note n : notes) {
//			if (n.getFreq() != null) {
//				s = s + n.getName() + " ";
//			}
//		}
//		return s;
//	}

	/**
	 * Prepares the NotePool for a new onslaught of frequency-assigning by setting all freqs (a freq can't be reset
	 * once it is set without throwing exception) to null and assigning a starter pitch to a note. By default
	 * that note is A, but if A isn't in notes, it'll be another.
	 */
	private static void initializeFreq(NotePool notes, double pitchA) {
		notes.clearFreqs();
		Note note = notes.get("A");
		
		try {
			if (note != null) {
				note.setFreq(pitchA);
			} else {
				note = getNote(notes);
				note.setFreq(pitchA);
			}
		} catch (FrequencyException e) {
			throw new IllegalStateException("Initalizing Frequencies shouldn't cause exception.");
		}
	}
	
	/**
	 * Extracts the notes mentioned in the given temperament to a NotePool
	 * @param temperament
	 * @return    the NotePool of extracted notes
	 */
	private static NotePool makePool(RelationshipsTemperament temperament) {
		NotePool notes = new NotePool();
		try {
			for (Relationship r : temperament.getRelationships()) {
				notes.add(r.getUpper());
				notes.add(r.getLower());
			}
		} catch (ParseException e) {
			throw new IllegalStateException("makePool() shouldn't throw an exception");
			
		}
		return notes;
	}
	
	
	
	/**
	 * returns a note (the first) from the note pool
	 * @param np
	 * @return
	 */
	private static Note getNote(NotePool np) {
		List<Note> notes = new ArrayList<Note>(np.getAll());
		return notes.get(0);
	}
	
	/**
	 * Returns a frequency that corresponds to the same note as given freq, but in an octave
	 * that puts it min <= freq < max
	 */
	private static double normalize(double freq, double min, double max) {
		if (doubleLessThan(freq, min)) {
			return normalize(freq*2, min, max);
		}
		if (doubleEquals(freq,  max) || !doubleLessThan(freq, max)) {
			return normalize(freq/2, min, max);
		}
		return freq;
	}
	
	
	/**
	 * True if difference between doubles is less than DELTA
	 */
	private static boolean doubleEquals(double a, double b) {
		// Probably should be doing this with ratios, but oh well. See Note for a better version
		// Unfortunately, due to foolish coupling in RelationshipsTemperaments to TunerState
		// tests are not easy to do right now.
		return Math.abs(a - b) < DELTA;
	}

	/**
	 * True if a < b by at least Delta. 
	
	 */
	private static boolean doubleLessThan(double a, double b) {
		// return a < b-DELTA should be the code, but I'm not retesting this now.
		return doubleEquals(a, b) ? false : a < b;
	}

	
}
	
