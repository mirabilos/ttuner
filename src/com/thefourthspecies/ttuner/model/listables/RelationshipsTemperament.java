package com.thefourthspecies.ttuner.model.listables;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thefourthspecies.ttuner.model.data.Frequencies;
import com.thefourthspecies.ttuner.model.data.Note;
import com.thefourthspecies.ttuner.model.data.Relationship;
import com.thefourthspecies.ttuner.model.exceptions.FrequencyException;
import com.thefourthspecies.ttuner.model.exceptions.TemperamentException;
import com.thefourthspecies.ttuner.model.exceptions.TunerException;
import com.thefourthspecies.ttuner.model.states.TunerState;

/**
 * A collection of unique relationships in their given order. Equivalent 
 * temperaments must have same name bur may have a different order of relationships, 
 * but still the same ones.
 *
 * @author graham
 *
 */
public class RelationshipsTemperament extends AbstractTemperament {
	
	// Originally this was a LinkedHashSet to help out with equals(), 
	// but I thought it would be easier to handle ArrayAdapter in the
	// activities if I could just give it the actual list. In the end
	// I didn't use array adapter right, so it probably doesn't matter.
	// This definitely needs to be ordered due to remove(int)
	private List<Relationship> relationships;
	
	public RelationshipsTemperament() {
		super();
		relationships = new ArrayList<Relationship>();
	}
	
	/**
	 * The input String can have each relationship defined on a separate line, or
	 * separated by commas, but not both.
	 * @param details   a string of relationships 
	 * @throws TunerException 
	 */
	public RelationshipsTemperament(String details) throws TunerException {
		this();
		parse(details);
	}
	
	/**
	 * Defines a new RelationshipsTemperament with given name and details.
	 * @param name
	 * @param details
	 * @throws TunerException
	 */
	public RelationshipsTemperament(String name, String details) throws TunerException {
		this(details);
		this.name = name;
	}

	public List<Relationship> getRelationships() {
		return relationships;
	}
	
	/**
	 * Adds a relationship only if the Temperament doesn't already include a relationship
	 * for the given notes and its relationship doesn't conflict with anything already defined.
	 * If it does, then it throws. If it already contains the exact same Relationship,
	 * then nothing happens.
	 */
	public void add(Relationship rel) throws FrequencyException, TemperamentException {
		if (relationships.contains(rel)) {
			return;
		}
		
		for (Relationship r : relationships) {
			if (r.getUpper().equals(rel.getUpper())  && 
					r.getLower().equals(rel.getLower())) {
				throw new TemperamentException(rel + " conflicts with previously-defined " + r);
			}
		}

		try {
			relationships.add(rel);
			TunerState state = TunerState.getInstance();
			double pitchA = (state == null) ? TunerState.DEFAULT_PITCH : state.getPitchA();
			Frequencies.makeScale(this, pitchA);
		} catch (FrequencyException e) {
			relationships.remove(rel);
			throw e;
		}
	}
	
	/**
	 * Remove the given relationship from the temperament
	 */
	public void remove(Relationship rel) {
		relationships.remove(rel);
	}

	/**
	 * Remove the relationship at the given position in the list of relationships from the temperament
	 */
	public void remove(int index) {
		relationships.remove(index);
	}

	/**
	 * Returns the number of relationships in this temperament
	 */
	public int size() {
		return relationships.size();
	}

	
	/**	
	 * Parses the string of relationships symbols and assigns each one as a Relationship to this 
	 * temperament.
	 * @throws TunerException	if there is problem with the symbols or their compatability
	 */
	private void parse(String details) throws TunerException {
		String[] symbols = details.split("[,\n]");  
		for (String symbol : symbols) {
			add(new Relationship(symbol));
		}
	}


	@Override
	public String getDetails() {
		String details = listToString(new ArrayList<Relationship>(relationships));

		// Toying with idea of using unicode sharp and flat symbols for details.
		// In order to not mess with Store, might be best only to do this at Activity level.
//		details = details.replaceAll("#", sharp);       // These two lines use the unicode Flat and Sharp symbols
//		details = details.replaceAll("b", flat);		// Unfortunately, they come with too much space to look good.
		return details;
	}

	
	@Override
	public void setDetails(String details) throws TunerException {
		parse(details);
	}
	
	@Override
	public List<Note> getScale(double pitchA) {
		List<Note> scale;
		try {
			scale = Frequencies.makeScale(this, pitchA);
			return scale;
		} catch (FrequencyException e) {
			TunerException.impossible(e);
		}
		return null;
	}



	@Override
	public int hashCode() {
		return relationships.hashCode();
	}
	
	/**
	 * Temperaments are considered equal if they have the same name and  same collection of 
	 * relationships, but it doesn't matter what the order is. 
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RelationshipsTemperament)) {
			return false;
		}
		
		RelationshipsTemperament other = (RelationshipsTemperament) o;
		Set<Relationship> thisRels = new HashSet<Relationship>(relationships);
		Set<Relationship> otherRels = new HashSet<Relationship>(other.relationships);
		
		return thisRels.equals(otherRels) &&
			   name.equals(other.name); 
	}
	
	@Override
	public String toString() {
		return name;
	}

	
	
	
}

