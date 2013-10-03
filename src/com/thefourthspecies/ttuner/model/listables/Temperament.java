package com.thefourthspecies.ttuner.model.listables;


import java.util.List;

import com.thefourthspecies.ttuner.model.data.Note;

/**
 * Besides being Listable, Temperaments must also all provide a list of notes for 
 * use by the tuner.
 * 
 * @author graham
 *
 */
public interface Temperament extends Listable {
	
	/**
	 * Return an ordered list of all the notes in this temperament, along with assigned
	 * frequencies in one octave, with A as the lowest (first) Note, if it exists. If the temperament is degenerate,
	 * order does not matter. 
	 * @param pitchA 	the pitch of A for the given list.
	 */
	public List<Note> getScale(double pitchA);
}
